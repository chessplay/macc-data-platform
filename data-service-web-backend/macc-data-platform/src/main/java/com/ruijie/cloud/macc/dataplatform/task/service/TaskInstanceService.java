package com.ruijie.cloud.macc.dataplatform.task.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.ruijie.cloud.dc.core.domain.PageQuery;
import com.ruijie.cloud.dc.core.page.TableDataInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilterTemplate;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.QueryFilterType;
import com.ruijie.cloud.macc.dataplatform.metadata.service.DataSourceService;
import com.ruijie.cloud.macc.dataplatform.metadata.service.TableMetaDataService;
import com.ruijie.cloud.macc.dataplatform.task.domain.SubtaskInstance;
import com.ruijie.cloud.macc.dataplatform.task.domain.TaskInstance;
import com.ruijie.cloud.macc.dataplatform.task.domain.TaskTemplate;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.SubTaskDetailVo;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.TaskInstanceDetailVo;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.TaskInstanceSummaryVo;
import com.ruijie.cloud.macc.dataplatform.task.entity.SinkTemplate;
import com.ruijie.cloud.macc.dataplatform.task.entity.SubTaskQueryDto;
import com.ruijie.cloud.macc.dataplatform.task.entity.TaskInstanceQueryDto;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskErrorCode;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskException;
import com.ruijie.cloud.macc.dataplatform.task.mapper.SubtaskInstanceMapper;
import com.ruijie.cloud.macc.dataplatform.task.mapper.TaskInstanceMapper;
import com.ruijie.cloud.macc.dataplatform.task.mapper.TaskTemplateMapper;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.sink.SinkBuilder;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.source.SourceBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
// @RequiredArgsConstructor // 移除此注解以解决循环依赖
@Slf4j
public class TaskInstanceService {

    // 使用字段注入并配合 @Lazy 来解决循环依赖
    @Autowired
    private TaskTemplateMapper taskTemplateMapper;
    @Autowired
    private TaskInstanceMapper taskInstanceMapper;
    @Autowired
    private SubtaskInstanceMapper subtaskInstanceMapper;
    @Autowired
    @Lazy // 关键：延迟注入 TaskSubmissionService 以打破循环
    private TaskSubmissionService taskSubmissionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final int SUBTASK_BATCH_SIZE = 10;

    @Autowired
    private Map<DataSourceType, SourceBuilder> sourceBuilderMap;

    @Autowired
    private Map<DataSourceType, SinkBuilder> sinkBuilderMap; // <--- 在这里注入
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private TableMetaDataService tableMetaDataService;

