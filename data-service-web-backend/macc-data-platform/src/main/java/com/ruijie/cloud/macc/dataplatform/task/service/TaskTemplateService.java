package com.ruijie.cloud.macc.dataplatform.task.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fasterxml.jackson.core.JsonProcessingException; // 引入 Jackson 异常
import com.fasterxml.jackson.databind.ObjectMapper;       // 引入 ObjectMapper

import com.ruijie.cloud.dc.core.domain.PageQuery;
import com.ruijie.cloud.dc.core.page.TableDataInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.DataSourceModel;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilterTemplate;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.TableInfo;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.constant.DataSourceType;
import com.ruijie.cloud.macc.dataplatform.metadata.service.DataSourceService;
import com.ruijie.cloud.macc.dataplatform.metadata.service.TableMetaDataService;
import com.ruijie.cloud.macc.dataplatform.task.config.DolphinProperties;

import com.ruijie.cloud.macc.dataplatform.task.constant.TaskParamsConstant;
import com.ruijie.cloud.macc.dataplatform.task.domain.TaskInstance;
import com.ruijie.cloud.macc.dataplatform.task.domain.TaskTemplate;

import com.ruijie.cloud.macc.dataplatform.task.domain.bo.TaskTemplateBo;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.ParameterDescriptor;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.TaskTemplateVo;
import com.ruijie.cloud.macc.dataplatform.task.ds.common.PageInfo;
import com.ruijie.cloud.macc.dataplatform.task.ds.core.DolphinClient;
import com.ruijie.cloud.macc.dataplatform.task.ds.enums.DsPriority;
import com.ruijie.cloud.macc.dataplatform.task.ds.instance.ProcessInstanceQueryResp;
import com.ruijie.cloud.macc.dataplatform.task.ds.process.ProcessDefineParam;
import com.ruijie.cloud.macc.dataplatform.task.ds.process.ProcessDefineResp;
import com.ruijie.cloud.macc.dataplatform.task.ds.process.TaskDefinition;
import com.ruijie.cloud.macc.dataplatform.task.ds.task.SeaTunnelTask;
import com.ruijie.cloud.macc.dataplatform.task.ds.util.TaskDefinitionUtils;
import com.ruijie.cloud.macc.dataplatform.task.ds.util.TaskLocationUtils;
import com.ruijie.cloud.macc.dataplatform.task.ds.util.TaskRelationUtils;
import com.ruijie.cloud.macc.dataplatform.task.entity.*;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskErrorCode;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskException;
import com.ruijie.cloud.macc.dataplatform.task.mapper.TaskInstanceMapper;
import com.ruijie.cloud.macc.dataplatform.task.mapper.TaskTemplateMapper;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.common.Commonbuilder;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.sink.SinkBuilder;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.source.SourceBuilder;
import com.ruijie.cloud.macc.dataplatform.task.service.contextbuilder.transform.FieldMapperBuilder;


import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j; // 添加 Slf4j 注解
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // 引入 Spring 的 StringUtils

import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data业务Service业务层处理
 *
 * @author zhouliwang
 * @date 2023-10-18
 */
@Slf4j // 添加 Slf4j 注解以便使用 log
@RequiredArgsConstructor
@Service
public class TaskTemplateService {

    private final TaskTemplateMapper baseMapper;
    private final DolphinClient dolphinClient;
    private final DolphinProperties dolphinProperties;
    private final DataSourceService dataSourceService;
    private final TableMetaDataService tableMetaDataService;
//    private final TaskService taskService;
    private final TaskInstanceMapper taskInstanceMapper; // 新增这一行注入

    @Autowired
    private Map<DataSourceType, SourceBuilder> sourceBuilderMap;
    @Autowired
    private Map<DataSourceType, SinkBuilder> sinkBuilderMap;

    @Value("${server.host}")
    private String host;
    @Value("${server.port}")
    private String port;
    @Value("${server.servlet.contextPath}")
    private String contextPath;
    @Value("${job.engine:seaTunnel}")
    private String engine;
    @Value("${job.parallelism:2}")
    private Integer parallelism;
    @Value("${job.mode:BATCH}")
    private String mode;
    @Value("${job.checkpoint-interval:10000}")
    private Integer checkpointInterval;
    @Value("${job.partition-num:10}")
    private Integer partitionNum;

    @Value("${job.flink.execution-mode:standalone}") // 默认为 standalone，以保证向后兼容
    private String flinkExecutionMode;

    @Value("${job.flink.yarn-queue:default}") // 默认为 default 队列
    private String flinkYarnQueue;