    /**
     * 启动一个新的任务实例，包含参数分片、创建父实例记录等逻辑
     */
    /**
     * 启动一个新的任务实例，包含参数预校验、参数分片、创建父实例记录等逻辑
     * @param templateId 任务模板ID
     * @param taskParamsWrapper 包含 "taskParams": { ... } 的请求体
     * @return 新创建的任务实例ID (instanceId)
     */
    @Transactional
    public String startNewTaskInstance(Long templateId, HashMap<String, HashMap<String, Object>> taskParamsWrapper) {
        TaskTemplate taskTemplate = taskTemplateMapper.selectById(templateId);
        if (taskTemplate == null) {
            throw new TaskException(TaskErrorCode.TASK_TEMPLATE_NOT_EXISTED, templateId);
        }

        HashMap<String, Object> originalParams = Optional.ofNullable(taskParamsWrapper)
                .map(wrapper -> wrapper.get("taskParams"))
                .orElseGet(HashMap::new);

        // ======================= 参数预校验步骤 =======================
        try {
            // 1. 获取数据源类型和对应的 SourceBuilder
            DataSourceModel sourceDataSourceModel = dataSourceService.selectByDataSourceKey(taskTemplate.getSourceDataKey());
            if (sourceDataSourceModel == null) {
                throw new TaskException(TaskErrorCode.METADATA_IS_NOT_EXISTED, "源数据源: " + taskTemplate.getSourceDataKey());
            }
            SourceBuilder sourceBuilder = sourceBuilderMap.get(sourceDataSourceModel.getDbType());
            if (sourceBuilder == null) {
                throw new TaskException(TaskErrorCode.UNKNOWN_ERROR, "不支持的数据源类型: " + sourceDataSourceModel.getDbType());
            }
            TableInfo tableInfo = tableMetaDataService.tableInfo(taskTemplate.getSourceDataKey(), taskTemplate.getSourceTableName());
            if (tableInfo == null) {
                throw new TaskException(TaskErrorCode.METADATA_IS_NOT_EXISTED, "源数据表: " + taskTemplate.getSourceTableName());
            }

            // 2. 将用户传入的参数转换为校验方法需要的 QueryFilter 列表
            List<QueryFilter> filtersToValidate = convertParamsToQueryFilters(originalParams, taskTemplate.getSourceQueryFilter());

            // 3. 调用校验方法，如果参数不合法，这里会直接抛出异常，中断执行
            sourceBuilder.checkAndParseParams(tableInfo, filtersToValidate);
            log.info("API parameters pre-validation successful for template ID: {}", templateId);

            // 3. 【关键修改】调用 Sink Builder 校验其专属参数
            DataSourceModel sinkDataSourceModel = dataSourceService.selectByDataSourceKey(taskTemplate.getSinkDataKey());
            if (sinkDataSourceModel != null) {
                SinkBuilder sinkBuilder = sinkBuilderMap.get(sinkDataSourceModel.getDbType());
                if (sinkBuilder != null) {
                    SinkTemplate sinkTemplate = new SinkTemplate();
                    sinkTemplate.setDataSourceModel(sinkDataSourceModel);
                    sinkTemplate.setTableName(taskTemplate.getSinkTableName());
                    sinkBuilder.validateSpecificParameters(originalParams,sinkTemplate);
                    log.info("Sink-specific parameters validation successful for sink type: {}", sinkDataSourceModel.getDbType());
                }
            }

        } catch (TaskException e) {
            log.error("API parameters pre-validation failed for template ID {}: {}", templateId, e.getMessage());
            throw e; // 直接向上抛出业务异常，请求会立即失败并返回清晰的错误信息
        } catch (Exception e) {
            log.error("An unexpected error occurred during API parameter pre-validation for template ID {}.", templateId, e);
            throw new TaskException(TaskErrorCode.UNKNOWN_ERROR, "参数预校验时发生未知错误。");
        }
        // ==========================================================

        List<Map<String, Object>> subtaskParamsList = shardParameters(originalParams, taskTemplate);

        String taskInstanceName;
        Object nameFromParams = originalParams.get("taskName");
        if (nameFromParams != null && !nameFromParams.toString().trim().isEmpty()) {
            taskInstanceName = nameFromParams.toString();
            log.info("使用参数中提供的任务实例名称: {}", taskInstanceName);
        } else {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            taskInstanceName = String.format("%s_%s", taskTemplate.getName(), timestamp);
            log.info("未提供任务实例名称，自动生成名称: {}", taskInstanceName);
        }

        TaskInstance taskInstance = new TaskInstance();
        String instanceId = UUID.randomUUID().toString();
        taskInstance.setInstanceId(instanceId);
        taskInstance.setTemplateId(templateId);
        taskInstance.setName(taskInstanceName);
        taskInstance.setOverallStatus("QUEUED");
        taskInstance.setTotalSubTasks(subtaskParamsList.size());
        try {
            originalParams.remove("taskName");
            taskInstance.setInitialRequestParamsJson(objectMapper.writeValueAsString(originalParams));
        } catch (Exception e) {
            log.error("Failed to serialize initial request params for instance {}", instanceId, e);
        }

        taskInstanceMapper.insert(taskInstance);
        log.info("Created new TaskInstance record with ID: {}", instanceId);

        boolean accepted = taskSubmissionService.submitTasks(templateId, subtaskParamsList, instanceId, taskInstanceName);

        if (!accepted) {
            throw new TaskException(TaskErrorCode.TASK_START_ERROR, "Submission service rejected the task instance.");
        }

        return instanceId;
    }


    /**
     * 分页查询任务实例列表 (直接查主表，不再聚合)
     */
    public TableDataInfo<TaskInstanceSummaryVo> pageTaskInstances(TaskInstanceQueryDto dto, PageQuery pageQuery) {
        LambdaQueryWrapper<TaskInstance> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(dto.getInstanceIdFilter()), TaskInstance::getInstanceId, dto.getInstanceIdFilter());
        if (StringUtils.hasText(dto.getStatusFilter())) {
            lqw.eq(TaskInstance::getOverallStatus, dto.getStatusFilter());
        }
        lqw.orderByDesc(TaskInstance::getUpdateTime);

        Page<TaskInstance> pageResult = taskInstanceMapper.selectPage(pageQuery.build(), lqw);

        Page<TaskInstanceSummaryVo> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        if (pageResult.getRecords().isEmpty()) {
            return TableDataInfo.build(voPage);
        }

        List<Long> templateIds = pageResult.getRecords().stream().map(TaskInstance::getTemplateId).distinct().collect(Collectors.toList());
        Map<Long, String> templateNameMap = taskTemplateMapper.selectBatchIds(templateIds).stream()
                .collect(Collectors.toMap(TaskTemplate::getId, TaskTemplate::getName));

        List<TaskInstanceSummaryVo> voList = pageResult.getRecords().stream().map(instance -> {
            TaskInstanceSummaryVo vo = new TaskInstanceSummaryVo();
            BeanUtils.copyProperties(instance, vo);
            vo.setTemplateName(templateNameMap.get(instance.getTemplateId()));
            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return TableDataInfo.build(voPage);
    }

    /**
     * 获取指定任务实例的详细信息
     */
    public TaskInstanceDetailVo getTaskInstanceDetails(String instanceId) {
        LambdaQueryWrapper<TaskInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(TaskInstance::getInstanceId, instanceId);
        TaskInstance taskInstance = taskInstanceMapper.selectOne(lqw);

        if (taskInstance == null) {
            throw new TaskException(TaskErrorCode.TASK_NOT_FOUND, "Task Instance with ID " + instanceId + " not found.");
        }

        TaskInstanceDetailVo detailVo = new TaskInstanceDetailVo();
        BeanUtils.copyProperties(taskInstance, detailVo);

        TaskTemplate template = taskTemplateMapper.selectById(taskInstance.getTemplateId());
        if(template != null) {
            detailVo.setTemplateName(template.getName());
        }

        List<SubtaskInstance> subtasks = subtaskInstanceMapper.selectList(
                Wrappers.lambdaQuery(SubtaskInstance.class).eq(SubtaskInstance::getTaskInstanceId, instanceId)
        );

        List<SubTaskDetailVo> subtaskDetailVos = subtasks.stream()
                .map(this::convertLogToSubTaskDetailVo)
                .sorted(Comparator.comparing(SubTaskDetailVo::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        detailVo.setSubTasks(subtaskDetailVos);

        return detailVo;
    }

    /**
     * 停止整个任务实例
     */
    public boolean stopTaskInstance(String instanceId) {
        log.info("Requesting to stop all subtasks for instance: {}", instanceId);
        List<SubtaskInstance> subtasks = subtaskInstanceMapper.selectList(
                Wrappers.lambdaQuery(SubtaskInstance.class).eq(SubtaskInstance::getTaskInstanceId, instanceId)
        );

        boolean allSuccess = true;
        for(SubtaskInstance subtask : subtasks) {
            try {
                taskSubmissionService.stopInstanceByCorrelationId(subtask.getCorrelationId());
            } catch (Exception e) {
                log.error("Failed to request stop for subtask with correlation ID: {}", subtask.getCorrelationId(), e);
                allSuccess = false;
            }
        }

        if (allSuccess) {
            TaskInstance instance = taskInstanceMapper.selectOne(Wrappers.lambdaQuery(TaskInstance.class).eq(TaskInstance::getInstanceId, instanceId));
            if (instance != null) {
                instance.setOverallStatus("STOPPING");
                taskInstanceMapper.updateById(instance);
            }
        }
        return allSuccess;
    }

    /**
     * 重跑任务实例中所有失败的子任务
     */
    public boolean rerunFailedSubtasksInInstance(String instanceId) {
        List<SubtaskInstance> allSubtasks = subtaskInstanceMapper.selectList(
                Wrappers.lambdaQuery(SubtaskInstance.class).eq(SubtaskInstance::getTaskInstanceId, instanceId)
        );

        List<SubtaskInstance> failedTasksToRerun = allSubtasks.stream()
                .filter(subtask -> "FAILURE".equals(taskSubmissionService.translateDsState(subtask.getFinalDsStatus())) || "FAILED_PERMANENTLY".equals(subtask.getSubmissionStatus()))
                .collect(Collectors.toList());

        if (failedTasksToRerun.isEmpty()) {
            log.info("No failed subtasks to rerun for instance: {}", instanceId);
            return true;
        }

        log.info("Found {} failed subtasks to rerun for instance: {}", failedTasksToRerun.size(), instanceId);
        boolean allRerunAccepted = true;
        for (SubtaskInstance failedTask : failedTasksToRerun) {
            try {
                taskSubmissionService.rerunInstanceByCorrelationId(failedTask.getCorrelationId());
            } catch (Exception e) {
                log.error("Failed to enqueue rerun for subtask with correlationId: {}", failedTask.getCorrelationId(), e);
                allRerunAccepted = false;
            }
        }
        return allRerunAccepted;
    }


    @Transactional
    public void updateParentTaskInstanceStatus(String taskInstanceId) {
        if (taskInstanceId == null) return;

        TaskInstance parentInstance = taskInstanceMapper.selectOne(
                new LambdaQueryWrapper<TaskInstance>().eq(TaskInstance::getInstanceId, taskInstanceId).last("FOR UPDATE")
        );
        if (parentInstance == null) {
            log.warn("updateParentTaskInstanceStatus: TaskInstance with ID {} not found.", taskInstanceId);
            return;
        }

        // 定义所有非终态的子任务状态
        List<String> nonTerminalStatuses = Arrays.asList(
                SubtaskInstance.STATUS_QUEUED,
                SubtaskInstance.STATUS_SUBMITTING,
                SubtaskInstance.STATUS_TRACKING,
                SubtaskInstance.STATUS_RETRY_SCHEDULED
        );

        // 1. 统计尚未完成的子任务数量
        long unfinishedCount = subtaskInstanceMapper.selectCount(
                new LambdaQueryWrapper<SubtaskInstance>()
                        .eq(SubtaskInstance::getTaskInstanceId, taskInstanceId)
                        .in(SubtaskInstance::getSubmissionStatus, nonTerminalStatuses)
        );

        if (unfinishedCount == 0) {
            // ========== 所有子任务都已结束，判断父任务的最终状态 ==========

            // 2. 统计永久失败的子任务数量
            long failedCount = subtaskInstanceMapper.selectCount(
                    new LambdaQueryWrapper<SubtaskInstance>()
                            .eq(SubtaskInstance::getTaskInstanceId, taskInstanceId)
                            .in(SubtaskInstance::getSubmissionStatus,
                                    SubtaskInstance.STATUS_FAILED_PERMANENTLY,
                                    SubtaskInstance.STATUS_SUBMIT_FAILED_API,
                                    SubtaskInstance.STATUS_SUBMIT_FAILED_NO_ID,
                                    SubtaskInstance.STATUS_STATUS_CHECK_ERROR
                            )
            );

            // 3. 统计成功完成的子任务数量
            long completedCount = subtaskInstanceMapper.selectCount(
                    new LambdaQueryWrapper<SubtaskInstance>()
                            .eq(SubtaskInstance::getTaskInstanceId, taskInstanceId)
                            .eq(SubtaskInstance::getSubmissionStatus, SubtaskInstance.STATUS_COMPLETED)
            );

            // 更新父实例的统计数据
            parentInstance.setCompletedSubTasks((int) completedCount);
            parentInstance.setFailedSubTasks((int) failedCount);

            // 4. 根据统计结果判断最终状态
            if (failedCount > 0) {
                parentInstance.setOverallStatus("PARTIALLY_FAILED");
            } else if (completedCount == parentInstance.getTotalSubTasks()) {
                parentInstance.setOverallStatus("COMPLETED");
            } else {
                // 如果没有失败，且成功的数量不等于总数，说明剩下的都是被停止的
                parentInstance.setOverallStatus("STOPPED");
            }

            if (parentInstance.getEndTime() == null) {
                parentInstance.setEndTime(new Date());
            }

        } else {
            // ========== 【关键修改】还有子任务在运行或排队中 ==========
            // 定义父任务的终态集合
            List<String> terminalParentStatuses = Arrays.asList("COMPLETED", "PARTIALLY_FAILED", "STOPPED");

            // 如果父任务当前处于任何一个终态，或者还是初始的QUEUED状态，都应该将其激活为RUNNING
            if (terminalParentStatuses.contains(parentInstance.getOverallStatus()) || "QUEUED".equals(parentInstance.getOverallStatus())) {
                parentInstance.setOverallStatus("RUNNING");

                // 如果是从终态“复活”，需要重置其结束时间
                if(terminalParentStatuses.contains(parentInstance.getOverallStatus())) {
                    parentInstance.setEndTime(null);
                }

                // 如果是第一次从QUEUED变为RUNNING，记录开始时间
                if (parentInstance.getStartTime() == null) {
                    parentInstance.setStartTime(new Date());
                }
            }
            // 如果已经是 RUNNING 或 STOPPING，则保持不变，等待所有子任务完成
        }

        taskInstanceMapper.updateById(parentInstance);
        log.info("Updated parent task instance {} status to: {}. Unfinished subtasks: {}, Completed: {}, Failed: {}",
                taskInstanceId, parentInstance.getOverallStatus(), unfinishedCount, parentInstance.getCompletedSubTasks(), parentInstance.getFailedSubTasks());
    }



    /**
     * 服务启动时，恢复处理所有处于中间状态的任务实例
     */
    public void recoverInFlightInstances() {
        LambdaQueryWrapper<TaskInstance> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(TaskInstance::getOverallStatus, "RUNNING", "STOPPING");
        List<TaskInstance> inFlightInstances = taskInstanceMapper.selectList(wrapper);

        if (inFlightInstances.isEmpty()) {
            log.info("No in-flight (RUNNING/STOPPING) task instances found to recover on startup.");
            return;
        }

        log.warn("Found {} in-flight task instances to recover on startup. Triggering status re-check...", inFlightInstances.size());
        for (TaskInstance instance : inFlightInstances) {
            try {
                log.info("Recovering status for instance: {}", instance.getInstanceId());
                updateParentTaskInstanceStatus(instance.getInstanceId());
            } catch (Exception e) {
                log.error("Error during recovery of task instance {}: {}", instance.getInstanceId(), e.getMessage(), e);
            }
        }
    }

    // ----------- 私有辅助方法 -----------

    /**
     * 参数分片逻辑
     */
    private List<Map<String, Object>> shardParameters(Map<String, Object> originalParams, TaskTemplate taskTemplate) {
        List<Map<String, Object>> subTaskParamsList = new ArrayList<>();
        List<QueryFilterTemplate> filters = taskTemplate.getSourceQueryFilter();

        boolean splitOccurred = false;
        if (filters != null && !filters.isEmpty()) {
            QueryFilterTemplate firstFilter = filters.get(0);
            if (firstFilter.getType() == QueryFilterType.IN) {
                String placeholder = firstFilter.getValue();
                String shardParamName = (placeholder != null) ? placeholder.toLowerCase() : null;

                if (shardParamName != null && originalParams.containsKey(shardParamName) && originalParams.get(shardParamName) instanceof List) {
                    List<?> originalList = (List<?>) originalParams.get(shardParamName);
                    if (originalList != null && !originalList.isEmpty()) {
                        for (List<?> subList : Lists.partition(originalList, SUBTASK_BATCH_SIZE)) {
                            Map<String, Object> subParams = new HashMap<>(originalParams);
                            subParams.put(shardParamName, subList);
                            subTaskParamsList.add(subParams);
                        }
                        splitOccurred = true;
                    }
                }
            }
        }

        if (!splitOccurred) {
            subTaskParamsList.add(new HashMap<>(originalParams));
        }

        return subTaskParamsList;
    }

    /**
     * 将 SubtaskInstance 实体转换为 SubTaskDetailVo 视图对象
     */
    private SubTaskDetailVo convertLogToSubTaskDetailVo(SubtaskInstance subtask) {
        SubTaskDetailVo subVo = new SubTaskDetailVo();
        BeanUtils.copyProperties(subtask, subVo);
        subVo.setDsStatusString(taskSubmissionService.translateDsState(subtask.getFinalDsStatus()));
        subVo.setYarnLogUrl(taskSubmissionService.buildYarnLogUrl(subtask.getYarnApplicationId()));
        return subVo;
    }

    private List<QueryFilter> convertParamsToQueryFilters(Map<String, Object> params, List<QueryFilterTemplate> templates) {
        if (params == null || params.isEmpty() || templates == null || templates.isEmpty()) {
            return Collections.emptyList();
        }

        // 为了快速查找，将模板列表转换为Map
        Map<String, QueryFilterTemplate> templateMap = templates.stream()
                .collect(Collectors.toMap(t -> t.getValue().toLowerCase(), Function.identity(), (t1, t2) -> t1));

        List<QueryFilter> filters = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String paramName = entry.getKey().toLowerCase();
            Object paramValue = entry.getValue();

            // 如果参数在模板中有定义，则转换
            if (templateMap.containsKey(paramName)) {
                QueryFilterTemplate template = templateMap.get(paramName);
                QueryFilter filter = new QueryFilter();
                filter.setType(template.getType());
                filter.setField(template.getField());

                // 将参数值统一包装成 List<Object>
                if (paramValue instanceof List) {
                    filter.setValue((List<Object>) paramValue);
                } else {
                    filter.setValue(Collections.singletonList(paramValue));
                }
                filters.add(filter);
            }
        }
        return filters;
    }
}