    /**
     * 查询Data业务
     */
    public TaskTemplateVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询Data业务列表
     */
    public TableDataInfo<TaskTemplateVo> queryPageList(TaskQueryDto bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TaskTemplate> lqw = Wrappers.lambdaQuery();
        // 组装查询条件
        lqw.like(StringUtils.hasText(bo.getName()), TaskTemplate::getName, bo.getName());
        lqw.like(StringUtils.hasText(bo.getSourceTableName()), TaskTemplate::getSourceTableName, bo.getSourceTableName());
        lqw.like(StringUtils.hasText(bo.getSinkTableName()), TaskTemplate::getSinkTableName, bo.getSinkTableName());
        lqw.like(StringUtils.hasText(bo.getSourceDataKey()), TaskTemplate::getSourceDataKey, bo.getSourceDataKey());
        lqw.like(StringUtils.hasText(bo.getSinkDataKey()), TaskTemplate::getSinkDataKey, bo.getSinkDataKey());
        lqw.orderByDesc(TaskTemplate::getUpdateTime); // 按更新时间降序

        Page<TaskTemplateVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 获取所有任务模板列表（按名称排序）
     */
    public List<TaskTemplate> list() {
        LambdaQueryWrapper<TaskTemplate> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(TaskTemplate::getName); // 升序排序
        return baseMapper.selectList(lqw);
    }

    @Transactional
    public Boolean insertByBo(TaskTemplateBo bo) {
        validEntityBeforeSave(bo);
        Long taskCode = dolphinClient.opsForProcess().generateTaskCode(dolphinProperties.getProjectCode(), 1).get(0);
        String configScript = generateTaskScript(bo);
        SeaTunnelTask seatunnelTask = createSeaTunnelTask(configScript);
        TaskDefinition taskDefinition = TaskDefinitionUtils.createDefaultTaskDefinition(taskCode, seatunnelTask);
        ProcessDefineParam pcr = createProcessDefineParam(bo, taskCode, Collections.singletonList(taskDefinition));
        ProcessDefineResp resp = dolphinClient.opsForProcess().create(dolphinProperties.getProjectCode(), pcr);
        dolphinClient.opsForProcess().online(dolphinProperties.getProjectCode(), resp.getCode());

        TaskTemplate add = BeanUtil.toBean(bo, TaskTemplate.class);
        add.setProcessCode(resp.getCode());
        add.setTaskCode(taskCode);

        // 【修改】调用新的 generateStartParams 方法
        List<ParameterDescriptor> startParams = generateStartParams(add.getSourceQueryFilter(),add);
        add.setStartParams(startParams);

        int insertedRows = baseMapper.insert(add);
        if (insertedRows > 0 && add.getId() != null) {
            add.setRequestExample(generateRequestExample(add.getId(), startParams));
            return baseMapper.updateById(add) > 0;
        }
        return false;
    }

    /**
     * 修改Data业务
     */
    @Transactional
    public Boolean updateByBo(TaskTemplateBo bo) {
        validEntityBeforeSave(bo);
        dolphinClient.opsForProcess().offline(dolphinProperties.getProjectCode(), bo.getProcessCode());
        String configScript = generateTaskScript(bo);
        SeaTunnelTask seatunnelTask = createSeaTunnelTask(configScript);
        TaskDefinition taskDefinition = TaskDefinitionUtils.createDefaultTaskDefinition(bo.getTaskCode(), seatunnelTask);
        ProcessDefineParam pcr = createProcessDefineParam(bo, bo.getTaskCode(), Collections.singletonList(taskDefinition));
        ProcessDefineResp resp = dolphinClient.opsForProcess().update(dolphinProperties.getProjectCode(), pcr, bo.getProcessCode());

        TaskTemplate update = BeanUtil.toBean(bo, TaskTemplate.class);

        // 【修改】调用新的 generateStartParams 方法
        List<ParameterDescriptor> startParams = generateStartParams(update.getSourceQueryFilter(),update);
        update.setStartParams(startParams);
        update.setRequestExample(generateRequestExample(bo.getId(), startParams));

        boolean updateResult = baseMapper.updateById(update) > 0;
        if (updateResult) {
            dolphinClient.opsForProcess().online(dolphinProperties.getProjectCode(), resp.getCode());
        }
        return updateResult;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(TaskTemplateBo bo) {
        // 1. 校验模板名称在数据库中是否重复
        LambdaQueryWrapper<TaskTemplate> nameCheckLqw = new LambdaQueryWrapper<>();
        nameCheckLqw.eq(TaskTemplate::getName, bo.getName());
        if (bo.getId() != null) { // 更新时排除自身
            nameCheckLqw.ne(TaskTemplate::getId, bo.getId());
        }
        if (baseMapper.exists(nameCheckLqw)) {
            throw new TaskException(TaskErrorCode.TASK_TEMPLATE_NAME_DUPLICATED);
        }

        // 2. 校验 DS 中工作流名称是否重复 (仅在新增时，或更新时名称改变了)
        TaskTemplate existingTemplate = null;
        if(bo.getId() != null) {
            existingTemplate = baseMapper.selectById(bo.getId());
        }

        // 如果是新增，或者更新时名称发生了变化，则需要检查DS名称
        if (existingTemplate == null || !Objects.equals(existingTemplate.getName(), bo.getName())) {
            try {
                // 使用分页查询可能更安全，即使预期只有一个
                List<ProcessDefineResp> processDefineResps = dolphinClient.opsForProcess().page(
                        dolphinProperties.getProjectCode(), 1, 10, bo.getName()); // 只查少量匹配的即可

                // 精确匹配名称
                boolean dsNameExists = processDefineResps.stream()
                        .anyMatch(resp -> Objects.equals(resp.getName(), bo.getName()) &&
                                (bo.getProcessCode() == null || resp.getCode() != bo.getProcessCode())); // 更新时排除自身 code
                if (dsNameExists) {
                    log.error("DS workflow name '{}' already exists in project {}.", bo.getName(), dolphinProperties.getProjectCode());
                    throw new TaskException(TaskErrorCode.DS_TASK_NAME_DUPLICATED);
                }
            } catch (Exception e) {
                log.error("Error checking DS workflow name uniqueness for '{}'. Assuming it might exist.", bo.getName(), e);
                // 根据策略，可以选择是抛出异常还是允许继续（有风险）
                throw new TaskException(TaskErrorCode.UNKNOWN_ERROR, "Failed to check DS workflow name uniqueness.");
            }
        }
        // 3. 可选：校验 defaultDsPriority 的值是否在 DS 支持的范围内
        if (StringUtils.hasText(bo.getDefaultDsPriority()) &&
                !TaskSubmissionService.TaskSubmissionRequest.VALID_DS_PRIORITIES_SET.contains(bo.getDefaultDsPriority().toUpperCase())) {
            throw new TaskException(TaskErrorCode.TASK_PARAMETER_PARSED_ERROR, "defaultDsPriority");
        }

        // TODO: 根据需要添加其他校验，例如数据源是否存在，表是否存在等
    }

    /**
     * 批量删除Data业务 (包含 DS 工作流删除)
     */
    @Transactional
    public Boolean deleteWithValidById(Long[] ids, Boolean isValid) {
        if (isValid) {
            Arrays.stream(ids).forEach((id) -> {
                TaskTemplate template = baseMapper.selectById(id);
                if (template == null) {
                    log.warn("Task template with ID {} not found, skipping deletion.", id);
                    return; // 跳过不存在的模板
                }
                Long processCode = template.getProcessCode();

                // 1. 检查是否有正在运行或未完成的实例 (使用新的逻辑)
                try {
                    // 构建查询条件，检查是否存在与该模板ID关联的任何任务实例
                    LambdaQueryWrapper<TaskInstance> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(TaskInstance::getTemplateId, id);

                    // 使用 taskInstanceMapper.exists() 进行高效检查
                    if (taskInstanceMapper.exists(wrapper)) {
                        log.error("Cannot delete template ID {}: Found existing task instances in mdp_task_instance table.", id);
                        throw new TaskException(TaskErrorCode.TASK_INSTANCE_EXISTED);
                    }
                } catch (Exception e) {
                    // 如果抛出的是我们自定义的异常，则直接再次抛出
                    if (e instanceof TaskException) {
                        throw (TaskException) e;
                    }
                    // 其他数据库异常等，包装成我们的通用未知错误
                    log.error("Error checking for existing task instances for template ID {}. Deletion aborted.", id, e);
                    throw new TaskException(TaskErrorCode.UNKNOWN_ERROR, "Failed to check for existing task instances before deletion.");
                }

                // 2. 删除 DS 工作流 (先尝试下线，再删除)
                if (processCode != null) {
                    try {
                        log.info("Attempting to offline DS workflow definition, Code: {}", processCode);
                        dolphinClient.opsForProcess().offline(dolphinProperties.getProjectCode(), processCode);
                    } catch (Exception e) {
                        log.warn("Failed to offline DS workflow definition, Code: {}. Continuing with deletion attempt.", processCode, e);
                    }
                    try {
                        log.info("Attempting to delete DS workflow definition, Code: {}", processCode);
                        dolphinClient.opsForProcess().delete(dolphinProperties.getProjectCode(), processCode);
                        log.info("Successfully deleted DS workflow definition, Code: {}", processCode);
                    } catch (Exception e) {
                        log.error("Failed to delete DS workflow definition, Code: {}. Database record for template ID {} might still be deleted.", processCode, id, e);
                    }
                } else {
                    log.warn("Template ID {} has no associated processCode, skipping DS deletion.", id);
                }

                // 3. 删除数据库中的模板记录
                log.info("Deleting task template record from database, ID: {}", id);
                baseMapper.deleteById(id);
            });
        }
        return true;
    }


    /**
     * 根据 ID 获取 ProcessCode
     */
    public Long getProcessCodeById(Long id) {
        TaskTemplate task = baseMapper.selectById(id); // MyBatis Plus 默认提供了 selectById
        return Optional.ofNullable(task).map(TaskTemplate::getProcessCode).orElse(null);
    }

    /**
     * [重点修改] 根据 sourceQueryFilter 和 Sink/Source 类型，动态生成结构化的启动参数描述符
     * @return List<ParameterDescriptor>
     */
    private List<ParameterDescriptor> generateStartParams(List<QueryFilterTemplate> sourceQueryFilter, TaskTemplate taskTemplate) {
        List<ParameterDescriptor> descriptors = new ArrayList<>();
        if (sourceQueryFilter == null) {
            sourceQueryFilter = Collections.emptyList(); // 确保不为null
        }

        // 1. 处理模板中定义的动态过滤条件
        sourceQueryFilter.forEach(filter -> {
            String key = filter.getValue().toLowerCase();
            String columnType = filter.getColumnType();
            String filterType = String.valueOf(filter.getType());

            ParameterDescriptor descriptor = new ParameterDescriptor()
                    .setName(key)
                    .setLabel(key)
                    .setRequired(true)
                    .setDataType(columnType); // 设置数据类型

            // 根据操作类型和列类型决定UI组件
            if ("IN".equals(filterType) || "NOT_IN".equals(filterType)) {
                descriptor.setComponent("list-input")
                        .setDefaultValue(Collections.emptyList())
                        .setProps(Collections.singletonMap("placeholder", "输入值后按回车，或粘贴逗号分隔的文本"));
            } else if ("BOOLEAN".equalsIgnoreCase(columnType) || "bool".equalsIgnoreCase(columnType)) {
                descriptor.setComponent("select")
                        .setDefaultValue(null)
                        .setProps(new HashMap<String, Object>() {{
                            put("placeholder", "请选择");
                            put("options", Arrays.asList(
                                    new HashMap<String, Object>() {{ put("label", "TRUE"); put("value", true); }},
                                    new HashMap<String, Object>() {{ put("label", "FALSE"); put("value", false); }}
                            ));
                        }});
            } else if ("TIMESTAMP".equalsIgnoreCase(columnType) || "DATE".equalsIgnoreCase(columnType)) {
                descriptor.setComponent("date-picker")
                        .setDefaultValue(null)
                        .setProps(Collections.singletonMap("showTime", true));
            } else if ("IS_NULL".equalsIgnoreCase(filterType) || "IS_NOT_NULL".equalsIgnoreCase(filterType)) {
                return; // 跳过当前循环
            } else {
                descriptor.setComponent("input")
                        .setDefaultValue("")
                        .setProps(Collections.singletonMap("placeholder", "请输入" + key));
            }
            descriptors.add(descriptor);
        });

        // 2. 固定添加应用层优先级 (priorityLevel)
        descriptors.add(new ParameterDescriptor()
                .setName("priorityLevel")
                .setLabel("应用层优先级")
                .setComponent("select")
                .setRequired(false) // 非必填
                .setDefaultValue(taskTemplate.getDefaultPriorityLevel()) // 使用模板的默认值
                .setProps(new HashMap<String, Object>() {{
                    put("placeholder", "可选，默认为 " + taskTemplate.getDefaultPriorityLevel());
                    put("options", Arrays.asList(
                            new HashMap<String, Object>() {{ put("label", "1 (最高)"); put("value", 1); }},
                            new HashMap<String, Object>() {{ put("label", "2"); put("value", 2); }},
                            new HashMap<String, Object>() {{ put("label", "3"); put("value", 3); }},
                            new HashMap<String, Object>() {{ put("label", "4"); put("value", 4); }},
                            new HashMap<String, Object>() {{ put("label", "5 (普通)"); put("value", 5); }},
                            new HashMap<String, Object>() {{ put("label", "6"); put("value", 6); }},
                            new HashMap<String, Object>() {{ put("label", "7"); put("value", 7); }},
                            new HashMap<String, Object>() {{ put("label", "8"); put("value", 8); }},
                            new HashMap<String, Object>() {{ put("label", "9"); put("value", 9); }},
                            new HashMap<String, Object>() {{ put("label", "10 (最低)"); put("value", 10); }}
                    ));
                }})
        );

        // 3. 固定添加 DolphinScheduler 优先级 (priority)
        descriptors.add(new ParameterDescriptor()
                .setName("priority")
                .setLabel("DS优先级")
                .setComponent("select")
                .setRequired(false) // 非必填
                .setDefaultValue(taskTemplate.getDefaultDsPriority()) // 使用模板的默认值
                .setProps(new HashMap<String, Object>() {{
                    put("placeholder", "可选，默认为 " + taskTemplate.getDefaultDsPriority());
                    // 【关键修改】使用枚举动态生成选项列表
                    put("options", Arrays.stream(DsPriority.values())
                            .map(p -> new HashMap<String, Object>() {{
                                put("label", p.getLabel()); // 使用枚举中定义的中文标签
                                put("value", p.getValue()); // 使用枚举中定义的值
                            }})
                            .collect(Collectors.toList()));
                }})
        );

        // 4. 【关键重构】动态地从 Source 和 Sink Builder 获取专属参数

        // 从 Source 端获取专属参数 (为未来扩展预留)
        if (taskTemplate != null && StringUtils.hasText(taskTemplate.getSourceDataKey())) {
            DataSourceModel sourceDataSourceModel = dataSourceService.selectByDataSourceKey(taskTemplate.getSourceDataKey());
            if (sourceDataSourceModel != null) {
                SourceBuilder sourceBuilder = sourceBuilderMap.get(sourceDataSourceModel.getDbType());
                if (sourceBuilder != null) {
                    // 假设SourceBuilder未来也增加了getSpecificParameterDescriptors方法
                    // List<ParameterDescriptor> specificSourceParams = sourceBuilder.getSpecificParameterDescriptors();
                    // if (!specificSourceParams.isEmpty()) {
                    //    descriptors.addAll(specificSourceParams);
                    //    log.info("为 {} 类型的Source添加了 {} 个专属参数。", sourceDataSourceModel.getDbType(), specificSourceParams.size());
                    // }
                }
            }
        }

        // 从 Sink 端获取专属参数
        if (taskTemplate != null && StringUtils.hasText(taskTemplate.getSinkDataKey())) {
            DataSourceModel sinkDataSourceModel = dataSourceService.selectByDataSourceKey(taskTemplate.getSinkDataKey());
            if (sinkDataSourceModel != null) {
                SinkBuilder sinkBuilder = sinkBuilderMap.get(sinkDataSourceModel.getDbType());
                if (sinkBuilder != null) {
                    // 调用接口新方法，获取并添加参数
                    List<ParameterDescriptor> specificSinkParams = sinkBuilder.getSpecificParameterDescriptors();
                    if (!specificSinkParams.isEmpty()) {
                        descriptors.addAll(specificSinkParams);
                        log.info("为 {} 类型的Sink添加了 {} 个专属参数。",
                                sinkDataSourceModel.getDbType(),
                                specificSinkParams.size());
                    }
                }
            }
        }

        return descriptors;
    }



    /**
     * 生成 SeaTunnel 任务脚本
     */
    private String generateTaskScript(TaskTemplateBo taskBo) {
        DataSourceModel sourceDataSourceModel = dataSourceService.selectByDataSourceKey(taskBo.getSourceDataKey());
        DataSourceModel sinkDataSourceModel = dataSourceService.selectByDataSourceKey(taskBo.getSinkDataKey());
        if (sourceDataSourceModel == null || sinkDataSourceModel == null) {
            throw new TaskException(TaskErrorCode.METADATA_IS_NOT_EXISTED, "Data source " + (sourceDataSourceModel == null ? taskBo.getSourceDataKey() : taskBo.getSinkDataKey()));
        }

        TableInfo sourceTableInfo = tableMetaDataService.tableInfo(taskBo.getSourceDataKey(), taskBo.getSourceTableName());
        if (sourceTableInfo == null) {
            throw new TaskException(TaskErrorCode.METADATA_IS_NOT_EXISTED, "Source table " + taskBo.getSourceTableName());
        }

        DataSourceType sourceDataSourceType = sourceDataSourceModel.getDbType();
        DataSourceType sinkDataSourceType = sinkDataSourceModel.getDbType();
        SourceBuilder sourceBuilder = sourceBuilderMap.get(sourceDataSourceType);
        SinkBuilder sinkBuilder = sinkBuilderMap.get(sinkDataSourceType);

        if (sourceBuilder == null || sinkBuilder == null) {
            throw new TaskException(TaskErrorCode.UNKNOWN_ERROR, "No builder found for source/sink type");
        }

        // 初始化 Velocity 引擎
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        // 获取模板
        String templatePath = String.format("templates/%s2%s.vm", sourceDataSourceType.getId(), sinkDataSourceType.getId());
        Template template = ve.getTemplate(templatePath);

        // 创建上下文
        VelocityContext context = new VelocityContext();

        // 填充 Common 部分
        CommonTemplate commonTemplate = new CommonTemplate();
        commonTemplate.setParallelism(parallelism);
        commonTemplate.setMode(mode);
        commonTemplate.setInterval(checkpointInterval);
        commonTemplate.setQueue(flinkYarnQueue);
        Commonbuilder commonbuilder = new Commonbuilder();
        commonbuilder.buildContext(context, commonTemplate);

        // 填充 Source 部分
        SourceTemplate sourceTemplate = new SourceTemplate();
        sourceTemplate.setDataSourceModel(sourceDataSourceModel);
        sourceTemplate.setTableInfo(sourceTableInfo);
        sourceTemplate.setSourceQueryFilter(taskBo.getSourceQueryFilter());
        sourceTemplate.setPartitionNum(partitionNum); // 设置分区数
        sourceBuilder.buildContext(context, sourceTemplate);

        // 填充 Sink 部分
        SinkTemplate sinkTemplate = new SinkTemplate();
        sinkTemplate.setDataSourceModel(sinkDataSourceModel);
        sinkTemplate.setTableName(taskBo.getSinkTableName());
        sinkBuilder.buildContext(context, sinkTemplate);

        // 填充 Transform 部分 (字段映射)
        FieldMapperTransformTemplate fieldMapperTransformTemplate = new FieldMapperTransformTemplate();
        fieldMapperTransformTemplate.setSinkTableName(taskBo.getSinkTableName());
        // 注意：源表名需要从 sourceBuilder 构建的 context 中获取，因为它可能包含别名
        String sourceTableNameInContext = (String) context.get("sourceTableNameAlias"); // 假设 sourceBuilder 会放入别名
        if (sourceTableNameInContext == null) sourceTableNameInContext = taskBo.getSourceTableName(); // 回退
        fieldMapperTransformTemplate.setSourceTableName(sourceTableNameInContext);
        // 键添加 _tmp 后缀
        fieldMapperTransformTemplate.setFieldMapping(appendTmpToKeys(taskBo.getFieldMapping()));
        FieldMapperBuilder fieldMapperBuilder = new FieldMapperBuilder();
        fieldMapperBuilder.buildContext(context, fieldMapperTransformTemplate);


        // 合并模板和上下文
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    /**
     * 生成请求示例 (用于前端展示)
     */

    public String generateRequestExample(Long id, List<ParameterDescriptor> startParams) {
        if (id == null) {
            log.warn("Cannot generate request example without template ID.");
            return "Error: Template ID is missing.";
        }

        HashMap<String, Object> exampleParams = new LinkedHashMap<>();
        if (startParams != null) {
            startParams.forEach(param -> {
                String key = param.getName();
                Object exampleValue;
                switch (param.getComponent()) {
                    case "list-input":
                        // [修正] 根据 dataType 生成不同类型的数组
                        String dataType = param.getDataType() != null ? param.getDataType().toUpperCase() : "";
                        if (dataType.contains("INT") || dataType.contains("BIGINT") || dataType.contains("NUMERIC") || dataType.contains("DECIMAL")) {
                            exampleValue = Arrays.asList(1001, 1002); // 生成数字数组示例
                        } else {
                            exampleValue = Arrays.asList("value1", "value2"); // 默认生成字符串数组示例
                        }
                        break;
                    case "select":
                        // [修正] 优先用默认值，否则用第一个选项，避免回退到 true
                        if (param.getDefaultValue() != null && !"".equals(param.getDefaultValue())) {
                            exampleValue = param.getDefaultValue();
                        } else {
                            Map<String, Object> props = param.getProps();
                            if (props != null && props.get("options") instanceof List) {
                                @SuppressWarnings("unchecked")
                                List<Map<String, Object>> options = (List<Map<String, Object>>) props.get("options");
                                if (!options.isEmpty()) {
                                    exampleValue = options.get(0).get("value"); // 使用第一个选项的值作为示例
                                } else {
                                    exampleValue = "<请选择一个选项>"; // 安全回退
                                }
                            } else {
                                exampleValue = "<请选择一个选项>"; // 安全回退
                            }
                        }
                        break;
                    case "date-picker":
                        exampleValue = "YYYY-MM-DD HH:mm:ss 或 毫秒时间戳";
                        break;
                    default:
                        exampleValue = "<" + param.getLabel() + ">";
                }
                exampleParams.put(key, exampleValue);
            });
        }

        // Velocity 模板引擎部分保持不变
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template template = ve.getTemplate("templates/request_template.vm");

        VelocityContext context = new VelocityContext();
        context.put("host", host);
        context.put("port", port);
        context.put("contextPath", contextPath);
        context.put("id", id);

        String jsonParams = "{}";
        if (!exampleParams.isEmpty()) {
            try {
                // 使用 Jackson 将参数 Map 美化成 JSON 字符串
                jsonParams = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(exampleParams);
            } catch (JsonProcessingException e) {
                log.error("Error formatting example params to JSON", e);
                jsonParams = "{\n  \"error\": \"Could not format parameters\"\n}";
            }
        }
        context.put("taskParams", jsonParams);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    /**
     * 将 Map 的键添加 "_TMP" 后缀 (保持原有顺序)
     */
    public  <V> LinkedHashMap<String, V> appendTmpToKeys(LinkedHashMap<String, V> originalMap) {
        if (originalMap == null) {
            return new LinkedHashMap<>(); // 处理空输入
        }
        return originalMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey() + "_TMP", // 键添加后缀
                        Map.Entry::getValue,              // 值保持不变
                        (oldVal, newVal) -> oldVal,       // 合并函数（理论上不会有冲突）
                        LinkedHashMap::new                // 保持顺序
                ));
    }

    /**
     * 创建 SeaTunnel 任务对象
     */
    private SeaTunnelTask createSeaTunnelTask(String configScript) {
        SeaTunnelTask seatunnelTask = new SeaTunnelTask();
        seatunnelTask.setUseCustom(true); // 使用自定义脚本
        seatunnelTask.setRawScript(configScript); // 设置生成的脚本内容

        if ("Flink".equalsIgnoreCase(engine)) {
            // Flink V2 Connector (Flink 1.15+) 的基础启动脚本
            final String baseScript = "start-seatunnel-flink-15-connector-v2.sh";

            // 根据不同的 Flink 执行模式进行配置
            if ("yarn-cluster".equalsIgnoreCase(flinkExecutionMode)) {
                // Flink on YARN 模式
                log.info("Configuring SeaTunnel task for Flink on YARN mode.");
                // 拼接启动参数


                seatunnelTask.setStartupScript(baseScript);
                seatunnelTask.setOthers(String.format("-m yarn-cluster "));
                // 对于自定义脚本参数的模式，DS 的 runMode 和 deployMode 应设为 null
                seatunnelTask.setRunMode("none");
                seatunnelTask.setDeployMode(null);

            } else {
                // 默认为 Flink Standalone 模式 (run-application)
                log.info("Configuring SeaTunnel task for Flink Standalone (run-application) mode.");
                seatunnelTask.setStartupScript(baseScript);
                seatunnelTask.setOthers(String.format("--name ${subtask_name}"));
                seatunnelTask.setRunMode("run-application");
                seatunnelTask.setDeployMode(null);
            }

        } else {
            // 默认或配置为 SeaTunnel (Spark引擎)
            log.info("Configuring SeaTunnel task for Spark engine.");
            seatunnelTask.setStartupScript("seatunnel.sh");
            seatunnelTask.setDeployMode("client");
            seatunnelTask.setRunMode(null);
        }
        // localParams 和 resourceList 通常为空，除非需要传递额外本地参数或资源文件
        seatunnelTask.setLocalParams(Collections.emptyList());
        seatunnelTask.setResourceList(Collections.emptyList());

        return seatunnelTask;
    }

    /**
     * 创建 DolphinScheduler 工作流定义参数对象
     */
    private ProcessDefineParam createProcessDefineParam(TaskTemplateBo bo, Long taskCode, List<TaskDefinition> taskDefinitions) {
        ProcessDefineParam pcr = new ProcessDefineParam();
        pcr.setName(bo.getName()) // 工作流名称
                .setLocations(TaskLocationUtils.verticalLocation(taskCode)) // 任务节点位置（垂直布局）
                .setDescription(bo.getDescription()) // 描述
                .setTenantCode(dolphinProperties.getTenantCode()) // 租户代码
                .setTimeout("0") // 超时时间（0表示不设置）
                .setExecutionType(ProcessDefineParam.EXECUTION_TYPE_PARALLEL) // 执行类型（并行）
                .setTaskDefinitionJson(taskDefinitions) // 任务定义列表
                .setTaskRelationJson(TaskRelationUtils.oneLineRelation(taskCode)) // 任务关系（单节点）
                .setGlobalParams(null); // 全局参数（这里没用到）
        return pcr;
    }
    private void addMaxComputeParams(List<ParameterDescriptor> descriptors) {
        descriptors.add(new ParameterDescriptor()
                .setName(TaskParamsConstant.MaxCompute.PARTITION_SPEC.toLowerCase())
                .setLabel("分区参数(MaxCompute)")
                .setComponent("input")
                .setRequired(false)
                .setDefaultValue("")
                .setProps(Collections.singletonMap("placeholder", "例如: pt=1,ds=20250101"))
        );
        descriptors.add(new ParameterDescriptor()
                .setName(TaskParamsConstant.MaxCompute.OVERWRITE.toLowerCase())
                .setLabel("是否覆盖(MaxCompute)")
                .setComponent("select")
                .setRequired(false)
                .setDefaultValue(null)
                .setProps(new HashMap<String, Object>() {{
                    put("placeholder", "请选择（默认为否）");
                    put("options", Arrays.asList(
                            new HashMap<String, Object>() {{ put("label", "是 (覆盖)"); put("value", true); }},
                            new HashMap<String, Object>() {{ put("label", "否 (追加)"); put("value", false); }}
                    ));
                }})
        );
    }
    /**
     *
     * @param currentFormParams 前端表单当前的所有值
     * @return 格式化后的 JSON 请求示例字符串
     */
    /**
     *
     * @param currentFormParams 前端表单当前的所有值
     * @return 格式化后的 JSON 请求示例字符串
     */
    public String generatePreviewRequestExample(HashMap<String, Object> currentFormParams) {
        final ObjectMapper mapper = new ObjectMapper();
        TaskTemplateBo bo;
        try {
            // 这行代码将前端的实时表单数据（包括用户可能刚刚在UI上选择的优先级）转换为 TaskTemplateBo 对象
            bo = mapper.convertValue(currentFormParams, TaskTemplateBo.class);
        } catch (IllegalArgumentException e) {
            log.error("Failed to convert form params to TaskTemplateBo.", e);
            return String.format("{\n  \"error\": \"无法生成预览，参数格式错误: %s\"\n}", e.getMessage());
        }

        // 检查关键信息是否存在 (此部分逻辑不变)
        if (!StringUtils.hasText(bo.getSourceDataKey()) || !StringUtils.hasText(bo.getSourceTableName()) ||
                !StringUtils.hasText(bo.getSinkDataKey()) || !StringUtils.hasText(bo.getSinkTableName())) {
            return "{\n  \"error\": \"无法生成预览，请确保源/目标数据源和表已选择。\"\n}";
        }

        try {
            // ======================= 修改开始 =======================

            // 1. 从 BO 创建一个临时的 TaskTemplate 实体，以传递给 generateStartParams
            //    这样 generateStartParams 就能获取到 defaultDsPriority 等默认值
            TaskTemplate tempTemplate = new TaskTemplate();
            // 使用 Spring 的 BeanUtils 或手动设置字段
            BeanUtils.copyProperties(bo, tempTemplate);

            // 2. 【复用】根据实时的过滤条件，动态生成启动参数的结构
            List<QueryFilterTemplate> queryFilters = bo.getSourceQueryFilter() != null ?
                    bo.getSourceQueryFilter() :
                    Collections.emptyList();

            // 3. 【关键修改】调用修改后的 generateStartParams 方法，传入临时的 template 对象
            //    现在，即使是预览，也能正确地将优先级参数及其默认值包含进来了
            List<ParameterDescriptor> startParams = generateStartParams(queryFilters, tempTemplate);

            // ======================= 修改结束 =======================

            // 4. 【复用】调用现有的、为已保存模板生成示例的逻辑 (此部分逻辑不变)
            Long previewId = bo.getId() != null ? bo.getId() : 0L;
            String example = generateRequestExample(previewId, startParams);

            // 5. 返回真正生成的请求示例 (此部分逻辑不变)
            return example;

        } catch (Exception e) {
            log.error("Error generating preview request example from form data.", e);
            return String.format("{\n  \"error\": \"无法生成预览, 请检查配置。\",\n  \"detail\": \"%s\"\n}", e.getMessage());
        }
    }

}