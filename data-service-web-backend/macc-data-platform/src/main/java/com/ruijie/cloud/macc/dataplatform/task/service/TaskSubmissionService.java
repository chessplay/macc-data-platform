package com.ruijie.cloud.macc.dataplatform.task.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.cloud.dc.core.domain.PageQuery;
import com.ruijie.cloud.dc.core.page.TableDataInfo;
import com.ruijie.cloud.macc.dataplatform.task.config.DolphinProperties;
import com.ruijie.cloud.macc.dataplatform.task.config.YarnAppIdCompensationProperties;
import com.ruijie.cloud.macc.dataplatform.task.constant.TaskParamsConstant;
import com.ruijie.cloud.macc.dataplatform.task.domain.DsProcessInstance;
// [改动点] 引入重构后的实体类
import com.ruijie.cloud.macc.dataplatform.task.domain.SubtaskInstance;
import com.ruijie.cloud.macc.dataplatform.task.domain.TaskInstance;
import com.ruijie.cloud.macc.dataplatform.task.domain.TaskTemplate;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.SubTaskDetailVo;
import com.ruijie.cloud.macc.dataplatform.task.ds.core.DolphinClient;
import com.ruijie.cloud.macc.dataplatform.task.ds.core.DolphinException;
import com.ruijie.cloud.macc.dataplatform.task.ds.instance.ProcessInstanceCreateParam;
import com.ruijie.cloud.macc.dataplatform.task.ds.instance.ProcessInstanceLogResp;
import com.ruijie.cloud.macc.dataplatform.task.ds.instance.ProcessInstanceQueryResp;
import com.ruijie.cloud.macc.dataplatform.task.ds.instance.ProcessInstanceTasksQueryResp;
import com.ruijie.cloud.macc.dataplatform.task.entity.SubTaskQueryDto;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskErrorCode;
import com.ruijie.cloud.macc.dataplatform.task.exception.TaskException;
// [改动点] 引入重构后的 Mapper
import com.ruijie.cloud.macc.dataplatform.task.mapper.SubtaskInstanceMapper;
import com.ruijie.cloud.macc.dataplatform.task.mapper.TaskInstanceMapper;
import com.ruijie.cloud.macc.dataplatform.task.mapper.TaskTemplateMapper;
import com.ruijie.cloud.macc.dataplatform.task.mapper.ds.DsProcessInstanceMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class TaskSubmissionService {

    private final DolphinClient dolphinClient;
    private final DolphinProperties dolphinProperties;
    private final TaskTemplateMapper taskTemplateMapper;
    private final SubtaskInstanceMapper subtaskInstanceMapper;
    private final TaskInstanceMapper taskInstanceMapper;
    private final DsProcessInstanceMapper dsProcessInstanceMapper;
    private final YarnAppIdCompensationProperties yarnAppIdCompensationProps;
    private final RestTemplate restTemplate;

    @Autowired
    @Lazy
    private TaskInstanceService taskInstanceService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Value("${yarn.resourcemanager.rest.url:http://spark-master002.dc:9276}")
    private String yarnRmBaseUrl;
    @Value("${yarn.resourcemanager.webapp.url-template:http://spark-master002.dc:9276/cluster/app/{appId}}")
    private String yarnLogUrlTemplate;




    @Value("${platform.task.max-concurrency:30}")
    private int maxConcurrency;
    @Value("${platform.task.max-retries:3}")
    private int maxRetries;
    @Value("${platform.task.retry-delay-ms:30000}")
    private long retryDelayMillis;
    @Value("${platform.task.submission-batch-size:3}")
    private int submissionBatchSize;
    @Value("${platform.task.submission-interval-ms:20000}")
    private long submissionIntervalMillis;

    private final AtomicInteger runningTaskCount = new AtomicInteger(0);
    private final BlockingQueue<TaskSubmissionRequest> taskQueue = new PriorityBlockingQueue<>();
    private final ExecutorService submissionExecutor = Executors.newFixedThreadPool(15);
    private final ExecutorService logParsingExecutor = Executors.newFixedThreadPool(15);
    private final ScheduledExecutorService statusCheckScheduler = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService batchSubmissionScheduler = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService retryScheduler = Executors.newSingleThreadScheduledExecutor();



    @Data
    @AllArgsConstructor
    public static class TaskSubmissionRequest implements Comparable<TaskSubmissionRequest> {
        private long templateId;
        private Map<String, Object> originalTaskParams;
        private String correlationId;
        private String taskInstanceId;
        private SubtaskInstance subtaskRecord;
        private int priority;

        public static final int DEFAULT_PRIORITY_LEVEL = 10;
        public static final String DEFAULT_DS_PRIORITY = "MEDIUM";
        public static final String PRIORITY_LEVEL_PARAM_KEY = "priorityLevel";
        public static final String DS_PRIORITY_PARAM_KEY = "priority";
        public static final Set<String> VALID_DS_PRIORITIES_SET =
                Collections.unmodifiableSet(new HashSet<>(Arrays.asList("HIGHEST", "HIGH", "MEDIUM", "LOW", "LOWEST")));


        @Override
        public int compareTo(TaskSubmissionRequest other) {
            return Integer.compare(this.priority, other.priority);
        }

        public static int parsePriority(Map<String, Object> params, TaskTemplate template) {
            if (params != null && params.containsKey(PRIORITY_LEVEL_PARAM_KEY)) {
                Object priorityValue = params.get(PRIORITY_LEVEL_PARAM_KEY);
                if (priorityValue instanceof Number) {
                    return ((Number) priorityValue).intValue();
                }
                try {
                    return Integer.parseInt(priorityValue.toString());
                } catch (NumberFormatException e) {
                    log.warn("Invalid priorityLevel value '{}' in params, falling back to template or default.", priorityValue);
                }
            }
            return Optional.ofNullable(template)
                    .map(TaskTemplate::getDefaultPriorityLevel)
                    .orElse(DEFAULT_PRIORITY_LEVEL);
        }

        public static String parseDsPriority(Map<String, Object> params, TaskTemplate template) {
            String priorityStr = null;
            if (params != null && params.containsKey(DS_PRIORITY_PARAM_KEY)) {
                Object priorityValue = params.get(DS_PRIORITY_PARAM_KEY);
                if (priorityValue instanceof String) {
                    priorityStr = ((String) priorityValue).toUpperCase();
                }
            }

            if (priorityStr != null && VALID_DS_PRIORITIES_SET.contains(priorityStr)) {
                log.debug("Using DS priority from params: {}", priorityStr);
                return priorityStr;
            } else if (priorityStr != null) {
                log.warn("Invalid DS priority value '{}' in params, falling back to template or default.", priorityStr);
            }

            priorityStr = Optional.ofNullable(template)
                    .map(TaskTemplate::getDefaultDsPriority)
                    .map(String::toUpperCase)
                    .filter(VALID_DS_PRIORITIES_SET::contains)
                    .orElse(DEFAULT_DS_PRIORITY);
            log.debug("Using DS priority from template or default: {}", priorityStr);
            return priorityStr;
        }
    }

    @PostConstruct
    public void initialize() {
        log.info("Initializing TaskSubmissionService (Hybrid Priority Queue Mode) with maxConcurrency: {}, batchSize: {}, intervalMs: {}, maxRetries: {}, retryDelayMs: {}",
                maxConcurrency, submissionBatchSize, submissionIntervalMillis, maxRetries, retryDelayMillis);
        loadAndResumeUnfinishedTasks();
        if (taskInstanceService != null) {
            taskInstanceService.recoverInFlightInstances();
        } else {
            log.error("TaskInstanceService is null during initialization. Recovery of parent instances will be skipped.");
        }
        statusCheckScheduler.scheduleWithFixedDelay(this::checkAndUpdateTaskStatus, 15, 1, TimeUnit.SECONDS);
        batchSubmissionScheduler.scheduleWithFixedDelay(this::processQueueInBatches, 5, submissionIntervalMillis, TimeUnit.MILLISECONDS);
        log.info("TaskSubmissionService initialized.");
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down TaskSubmissionService...");
        shutdownExecutorService(submissionExecutor, "SubmissionExecutor");
        shutdownExecutorService(statusCheckScheduler, "StatusCheckScheduler");
        shutdownExecutorService(batchSubmissionScheduler, "BatchSubmissionScheduler");
        shutdownExecutorService(retryScheduler, "RetryScheduler");
        shutdownExecutorService(logParsingExecutor, "LogParsingExecutor");
        log.info("TaskSubmissionService shut down complete.");
    }

    private void shutdownExecutorService(ExecutorService executor, String name) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS))
                    log.error("{} did not terminate.", name);
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    @Scheduled(cron = "${platform.task.yarn-app-id.compensation-job.cron}")
    public void scheduledYarnAppIdCompensation() {
        if (!yarnAppIdCompensationProps.isEnabled()) {
            log.trace("YARN App ID 补偿任务已被禁用。");
            return;
        }
        log.info("开始执行YARN App ID补偿定时任务...");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkFrom = now.minusHours(yarnAppIdCompensationProps.getMaxAgeHoursForCheck());
        LocalDateTime checkUpTo = now.minusMinutes(yarnAppIdCompensationProps.getInitialDelayMinutes());

        QueryWrapper<SubtaskInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("submission_status", SubtaskInstance.STATUS_TRACKING)
                .isNull("yarn_application_id")
                .isNotNull("ds_instance_id")
                .ge("create_time", Date.from(checkFrom.atZone(ZoneId.systemDefault()).toInstant()))
                .le("create_time", Date.from(checkUpTo.atZone(ZoneId.systemDefault()).toInstant()));

        List<SubtaskInstance> tasksToCompensate;
        try {
            tasksToCompensate = subtaskInstanceMapper.selectList(queryWrapper);
        } catch (Exception e) {
            log.error("查询需要补偿YARN App ID的任务时出错: {}", e.getMessage(), e);
            return;
        }

        if (tasksToCompensate.isEmpty()) {
            log.info("本次运行未发现需要补偿YARN App ID的任务。");
            return;
        }

        log.info("发现 {} 个任务需要补偿YARN App ID，开始处理...", tasksToCompensate.size());

        for (SubtaskInstance subtask : tasksToCompensate) {
            logParsingExecutor.submit(() -> {
                log.info("正在为子任务 (DS Instance ID: {}, CorrelationId: {}) 补偿获取YARN App ID",
                        subtask.getDsInstanceId(), subtask.getCorrelationId());

                fetchAndStoreYarnAppIdLogic(
                        subtask.getId(),
                        subtask.getDsInstanceId(),
                        subtask.getCorrelationId(),
                        yarnAppIdCompensationProps.getLogFetchAttempts(),
                        yarnAppIdCompensationProps.getLogFetchDelayMs(),
                        "[YarnAppIdCompensation]"
                );
            });
        }
        log.info("YARN App ID 补偿任务派发完成。");
    }


    @Transactional
    public boolean submitTasks(long templateId, List<Map<String, Object>> subTaskParamsList, String taskInstanceId, String taskInstanceName) {
        log.info("接收到 {} 个子任务, 模板ID: {}, 所属任务实例ID: {}", subTaskParamsList.size(), templateId, taskInstanceId);

        TaskTemplate taskTemplate = taskTemplateMapper.selectById(templateId);
        if (taskTemplate == null) {
            log.error("无法提交任务：找不到ID为 {} 的任务模板。", templateId);
            return false;
        }

        for (int i = 0; i < subTaskParamsList.size(); i++) {
            Map<String, Object> originalParams = subTaskParamsList.get(i);
            String correlationId = UUID.randomUUID().toString();

            String subtaskName = String.format("%s-%d", taskInstanceName, i + 1);

            int priority = TaskSubmissionRequest.parsePriority(originalParams, taskTemplate);

            SubtaskInstance subtaskRecord = new SubtaskInstance()
                    .setCorrelationId(correlationId)
                    .setTaskInstanceId(taskInstanceId)
                    .setTemplateId(templateId)
                    .setName(subtaskName)
                    .setRequestParamsJson(safeToJson(originalParams))
                    .setSubmissionStatus(SubtaskInstance.STATUS_QUEUED)
                    .setRetryCount(0);

            subtaskInstanceMapper.insert(subtaskRecord);

            TaskSubmissionRequest request = new TaskSubmissionRequest(
                    templateId,
                    originalParams,
                    correlationId,
                    taskInstanceId,
                    subtaskRecord,
                    priority
            );

            enqueueTask(request);
        }

        return true;
    }



    public void enqueueTask(TaskSubmissionRequest request) {
        SubtaskInstance subtaskRecord = request.getSubtaskRecord();
        try {
            if (!SubtaskInstance.STATUS_RETRY_SCHEDULED.equals(subtaskRecord.getSubmissionStatus())
                    && subtaskRecord.getId() != null
                    && !SubtaskInstance.STATUS_QUEUED.equals(subtaskRecord.getSubmissionStatus()))
            {
                subtaskRecord.setSubmissionStatus(SubtaskInstance.STATUS_QUEUED);
                updateSubtaskRecord(subtaskRecord);
                log.info("Updated status to QUEUED for correlationId: {}", request.getCorrelationId());
            }

            taskQueue.put(request);
            log.info("Task enqueued (Priority: {}). CorrelationId: {}. Queue size: {}",
                    request.getPriority(), request.getCorrelationId(), taskQueue.size());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to queue task due to interruption. CorrelationId: {}", request.getCorrelationId(), e);
            updateLogError(request.getSubtaskRecord(), SubtaskInstance.STATUS_SUBMIT_FAILED_API, "Failed to queue: Interrupted", null);
        } catch (Exception e) {
            log.error("Failed to queue task. CorrelationId: {}", request.getCorrelationId(), e);
            updateLogError(request.getSubtaskRecord(), SubtaskInstance.STATUS_SUBMIT_FAILED_API, "Failed to queue: " + e.getMessage(), null);
        }
    }

    private void processQueueInBatches() {
        log.debug("Checking task queue for batch submission...");
        int availableSlots = maxConcurrency - runningTaskCount.get();

        if (availableSlots <= 0) {
            log.debug("No available concurrency slots ({}/{}), skipping queue processing.",
                    runningTaskCount.get(), maxConcurrency);
            return;
        }

        if (taskQueue.isEmpty()) {
            log.debug("Task queue is empty.");
            return;
        }

        int tasksToSubmitCount = Math.min(availableSlots, submissionBatchSize);

        if (tasksToSubmitCount <= 0) {
            log.debug("Calculated tasksToSubmitCount is 0.");
            return;
        }

        log.info("Attempting to submit batch of up to {} tasks. Available slots: {}, Queue size: {}",
                tasksToSubmitCount, availableSlots, taskQueue.size());

        List<TaskSubmissionRequest> batchToSubmit = new ArrayList<>(tasksToSubmitCount);
        int polledCount = 0;
        while (polledCount < tasksToSubmitCount) {
            if(runningTaskCount.get() >= maxConcurrency){
                log.debug("Concurrency limit reached during batch polling ({}/{}), stopping polling.",
                        runningTaskCount.get(), maxConcurrency);
                break;
            }

            TaskSubmissionRequest request = taskQueue.poll();
            if (request == null) {
                log.debug("Queue became empty while polling for batch.");
                break;
            }

            if (runningTaskCount.incrementAndGet() > maxConcurrency) {
                runningTaskCount.decrementAndGet();
                log.warn("Concurrency limit reached during polling for batch (race condition), requeuing task {}.", request.getCorrelationId());
                try {
                    taskQueue.put(request);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Interrupted while requeuing task {}", request.getCorrelationId(), e);
                    updateLogError(request.getSubtaskRecord(), SubtaskInstance.STATUS_SUBMIT_FAILED_API, "Failed to requeue after concurrency race: Interrupted", null);
                } catch (Exception e) {
                    log.error("Exception while requeuing task {}", request.getCorrelationId(), e);
                    updateLogError(request.getSubtaskRecord(), SubtaskInstance.STATUS_SUBMIT_FAILED_API, "Failed to requeue after concurrency race: " + e.getMessage(), null);
                }
                break;
            }

            batchToSubmit.add(request);
            polledCount++;
            log.debug("Polled task {} (Priority: {}) for batch. Incremented count to {}", request.getCorrelationId(), request.getPriority(), runningTaskCount.get());
        }


        if (!batchToSubmit.isEmpty()) {
            log.info("Polled {} tasks for submission (highest priority first). Current running count: {}",
                    batchToSubmit.size(), runningTaskCount.get());
            for (TaskSubmissionRequest request : batchToSubmit) {
                submitToDolphinSchedulerAsync(request);
            }
            log.info("Submitted batch of {} tasks asynchronously.", batchToSubmit.size());
        } else {
            log.debug("No tasks polled for submission in this cycle (Queue Size: {}).", taskQueue.size());
        }
    }

    private void submitToDolphinSchedulerAsync(TaskSubmissionRequest request) {
        final SubtaskInstance subtaskRecord = request.getSubtaskRecord();
        final String correlationId = request.getCorrelationId();
        final Map<String, Object> originalParams = request.getOriginalTaskParams();

        subtaskRecord.setSubmissionStatus(SubtaskInstance.STATUS_SUBMITTING);
        updateSubtaskRecord(subtaskRecord);
        log.info("Set status to SUBMITTING for correlationId: {} (Priority: {})", correlationId, request.getPriority());

        submissionExecutor.submit(() -> {
            Integer processInstanceId = null;
            boolean submissionApiSuccess = false;
            String errorMessage = null;
            String finalSubmissionStatus = SubtaskInstance.STATUS_SUBMITTING;
            boolean countDecrementedLocally = false;
            TaskTemplate taskTemplate = null;

            try {
                Map<String, Object> paramsToFormat = new HashMap<>(originalParams);
                paramsToFormat.putIfAbsent("_correlationId", correlationId);
                String formattedJsonParams = convertAndSerializeParams(paramsToFormat, subtaskRecord.getName());

                Long dsProcessDefinitionCode = getProcessCode(request.getTemplateId());
                if (dsProcessDefinitionCode == null) {
                    throw new TaskException(TaskErrorCode.TASK_TEMPLATE_NOT_EXISTED, request.getTemplateId());
                }

                taskTemplate = taskTemplateMapper.selectById(request.getTemplateId());
                ProcessInstanceCreateParam startParam = createDolphinStartParam(dsProcessDefinitionCode, formattedJsonParams, originalParams, taskTemplate);

                log.info("Attempting to start DS instance for correlationId: {} (Priority: {}, DS Priority: {}), DS ProcessDefCode: {}, params: {}",
                        correlationId, request.getPriority(), startParam.getProcessInstancePriority(), dsProcessDefinitionCode, formattedJsonParams);

                boolean started = dolphinClient.opsForProcessInst().start(dolphinProperties.getProjectCode(), startParam);
                submissionApiSuccess = started;

                if (started) {
                    log.info("DS start API successful for correlationId: {}. Fetching ProcessInstanceId...", correlationId);
                    processInstanceId = fetchInstanceIdWithRetry(dsProcessDefinitionCode, correlationId);
                    if (processInstanceId != null) {
                        subtaskRecord.setDsInstanceId(processInstanceId);
                        finalSubmissionStatus = SubtaskInstance.STATUS_TRACKING;
                        log.info("Fetched DS ProcessInstanceId: {} for correlationId: {}. Status -> TRACKING.", processInstanceId, correlationId);

                        // ====================== 新增代码开始 ======================
                        // 既然子任务已开始跟踪，就立即更新父实例的状态为"RUNNING"
                        if (taskInstanceService != null) {
                            taskInstanceService.updateParentTaskInstanceStatus(request.getTaskInstanceId());
                        } else {
                            log.error("TaskInstanceService is null, cannot update parent task status for instance: {}", request.getTaskInstanceId());
                        }


                    } else {
                        finalSubmissionStatus = SubtaskInstance.STATUS_SUBMIT_FAILED_NO_ID;
                        errorMessage = "DS API succeeded but failed to fetch DS ProcessInstanceId.";
                        runningTaskCount.decrementAndGet(); countDecrementedLocally = true;
                        log.error(errorMessage + " CorrelationId: {}. Count decremented.", correlationId);
                    }
                } else {
                    finalSubmissionStatus = SubtaskInstance.STATUS_SUBMIT_FAILED_API;
                    errorMessage = "DS start API returned failure.";
                    runningTaskCount.decrementAndGet(); countDecrementedLocally = true;
                    log.error(errorMessage + " CorrelationId: {}. Count decremented.", correlationId);
                }
            } catch (Exception e) {
                log.error("Exception during submission/ID fetch for correlationId: {}", correlationId, e);
                errorMessage = "Exception: " + e.getMessage();
                finalSubmissionStatus = submissionApiSuccess ? SubtaskInstance.STATUS_SUBMIT_FAILED_NO_ID : SubtaskInstance.STATUS_SUBMIT_FAILED_API;
                if (!countDecrementedLocally) { runningTaskCount.decrementAndGet(); log.warn("Count decremented due to exception for {}", correlationId); }
            } finally {
                subtaskRecord.setSubmissionStatus(finalSubmissionStatus);
                if (processInstanceId != null) subtaskRecord.setDsInstanceId(processInstanceId);
                if (errorMessage != null) subtaskRecord.setErrorMessage(errorMessage);
                updateSubtaskRecord(subtaskRecord);

                if (SubtaskInstance.STATUS_TRACKING.equals(finalSubmissionStatus) && processInstanceId != null) {
                    log.info("Submission successful, entered TRACKING for ProcessInstanceId: {}. Triggering YARN App ID fetch for correlationId: {}", processInstanceId, correlationId);
                    final Integer finalProcessInstanceId = processInstanceId;
                    final Long finalSubtaskRecordId = subtaskRecord.getId();
                    logParsingExecutor.submit(() -> {
                        fetchAndStoreYarnAppIdLogic(
                                finalSubtaskRecordId,
                                finalProcessInstanceId,
                                correlationId,
                                3,
                                5000L,
                                "[InitialYarnAppIdFetch]"
                        );
                    });
                } else {
                    log.warn("Submission process for {} ended with non-tracking status: {} or ProcessInstanceId is null.", correlationId, finalSubmissionStatus);
                }
            }
        });
    }
    private void fetchAndStoreYarnAppIdLogic(
            Long subtaskInstanceId, Integer processInstanceId, String correlationId,
            int fetchAttempts, long fetchDelayMs, String logPrefix) {

        SubtaskInstance currentSubtask = subtaskInstanceMapper.selectById(subtaskInstanceId);
        if (currentSubtask == null) {
            log.warn("{} 子任务记录 (ID: {}) 未找到。无法为DS流程实例 {} 获取YARN App ID。", logPrefix, subtaskInstanceId, processInstanceId);
            return;
        }
        if (currentSubtask.getYarnApplicationId() != null) {
            log.info("{} YARN Application ID ({}) 已存在于DS流程实例 {} 中。跳过获取。",
                    logPrefix, currentSubtask.getYarnApplicationId(), processInstanceId);
            return;
        }

        log.info("{} 尝试为DS流程实例 {} (CorrelationId: {}) 获取YARN Application ID。",
                logPrefix, processInstanceId, correlationId);
        String yarnAppId = null;
        Long dsTaskInstanceIdToQueryLogs = null;

        Long projectCode = dolphinProperties.getProjectCode();
        if (projectCode == null) {
            updateYarnAppIdFetchError(subtaskInstanceId, logPrefix + " 项目代码 (Project code) 为空，无法获取任务实例。");
            return;
        }

        ProcessInstanceTasksQueryResp tasksInWorkflowInstance;
        try {
            tasksInWorkflowInstance = dolphinClient.opsForProcessInst().fetchTasksForProcessInstance(projectCode, processInstanceId);
        } catch (Exception e) {
            updateYarnAppIdFetchError(subtaskInstanceId, logPrefix + " 获取DS任务实例失败，DS流程实例ID " + processInstanceId + ": " + e.getMessage());
            return;
        }

        if (tasksInWorkflowInstance == null || tasksInWorkflowInstance.getTaskList() == null || tasksInWorkflowInstance.getTaskList().isEmpty()) {
            updateYarnAppIdFetchError(subtaskInstanceId, logPrefix + " 在流程实例 " + processInstanceId + " 中未找到任何DS任务实例。");
            return;
        }

        TaskTemplate taskTemplate = taskTemplateMapper.selectById(currentSubtask.getTemplateId());
        Long expectedDsTaskDefinitionCode = taskTemplate != null ? taskTemplate.getTaskCode() : null;

        Optional<ProcessInstanceTasksQueryResp.Task> targetTaskOpt = tasksInWorkflowInstance.getTaskList().stream()
                .filter(t -> expectedDsTaskDefinitionCode != null && t.getTaskCode() != null && t.getTaskCode().equals(expectedDsTaskDefinitionCode))
                .findFirst();

        if (targetTaskOpt.isPresent()) {
            dsTaskInstanceIdToQueryLogs = targetTaskOpt.get().getId();
            log.info("{} 通过TaskDefinitionCode为流程实例 {} 找到了目标DS任务实例ID: {}",
                    logPrefix, processInstanceId, dsTaskInstanceIdToQueryLogs);
        } else if (tasksInWorkflowInstance.getTaskList().size() == 1) {
            dsTaskInstanceIdToQueryLogs = tasksInWorkflowInstance.getTaskList().get(0).getId();
            log.warn("{} 无法通过TaskDefinitionCode匹配，但由于只有一个任务，将使用此DS任务实例ID: {} (期望的Code: {})。",
                    logPrefix, dsTaskInstanceIdToQueryLogs, expectedDsTaskDefinitionCode);
        } else {
            String errorMsg = String.format("%s 无法在 %d 个任务中唯一确定目标DS任务实例。流程实例ID: %d, 期望的TaskDefinitionCode: %s",
                    logPrefix, tasksInWorkflowInstance.getTaskList().size(), processInstanceId, expectedDsTaskDefinitionCode);
            log.warn(errorMsg);
            updateYarnAppIdFetchError(subtaskInstanceId, errorMsg);
            return;
        }

        if (dsTaskInstanceIdToQueryLogs == null) {
            updateYarnAppIdFetchError(subtaskInstanceId, logPrefix + " 无法确定用于查询日志的DS任务实例ID。");
            return;
        }

        for (int i = 0; i < fetchAttempts; i++) {
            try {
                if (i > 0) Thread.sleep(fetchDelayMs);
                long logFetchLimit = 10000L;
                ProcessInstanceLogResp dsLogResp = dolphinClient.opsForProcessInst().log(dsTaskInstanceIdToQueryLogs, logFetchLimit, 0L);

                if (dsLogResp != null && dsLogResp.getMessage() != null && !dsLogResp.getMessage().isEmpty()) {
                    String logContent = dsLogResp.getMessage();
                    Pattern pattern = Pattern.compile(
                            "(?:Submitting application master|Submitted application|YARN Application ID:|Flink Job YARN APP Id:|of application ')\\s*(application_\\d+_\\d+)",
                            Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(logContent);
                    if (matcher.find()) {
                        yarnAppId = matcher.group(1);
                        log.info("{} 在DS任务实例 {} 的日志中找到YARN App ID: {} (尝试次数 {}/{})",
                                logPrefix, dsTaskInstanceIdToQueryLogs, yarnAppId, i + 1, fetchAttempts);
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{} 日志获取过程被中断。DS任务实例ID: {}", logPrefix, dsTaskInstanceIdToQueryLogs, e);
                break;
            } catch (Exception e) {
                log.error("{} 获取或解析日志时出错。DS任务实例ID: {} (尝试次数 {}/{})",
                        logPrefix, dsTaskInstanceIdToQueryLogs, i + 1, fetchAttempts, e);
            }
        }

        SubtaskInstance finalSubtaskToUpdate = subtaskInstanceMapper.selectById(subtaskInstanceId);
        if (finalSubtaskToUpdate == null) {
            log.error("{} 在更新YARN App ID之前，子任务记录(ID: {})消失了。",
                    logPrefix, subtaskInstanceId);
            return;
        }

        if (yarnAppId != null) {
            if (finalSubtaskToUpdate.getYarnApplicationId() == null) {
                finalSubtaskToUpdate.setYarnApplicationId(yarnAppId);
                finalSubtaskToUpdate.setUpdateTime(new Date());
                subtaskInstanceMapper.updateById(finalSubtaskToUpdate);
                log.info("{} 成功存储YARN App ID: {}，对应DS流程实例ID: {}, CorrelationId: {}",
                        logPrefix, yarnAppId, processInstanceId, correlationId);
            }
        } else {
            log.warn("{} 尝试 {} 次后，仍未找到YARN App ID。DS流程实例ID: {}",
                    logPrefix, fetchAttempts, processInstanceId);
            if (finalSubtaskToUpdate.getYarnApplicationId() == null && logPrefix.contains("[YarnAppIdCompensation]")) {
                updateYarnAppIdFetchError(subtaskInstanceId, logPrefix + " YARN_APP_ID_FETCH_FAILED_COMPENSATION");
            }
        }
    }

    private void updateYarnAppIdFetchError(Long subtaskInstanceId, String errorMessage) {
        SubtaskInstance subtaskToUpdate = subtaskInstanceMapper.selectById(subtaskInstanceId);
        if (subtaskToUpdate != null && subtaskToUpdate.getYarnApplicationId() == null) {
            String currentErrorMessage = subtaskToUpdate.getErrorMessage() == null ? "" : subtaskToUpdate.getErrorMessage();
            if (!currentErrorMessage.contains(errorMessage)) {
                subtaskToUpdate.setErrorMessage(currentErrorMessage + (currentErrorMessage.isEmpty() ? "" : "; ") + errorMessage);
                subtaskToUpdate.setUpdateTime(new Date());
                subtaskInstanceMapper.updateById(subtaskToUpdate);
            }
            log.warn("为子任务 {} 更新YARN App ID获取错误: {}", subtaskInstanceId, errorMessage);
        }
    }

    private static final int DS_STATE_SUBMIT_SUCCEEDED = 0;
    private static final int DS_STATE_RUNNING = 1;
    private static final int DS_STATE_PREPARE_PAUSE = 2;
    private static final int DS_STATE_PAUSE = 3;
    private static final int DS_STATE_PREPARE_STOP = 4;
    private static final int DS_STATE_STOP = 5;
    private static final int DS_STATE_FAILURE = 6;
    private static final int DS_STATE_SUCCESS = 7;
    private static final int DS_STATE_NEED_FAULT_TOLERANCE = 8;
    private static final int DS_STATE_KILL = 9;
    private static final int DS_STATE_WAITING_THREAD = 10;
    private static final int DS_STATE_WAITING_DEPENDENCY = 11;
    private static final String INTERNAL_STATUS_NOT_FOUND = "NOT_FOUND";
    private static final String INTERNAL_STATUS_QUERY_ERROR = "QUERY_ERROR";
    private static final String INTERNAL_STATUS_CHECK_EXCEPTION = "CHECK_EXCEPTION";

    private boolean isConsideredTerminalForSlotRelease(String status) {
        if (status == null) return false;
        try {
            int statusCode = Integer.parseInt(status);
            switch (statusCode) {
                case DS_STATE_PAUSE:
                case DS_STATE_STOP:
                case DS_STATE_FAILURE:
                case DS_STATE_SUCCESS:
                case DS_STATE_KILL:
                    return true;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            return INTERNAL_STATUS_NOT_FOUND.equals(status.toUpperCase()) ||
                    INTERNAL_STATUS_QUERY_ERROR.equals(status.toUpperCase()) ||
                    INTERNAL_STATUS_CHECK_EXCEPTION.equals(status.toUpperCase());
        }
    }

    private void checkAndUpdateTaskStatus() {
        List<SubtaskInstance> unfinishedSubtasks;
        try {
            unfinishedSubtasks = subtaskInstanceMapper.findUnfinishedSubtasks();
        } catch (Exception e) {
            log.error("从数据库查询未完成的子任务失败", e);
            return;
        }

        if (unfinishedSubtasks.isEmpty()) {
            log.debug("没有处于活动状态的子任务需要检查。");
            return;
        }

        log.info("开始检查 {} 个活动状态的子任务...", unfinishedSubtasks.size());
        int freedSlots = 0;

        for (SubtaskInstance subtask : unfinishedSubtasks) {
            if (!SubtaskInstance.STATUS_TRACKING.equals(subtask.getSubmissionStatus())) {
                continue;
            }

            Integer dsInstanceId = subtask.getDsInstanceId();
            String correlationId = subtask.getCorrelationId();
            boolean taskFinishedOrFailed = false;
            String currentSubmissionStatus = subtask.getSubmissionStatus();
            String finalDsStatusToLog = subtask.getFinalDsStatus();
            String errorMessage = subtask.getErrorMessage();

            if (dsInstanceId == null) {
                log.warn("子任务 (CorrelationId: {}) 处于TRACKING状态但没有ds_instance_id，标记为错误。", correlationId);
                currentSubmissionStatus = SubtaskInstance.STATUS_STATUS_CHECK_ERROR;
                errorMessage = "TRACKING状态下ds_instance_id为空";
                finalDsStatusToLog = "INTERNAL_QUERY_ERROR";
                taskFinishedOrFailed = true;
            } else {
                try {
                    String dsStatusString = getProcessInstanceStatusFromDS(dsInstanceId);
                    finalDsStatusToLog = dsStatusString;

                    if (isConsideredTerminalForSlotRelease(dsStatusString)) {
                        taskFinishedOrFailed = true;
                        log.info("子任务实例 (DS Instance ID: {}) 到达终态: {}. CorrelationId: {}",
                                dsInstanceId, statusStringFromCode(dsStatusString), correlationId);

                        // [关键修复]：在任务到达终态时，最后再尝试获取一次Yarn ID
                        if (subtask.getYarnApplicationId() == null) {
                            log.info("任务 {} 到达终态，在标记完成前最后一次尝试获取Yarn App ID。", correlationId);
                            fetchAndStoreYarnAppIdLogic(
                                    subtask.getId(),
                                    dsInstanceId,
                                    correlationId,
                                    1, // 只需尝试一次
                                    0, // 无需延迟
                                    "[FinalYarnAppIdFetch]"
                            );
                            SubtaskInstance updatedSubtask = subtaskInstanceMapper.selectById(subtask.getId());
                            if (updatedSubtask != null) {
                                subtask = updatedSubtask;
                            }
                        }

                        if (String.valueOf(7).equals(dsStatusString)) {
                            currentSubmissionStatus = SubtaskInstance.STATUS_COMPLETED;
                            errorMessage = null;
                            log.info("子任务实例 {} (CorrelationId: {}) 成功完成。", dsInstanceId, correlationId);
                        } else if (String.valueOf(6).equals(dsStatusString)) {
                            log.warn("子任务实例 {} (CorrelationId: {}) 执行失败。", dsInstanceId, correlationId);
                            if (subtask.getRetryCount() < maxRetries) {
                                subtask.setRetryCount(subtask.getRetryCount() + 1);
                                currentSubmissionStatus = SubtaskInstance.STATUS_RETRY_SCHEDULED;
                                errorMessage = String.format("将在 %d 毫秒后进行第 %d/%d 次重试。",
                                        retryDelayMillis, subtask.getRetryCount(), maxRetries);
                                log.info(errorMessage + " CorrelationId: {}", correlationId);

                                TaskSubmissionRequest retryRequest = buildRequestFromLog(subtask);
                                if (retryRequest != null) {
                                    retryScheduler.schedule(() -> enqueueTask(retryRequest), retryDelayMillis, TimeUnit.MILLISECONDS);
                                } else {
                                    currentSubmissionStatus = SubtaskInstance.STATUS_FAILED_PERMANENTLY;
                                    errorMessage = "任务失败，且无法构建重试请求。";
                                    log.error(errorMessage + " CorrelationId: {}", correlationId);
                                }
                            } else {
                                currentSubmissionStatus = SubtaskInstance.STATUS_FAILED_PERMANENTLY;
                                errorMessage = String.format("重试 %d 次后仍然失败，任务被标记为永久失败。", maxRetries);
                                log.error(errorMessage + " CorrelationId: {}", correlationId);
                            }
                        } else {
                            currentSubmissionStatus = SubtaskInstance.STATUS_STOPPED;
                            errorMessage = "任务被手动停止或杀死: " + statusStringFromCode(dsStatusString);
                            log.info("子任务实例 {} (CorrelationId: {}) 已停止，最终DS状态: {}", dsInstanceId, correlationId, statusStringFromCode(dsStatusString));
                        }
                    }
                } catch (Exception e) {
                    log.error("检查实例状态时发生异常, DS Instance ID: {}, CorrelationId: {}", dsInstanceId, correlationId, e);
                    currentSubmissionStatus = SubtaskInstance.STATUS_STATUS_CHECK_ERROR;
                    errorMessage = "检查状态时发生异常: " + e.getMessage();
                    finalDsStatusToLog = "INTERNAL_CHECK_EXCEPTION";
                    taskFinishedOrFailed = true;
                }
            }

            boolean logNeedsUpdate = !Objects.equals(subtask.getSubmissionStatus(), currentSubmissionStatus);
            if (logNeedsUpdate) {
                subtask.setSubmissionStatus(currentSubmissionStatus);
                subtask.setFinalDsStatus(finalDsStatusToLog);
                subtask.setErrorMessage(errorMessage);
                updateSubtaskRecord(subtask);
            }

            if (taskFinishedOrFailed) {
                runningTaskCount.decrementAndGet();
                freedSlots++;
                log.info("子任务结束 (CorrelationId: {}), 释放一个并发槽。当前运行数: {}",
                        correlationId, runningTaskCount.get());

                if (taskInstanceService != null) {
                    taskInstanceService.updateParentTaskInstanceStatus(subtask.getTaskInstanceId());
                } else {
                    log.error("TaskInstanceService is null, cannot update parent task status for instance: {}", subtask.getTaskInstanceId());
                }
            }
        }

        if (freedSlots > 0) {
            log.info("状态检查完成，释放了 {} 个并发槽。", freedSlots);
        }
    }



    private void updateSubtaskRecord(SubtaskInstance subtask) {
        if (subtask == null || subtask.getId() == null) {
            log.error("Attempted to update a null or ID-less subtask record.");
            return;
        }
        try {
            subtask.setUpdateTime(new Date());
            int updatedRows = subtaskInstanceMapper.updateById(subtask);
            if (updatedRows == 0) {
                log.warn("Failed to update subtask record in DB (0 rows affected). Log ID: {}", subtask.getId());
            }
        } catch (Exception dbEx) {
            log.error("Failed to update subtask record in DB. Log ID: {}", subtask.getId(), dbEx);
        }
    }


    private void loadAndResumeUnfinishedTasks() {
        log.info("Checking for unfinished tasks in the database...");
        List<SubtaskInstance> unfinishedTasks;
        try {
            unfinishedTasks = subtaskInstanceMapper.selectList(
                    new LambdaQueryWrapper<SubtaskInstance>()
                            .in(SubtaskInstance::getSubmissionStatus,
                                    SubtaskInstance.STATUS_SUBMITTING,
                                    SubtaskInstance.STATUS_TRACKING,
                                    SubtaskInstance.STATUS_RETRY_SCHEDULED,
                                    SubtaskInstance.STATUS_QUEUED)
            );
        } catch (Exception e) {
            log.error("Failed to query unfinished tasks from database during initialization", e);
            return;
        }

        if (unfinishedTasks.isEmpty()) {
            log.info("No potentially active unfinished tasks found.");
            return;
        }

        log.warn("Found {} potentially unfinished tasks on startup. Resuming/Re-queuing...", unfinishedTasks.size());
        int resumedTrackingCount = 0;
        int reQueuedCount = 0;

        for(SubtaskInstance task : unfinishedTasks) {
            log.warn("Processing unfinished task: CorrelationId: {}, Status: {}, DS InstanceId: {}",
                    task.getCorrelationId(), task.getSubmissionStatus(), task.getDsInstanceId());

            if (SubtaskInstance.STATUS_TRACKING.equals(task.getSubmissionStatus())) {
                if (task.getDsInstanceId() != null) {
                    runningTaskCount.incrementAndGet();
                    resumedTrackingCount++;
                    log.info("Resumed TRACKING task. Incremented running count. CorrelationId: {}", task.getCorrelationId());
                } else {
                    log.error("Found TRACKING task without DS instance ID on startup. Marking as error. CorrelationId: {}", task.getCorrelationId());
                    task.setSubmissionStatus(SubtaskInstance.STATUS_STATUS_CHECK_ERROR);
                    task.setErrorMessage("Resumed from TRACKING state with null ds_instance_id");
                    updateSubtaskRecord(task);
                }
            } else if (SubtaskInstance.STATUS_SUBMITTING.equals(task.getSubmissionStatus())) {
                log.warn("Found SUBMITTING task on startup. Assuming submission failed. Marking as error. CorrelationId: {}", task.getCorrelationId());
                task.setSubmissionStatus(SubtaskInstance.STATUS_SUBMIT_FAILED_API);
                task.setErrorMessage("Resumed from SUBMITTING state, assuming failure.");
                updateSubtaskRecord(task);
            } else if (SubtaskInstance.STATUS_RETRY_SCHEDULED.equals(task.getSubmissionStatus()) || SubtaskInstance.STATUS_QUEUED.equals(task.getSubmissionStatus())) {
                TaskSubmissionRequest resumedRequest = buildRequestFromLog(task);
                if (resumedRequest != null) {
                    if (SubtaskInstance.STATUS_RETRY_SCHEDULED.equals(task.getSubmissionStatus())) {
                        log.info("Rescheduling enqueue for RETRY_SCHEDULED task found on startup. CorrelationId: {}", task.getCorrelationId());
                        retryScheduler.schedule(() -> {
                            log.info("Executing rescheduled enqueue from startup. CorrelationId: {}", resumedRequest.getCorrelationId());
                            enqueueTask(resumedRequest);
                        }, retryDelayMillis, TimeUnit.MILLISECONDS);
                    } else {
                        log.info("Re-queuing task found in QUEUED state on startup. CorrelationId: {}", task.getCorrelationId());
                        enqueueTask(resumedRequest);
                    }
                    reQueuedCount++;
                } else {
                    log.error("Could not rebuild request for {} task found on startup. Marking as failed. Log ID: {}", task.getSubmissionStatus(), task.getId());
                    task.setSubmissionStatus(SubtaskInstance.STATUS_FAILED_PERMANENTLY);
                    task.setErrorMessage("Resumed from " + task.getSubmissionStatus() + ", failed to rebuild request.");
                    updateSubtaskRecord(task);
                }
            }
        }
        log.info("Finished processing unfinished tasks. Resumed Tracking: {}, Re-queued/Scheduled: {}. Current running count: {}",
                resumedTrackingCount, reQueuedCount, runningTaskCount.get());
    }

    private void updateLogError(SubtaskInstance subtask, String status, String errorMessage, Integer dsInstanceId) {
        if (subtask != null && subtask.getId() != null) {
            subtask.setSubmissionStatus(status);
            subtask.setErrorMessage(errorMessage);
            if (dsInstanceId != null) {
                subtask.setDsInstanceId(dsInstanceId);
            }
            this.updateSubtaskRecord(subtask);
        }
    }

    public TaskSubmissionRequest buildRequestFromLog(SubtaskInstance subtask) {
        if (subtask == null) {
            log.error("无法从空的子任务记录构建请求。");
            return null;
        }

        try {
            if (!StringUtils.hasText(subtask.getRequestParamsJson())) {
                log.error("无法重建请求：requestParamsJson 为空。子任务ID: {}", subtask.getId());
                return null;
            }

            Map<String, Object> originalTaskParams = objectMapper.readValue(
                    subtask.getRequestParamsJson(),
                    new TypeReference<Map<String, Object>>() {}
            );

            TaskTemplate taskTemplate = taskTemplateMapper.selectById(subtask.getTemplateId());
            if (taskTemplate == null) {
                log.error("重建请求时找不到模板(ID: {})。将使用默认优先级。子任务ID: {}",
                        subtask.getTemplateId(), subtask.getId());
            }

            int priority = TaskSubmissionRequest.parsePriority(originalTaskParams, taskTemplate);
            log.debug("为重试/恢复的子任务 (CorrelationId: {}) 重建了优先级: {}", subtask.getCorrelationId(), priority);

            SubtaskInstance subtaskForRequest = new SubtaskInstance();
            subtaskForRequest.setId(subtask.getId());
            subtaskForRequest.setCorrelationId(subtask.getCorrelationId());
            subtaskForRequest.setName(subtask.getName());

            subtaskForRequest.setTaskInstanceId(subtask.getTaskInstanceId());

            subtaskForRequest.setTemplateId(subtask.getTemplateId());
            subtaskForRequest.setRequestParamsJson(subtask.getRequestParamsJson());
            subtaskForRequest.setDsInstanceId(null);
            subtaskForRequest.setSubmissionStatus(subtask.getSubmissionStatus());
            subtaskForRequest.setFinalDsStatus(subtask.getFinalDsStatus());
            subtaskForRequest.setRetryCount(subtask.getRetryCount());
            subtaskForRequest.setErrorMessage(subtask.getErrorMessage());
            subtaskForRequest.setCreateTime(subtask.getCreateTime());


            return new TaskSubmissionRequest(
                    subtask.getTemplateId(),
                    originalTaskParams,
                    subtask.getCorrelationId(),
                    subtask.getTaskInstanceId(),
                    subtaskForRequest,
                    priority
            );
        } catch (Exception e) {
            log.error("从日志构建请求失败。子任务ID: {}, CorrelationId: {}",
                    subtask.getId(), subtask.getCorrelationId(), e);
            return null;
        }
    }

    private String convertAndSerializeParams(Map<String, Object> taskParams,String subtaskName) {
        HashMap<String, String> parsedMap = new HashMap<>();
        if (subtaskName != null) {
            parsedMap.put("subtask_name", subtaskName);
        }
        if (taskParams != null) {
            for (Map.Entry<String, Object> entry : taskParams.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (TaskSubmissionRequest.PRIORITY_LEVEL_PARAM_KEY.equals(key) || TaskSubmissionRequest.DS_PRIORITY_PARAM_KEY.equals(key)) {
                    log.trace("Skipping priority key '{}' during DS startParams serialization.", key);
                    continue;
                }
                if ("_correlationId".equals(key)) {
                    parsedMap.put(key, (value != null) ? value.toString() : null);
                    continue;
                }

                if (TaskParamsConstant.MaxCompute.PARTITION_SPEC.equals(key)) {
                    if (value instanceof String && !((String) value).isEmpty()) {
                        parsedMap.put(key, "partition_spec=\"" + value.toString() + "\"");
                    } else {
                        parsedMap.put(key, "");
                    }
                }
                else if (value instanceof List<?>) {
                    List<?> listValue = (List<?>) value;
                    if (listValue.isEmpty()) {
                        throw  new TaskException(TaskErrorCode.TASK_PARAMETER_PARSED_ERROR);
                    } else {
                        String listAsString = "(" + listValue.stream()
                                .map(this::formatValueForDsInternal)
                                .collect(Collectors.joining(","))
                                + ")";
                        parsedMap.put(key, listAsString);
                    }
                }
                else if(value instanceof String) {
                    String valueStr = (String) value;
                    if (valueStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        valueStr = valueStr + " 00:00:00";
                        parsedMap.put(key, "TO_TIMESTAMP('" + valueStr + "')");
                    }
                    else if (valueStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
                        valueStr = valueStr + ":00";
                        parsedMap.put(key, "TO_TIMESTAMP('" + valueStr + "')");
                    }
                    else if (valueStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                        parsedMap.put(key, "TO_TIMESTAMP('" + valueStr + "')");
                    } else {
                        parsedMap.put(key, formatValueForDsInternal(value));
                    }
                }
                else if ("overwrite".equals(key)) {
                    parsedMap.put(key, (value != null) ? value.toString().toLowerCase() : "false");
                }
                else {
                    parsedMap.put(key, formatValueForDsInternal(value));
                }
            }
        }
        try {
            String jsonResult = objectMapper.writeValueAsString(parsedMap);
            log.trace("Serialized params for DS: {}", jsonResult);
            return jsonResult;
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize task parameters map to JSON for DS startParams", e);
            throw new TaskException(TaskErrorCode.TASK_PARAMETER_PARSED_ERROR);
        }
    }

    private String formatValueForDsInternal(Object value) {
        if (value == null) return "";
        if (value instanceof String || value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        log.warn("Formatting potentially unsupported type for DS startParams value: {}", value.getClass().getName());
        return value.toString();
    }

    private ProcessInstanceCreateParam createDolphinStartParam(Long processCode, String jsonParams, Map<String, Object> originalParams, TaskTemplate taskTemplate) {
        ProcessInstanceCreateParam startParam = new ProcessInstanceCreateParam();

        String dsPriority = TaskSubmissionRequest.parseDsPriority(originalParams, taskTemplate);
        log.debug("Setting DS instance priority to: {}", dsPriority);

        startParam.setProcessDefinitionCode(processCode)
                .setProcessInstancePriority(dsPriority)
                .setStartParams(jsonParams)
                .setScheduleTime("")
                .setFailureStrategy("END")
                .setWarningType("NONE")
                .setWarningGroupId(0L)
                .setExecType(null)
                .setStartNodeList(null)
                .setTaskDependType("TASK_POST")
                .setRunMode("RUN_MODE_SERIAL")
                .setWorkerGroup("default")
                .setEnvironmentCode(null)
                .setExpectedParallelismNumber(null)
                .setDryRun(0);
        return startParam;
    }


    private Integer fetchInstanceIdWithRetry(Long processCode, String correlationId) {
        int retries = 5;
        long delay = 3000;
        long timeWindowMillis = 24*60 * 60 * 1000;

        Date startTimeAfter = new Date(System.currentTimeMillis() - timeWindowMillis);
        int queryLimit = 500;

        log.info("[fetchInstanceId] 开始查找 correlationId: {}, processCode: {}", correlationId, processCode);

        for (int i = 0; i < retries; i++) {
            try {
                if (i > 0) {
                    log.debug("[fetchInstanceId] 等待 {}ms 后进行第 {} 次重试...", delay, i + 1);
                    Thread.sleep(delay);
                }
                log.info("[fetchInstanceId] 第 {}/{} 次尝试查找 correlationId: {}", i + 1, retries, correlationId);

                List<DsProcessInstance> recentInstances;
                try {
                    log.debug("[fetchInstanceId] 查询 DS 数据库 (findRecentInstances)... processCode={}, startTimeAfter={}, limit={}", processCode, startTimeAfter, queryLimit);
                    recentInstances = dsProcessInstanceMapper.findRecentInstances(processCode, startTimeAfter, queryLimit);
                    log.debug("[fetchInstanceId] 数据库查询返回 {} 条可能的实例记录.", recentInstances != null ? recentInstances.size() : 0);
                } catch (Exception dbEx) {
                    log.error("[fetchInstanceId] 第 {} 次尝试时数据库查询失败. CorrelationId: {}", i + 1, correlationId, dbEx);
                    continue;
                }

                if (recentInstances == null || recentInstances.isEmpty()) {
                    log.warn("[fetchInstanceId] 第 {} 次尝试: 数据库中未找到 processCode {} 的近期实例. CorrelationId: {}", i + 1, processCode, correlationId);
                    continue;
                }

                Optional<DsProcessInstance> matchedInstanceOpt = recentInstances.stream()
                        .filter(inst -> inst != null && inst.getCommandParam() != null)
                        .peek(inst -> log.trace("[fetchInstanceId] 检查实例 ID: {}, StartTime: {}, CommandParam: '{}'",
                                inst.getId(), inst.getStartTime(), inst.getCommandParam()))
                        .filter(inst -> {
                            boolean containsMatch = inst.getCommandParam().contains(correlationId);
                            log.trace("[fetchInstanceId] 实例 ID: {}. 检查 CommandParam 是否包含 '{}'. 结果: {}",
                                    inst.getId(), correlationId, containsMatch);
                            return containsMatch;
                        })
                        .max(Comparator.comparing(DsProcessInstance::getStartTime));

                if (matchedInstanceOpt.isPresent()) {
                    DsProcessInstance matchedInstance = matchedInstanceOpt.get();
                    log.info("[fetchInstanceId] 成功! (字符串匹配) 找到匹配的实例 ID: {} (开始时间: {}). CorrelationId: {}",
                            matchedInstance.getId(), matchedInstance.getStartTime(), correlationId);
                    return matchedInstance.getId();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[fetchInstanceId] 查找过程被中断. CorrelationId: {}", correlationId);
                return null;
            } catch (Exception e) {
                log.error("[fetchInstanceId] 第 {} 次尝试时发生意外错误. CorrelationId: {}", i + 1, correlationId, e);
            }
        }
        log.error("[fetchInstanceId] 重试 {} 次后仍然无法找到实例 ID (字符串匹配). CorrelationId: {}", retries, correlationId);
        return null;
    }

    public synchronized boolean tryDequeueTask(String correlationId) {
        if (correlationId == null) return false;

        Optional<TaskSubmissionRequest> requestToRemove = taskQueue.stream()
                .filter(req -> correlationId.equals(req.getCorrelationId()))
                .findFirst();

        if (requestToRemove.isPresent()) {
            boolean removed = taskQueue.remove(requestToRemove.get());
            if (removed) {
                log.info("Task with correlationId {} successfully removed from queue.", correlationId);
            } else {
                log.warn("Task with correlationId {} found in queue but failed to remove (concurrent modification?).", correlationId);
            }
            return removed;
        }
        log.info("Task with correlationId {} not found in queue for removal.", correlationId);
        return false;
    }



    private String getProcessInstanceStatusFromDS(Integer instanceId) {
        if (instanceId == null) {
            log.error("Cannot get DS status: instanceId is null.");
            return INTERNAL_STATUS_QUERY_ERROR;
        }
        try {
            log.debug("Querying DS DB for status of instanceId: {}", instanceId);
            DsProcessInstance instance = dsProcessInstanceMapper.findById(instanceId);

            if (instance != null) {
                String state = instance.getState();
                log.info("InstanceId {} found in DS DB. State: '{}'", instanceId, state);
                if (state != null) {
                    return state;
                } else {
                    log.warn("InstanceId {} found, but state is null in DS DB.", instanceId);
                    return INTERNAL_STATUS_QUERY_ERROR;
                }
            } else {
                log.warn("InstanceId {} not found in DS database.", instanceId);
                return INTERNAL_STATUS_NOT_FOUND;
            }
        } catch (Exception e) {
            log.error("Error querying DS database for status of instanceId: {}", instanceId, e);
            return INTERNAL_STATUS_QUERY_ERROR;
        }
    }

    private Long getProcessCode(Long templateId) {
        TaskTemplate taskTemplate = taskTemplateMapper.selectById(templateId);
        if (taskTemplate == null) {
            log.error("Task template not found for ID: {}", templateId);
            throw new TaskException(TaskErrorCode.TASK_TEMPLATE_NOT_EXISTED, templateId);
        }
        return taskTemplate.getProcessCode();
    }


    private String safeToJson(Object object) {
        if (object == null) return null;
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON for logging", e);
            return "{\"error\":\"Serialization failed\"}";
        }
    }

    // =========== 新增的、缺失的辅助方法 =================
    private String statusStringFromCode(String statusCodeStr) {
        if (statusCodeStr == null) return "UNKNOWN";
        try {
            int code = Integer.parseInt(statusCodeStr);
            switch (code) {
                case DS_STATE_SUBMIT_SUCCEEDED: return "SUBMIT_SUCCEEDED(0)";
                case DS_STATE_RUNNING: return "RUNNING(1)";
                case DS_STATE_PREPARE_PAUSE: return "PREPARE_PAUSE(2)";
                case DS_STATE_PAUSE: return "PAUSE(3)";
                case DS_STATE_PREPARE_STOP: return "PREPARE_STOP(4)";
                case DS_STATE_STOP: return "STOP(5)";
                case DS_STATE_FAILURE: return "FAILURE(6)";
                case DS_STATE_SUCCESS: return "SUCCESS(7)";
                case DS_STATE_NEED_FAULT_TOLERANCE: return "NEED_FAULT_TOLERANCE(8)";
                case DS_STATE_KILL: return "KILL(9)";
                case DS_STATE_WAITING_THREAD: return "WAITING_THREAD(10)";
                case DS_STATE_WAITING_DEPENDENCY: return "WAITING_DEPENDENCY(11)";
                default: return "UNKNOWN_CODE(" + code + ")";
            }
        } catch (NumberFormatException e) {
            switch (statusCodeStr.toUpperCase()) {
                case INTERNAL_STATUS_NOT_FOUND: return "NOT_FOUND_IN_DS_DB";
                case INTERNAL_STATUS_QUERY_ERROR: return "DS_DB_QUERY_ERROR";
                case INTERNAL_STATUS_CHECK_EXCEPTION: return "STATUS_CHECK_EXCEPTION";
                default: return statusCodeStr;
            }
        }
    }

    public String translateDsState(String dsStateCode) {
        if (dsStateCode == null || dsStateCode.trim().isEmpty()) return "UNKNOWN";
        try {
            int code = Integer.parseInt(dsStateCode);
            switch (code) {
                case 0: return "SUBMITTED_SUCCESS";
                case 1: return "RUNNING_EXECUTION";
                case 2: return "READY_PAUSE";
                case 3: return "PAUSE";
                case 4: return "READY_STOP";
                case 5: return "STOP";
                case 6: return "FAILURE";
                case 7: return "SUCCESS";
                case 8: return "NEED_FAULT_TOLERANCE";
                case 9: return "KILL";
                case 10: return "WAITING_THREAD";
                case 11: return "WAITING_DEPENDENCY";
                default: return "DS_CODE_" + code;
            }
        } catch (NumberFormatException e) {
            return dsStateCode;
        }
    }

    public String buildYarnLogUrl(String yarnApplicationId) {
        if (yarnApplicationId != null && yarnLogUrlTemplate != null && !yarnLogUrlTemplate.isEmpty()) {
            return yarnLogUrlTemplate.replace("{appId}", yarnApplicationId);
        }
        return null;
    }
    // =======================================================

    public TableDataInfo<SubTaskDetailVo> pageSubTaskDetails(SubTaskQueryDto dto, PageQuery pageQuery) {
        LambdaQueryWrapper<SubtaskInstance> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.hasText(dto.getInstanceIdFilter()), SubtaskInstance::getTaskInstanceId, dto.getInstanceIdFilter());
        lqw.like(StringUtils.hasText(dto.getTemplateNameFilter()), SubtaskInstance::getTemplateId, dto.getTemplateNameFilter());
        lqw.eq(StringUtils.hasText(dto.getSubmissionStatusFilter()), SubtaskInstance::getSubmissionStatus, dto.getSubmissionStatusFilter());
        lqw.orderByDesc(SubtaskInstance::getCreateTime);

        Page<SubtaskInstance> pageResult = subtaskInstanceMapper.selectPage(pageQuery.build(), lqw);

        Page<SubTaskDetailVo> voPage = new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
        List<SubTaskDetailVo> voList = pageResult.getRecords().stream()
                .map(this::convertLogToSubTaskDetailVo)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return TableDataInfo.build(voPage);
    }
    private SubTaskDetailVo convertLogToSubTaskDetailVo(SubtaskInstance subtask) {
        SubTaskDetailVo subVo = new SubTaskDetailVo();
        BeanUtils.copyProperties(subtask, subVo);
        subVo.setDsStatusString(this.translateDsState(subtask.getFinalDsStatus()));
        subVo.setYarnLogUrl(this.buildYarnLogUrl(subtask.getYarnApplicationId()));
        return subVo;
    }
    @Transactional
    public boolean stopInstanceByDsInstanceId(Long dsInstanceId, String correlationId) {
        if (dsInstanceId == null) {
            log.error("Cannot stop instance: dsInstanceId is null for correlationId: {}", correlationId);
            updateSubmissionLogError(correlationId, "Stop failed: dsInstanceId is null.");
            return false;
        }

        log.info("Requesting to stop DS instanceId: {} (correlationId: {})", dsInstanceId, correlationId);
        boolean dsStopApiCallSuccess = false;
        try {
            dsStopApiCallSuccess = dolphinClient.opsForProcessInst().stop(dolphinProperties.getProjectCode(), dsInstanceId);
            if (!dsStopApiCallSuccess) {
                log.error("Failed to stop DS instanceId via API: {} (correlationId: {})", dsInstanceId, correlationId);
                updateSubmissionLogError(correlationId, "DS stop API call failed for instance " + dsInstanceId);
            } else {
                log.info("Successfully called DS API to stop instanceId: {} (correlationId: {})", dsInstanceId, correlationId);
            }
        } catch (Exception e) {
            log.error("Exception while calling DS stop API for instanceId {} (correlationId: {}): {}", dsInstanceId, correlationId, e.getMessage(), e);
            updateSubmissionLogError(correlationId, "Exception during DS stop API call for instance " + dsInstanceId + ": " + e.getMessage());
            return false;
        }

        SubtaskInstance subtaskRecord = subtaskInstanceMapper.selectByCorrelationId(correlationId);

        if (subtaskRecord != null && subtaskRecord.getYarnApplicationId() != null) {
            String yarnAppId = subtaskRecord.getYarnApplicationId();
            log.info("Attempting to stop associated YARN application: {} for dsInstanceId: {} (correlationId: {})", yarnAppId, dsInstanceId, correlationId);
            try {
                boolean yarnStopSuccess = stopYarnApplication(yarnAppId);
                log.info("YARN application {} stop attempt result: {}", yarnAppId, yarnStopSuccess);
                if (!yarnStopSuccess) {
                    updateSubmissionLogError(correlationId, (subtaskRecord.getErrorMessage() == null ? "" : subtaskRecord.getErrorMessage() + "; ") + "YARN app " + yarnAppId + " stop attempt failed or was not conclusive.");
                }
            } catch (Exception e) {
                log.error("Error attempting to stop YARN application {}: {}", yarnAppId, e.getMessage(), e);
                updateSubmissionLogError(correlationId, (subtaskRecord.getErrorMessage() == null ? "" : subtaskRecord.getErrorMessage() + "; ") + "Error stopping YARN app " + yarnAppId + ": " + e.getMessage());
            }
        } else if (subtaskRecord != null) {
            log.warn("No YARN Application ID found for dsInstanceId: {} (correlationId: {}) in mdp_task_submission_log. Cannot directly stop YARN application.", dsInstanceId, correlationId);
            updateSubmissionLogError(correlationId, (subtaskRecord.getErrorMessage() == null ? "" : subtaskRecord.getErrorMessage() + "; ") + "YARN ID unknown, cannot stop YARN app directly.");
        } else {
            log.warn("No SubtaskInstance record found for correlationId: {} when trying to stop dsInstanceId: {}.", correlationId, dsInstanceId);
        }
        return dsStopApiCallSuccess;
    }

    @Transactional
    public boolean stopInstanceByCorrelationId(String correlationId) {
        if (!StringUtils.hasText(correlationId)) {
            log.error("无法停止实例：correlationId 为空。");
            return false;
        }

        SubtaskInstance subtask = subtaskInstanceMapper.selectByCorrelationId(correlationId);
        if (subtask == null) {
            log.error("根据 correlationId: {} 未找到要停止的子任务记录。", correlationId);
            return false;
        }

        if (subtask.getDsInstanceId() != null) {
            return this.stopInstanceByDsInstanceId(subtask.getDsInstanceId().longValue(), correlationId);
        } else {
            if (SubtaskInstance.STATUS_QUEUED.equals(subtask.getSubmissionStatus()) ||
                    SubtaskInstance.STATUS_RETRY_SCHEDULED.equals(subtask.getSubmissionStatus())) {

                boolean dequeued = this.tryDequeueTask(correlationId);
                if (dequeued) {
                    log.info("子任务 (correlationId: {}) 在队列中被成功取消。", correlationId);
                    subtask.setSubmissionStatus(SubtaskInstance.STATUS_STOPPED); // 修改为新状态
                    subtask.setFinalDsStatus("USER_CANCELLED_IN_QUEUE");
                    subtask.setErrorMessage("用户在任务排队时取消 (correlationId: " + correlationId + ")");
                    subtask.setUpdateTime(new Date());

                    subtaskInstanceMapper.updateById(subtask);

                    if (taskInstanceService != null) {
                        taskInstanceService.updateParentTaskInstanceStatus(subtask.getTaskInstanceId());
                    }
                    return true;
                } else {
                    log.warn("尝试从队列取消子任务 (correlationId: {}) 失败，可能任务已经开始处理。", correlationId);
                    return false;
                }
            }

            log.warn("无法停止子任务 (correlationId: {}): dsInstanceId 为空且任务状态不是可取消的排队状态 (当前状态: {})。",
                    correlationId, subtask.getSubmissionStatus());
            this.updateSubmissionLogError(correlationId, "停止失败：dsInstanceId 为空且任务不在队列中。");
            return false;
        }
    }

    public Boolean rerunInstanceByDsInstanceId(Long dsInstanceId) {
        if (dsInstanceId == null) {
            log.error("Cannot rerun instance: dsInstanceId is null.");
            return false;
        }
        log.info("Requesting to rerun DS instanceId directly: {}", dsInstanceId);
        return dolphinClient.opsForProcessInst().reRun(dolphinProperties.getProjectCode(), dsInstanceId);
    }
    /**
     * 重跑子任务实例的唯一方法。
     * 该方法采用“应用级重跑”模式：彻底重置任务状态，并将其作为全新任务重新放入执行队列。
     *
     * @param correlationId 子任务的唯一关联ID
     * @return 操作是否成功接受
     */
    @Transactional
    public boolean rerunInstanceByCorrelationId(String correlationId) {
        if (!StringUtils.hasText(correlationId)) {
            log.error("无法重跑子任务：correlationId 为空。");
            throw new TaskException(TaskErrorCode.TASK_PARAMETER_PARSED_ERROR, "用于重跑的CorrelationId为空");
        }

        SubtaskInstance subtask = subtaskInstanceMapper.selectByCorrelationId(correlationId);
        if (subtask == null) {
            log.error("根据 correlationId: {} 未找到要重跑的子任务记录。", correlationId);
            throw new TaskException(TaskErrorCode.TASK_NOT_FOUND, "用于重跑的子任务记录 (correlationId: " + correlationId + ") 未找到。");
        }

        // 1. 停止可能残留的旧YARN应用
        if (StringUtils.hasText(subtask.getYarnApplicationId())) {
            log.info("在重跑前，尝试停止旧的YARN应用 {} (correlationId: {})", subtask.getYarnApplicationId(), correlationId);
            stopYarnApplication(subtask.getYarnApplicationId());
        }

        // 2. 【关键修复】使用 LambdaUpdateWrapper 强制将字段更新为NULL
        log.info("为重跑准备子任务记录，使用UpdateWrapper强制重置所有状态 (correlationId: {})", correlationId);
        LambdaUpdateWrapper<SubtaskInstance> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SubtaskInstance::getId, subtask.getId()) // 定位要更新的记录
                .set(SubtaskInstance::getSubmissionStatus, SubtaskInstance.STATUS_QUEUED)
                .set(SubtaskInstance::getDsInstanceId, null)       // 强制设为 null
                .set(SubtaskInstance::getYarnApplicationId, null)  // 强制设为 null
                .set(SubtaskInstance::getFinalDsStatus, null)      // 强制设为 null
                .set(SubtaskInstance::getRetryCount, 0)
                .set(SubtaskInstance::getErrorMessage, "由用户请求重跑 (correlationId: " + correlationId + ")");

        // 执行强制更新，传入的第一个参数为null表示不使用实体类填充，完全依赖Wrapper
        subtaskInstanceMapper.update(null, updateWrapper);

        // 3. 构建新的请求并重新入队
        // 为了确保后续逻辑获取到的是最新的状态，可以重新查询一次记录
        SubtaskInstance updatedSubtask = subtaskInstanceMapper.selectById(subtask.getId());
        TaskSubmissionRequest rerunRequest = this.buildRequestFromLog(updatedSubtask);
        if (rerunRequest == null) {
            log.error("根据日志构建重跑请求失败 (correlationId: {})", correlationId);
            throw new TaskException(TaskErrorCode.TASK_START_ERROR, "构建重跑请求失败 " + correlationId);
        }

        this.enqueueTask(rerunRequest);
        log.info("已成功将重跑请求重新入队 (correlationId: {})", correlationId);

        // 4. 更新父任务状态，让其从终态变为运行中
        if (taskInstanceService != null) {
            taskInstanceService.updateParentTaskInstanceStatus(subtask.getTaskInstanceId());
        }
        return true;
    }

    public ProcessInstanceLogResp getLogsByDsInstanceId(Long workflowInstanceId, Long limit, Long skipLineNum) {
        if (workflowInstanceId == null) {
            log.error("Cannot get log: workflowInstanceId is null.");
            ProcessInstanceLogResp emptyResp = new ProcessInstanceLogResp();
            emptyResp.setMessage("Error: workflowInstanceId is null.");
            return emptyResp;
        }

        log.info("Fetching logs for DS workflow instance: {}", workflowInstanceId);
        try {
            Long projectCode = dolphinProperties.getProjectCode();

            ProcessInstanceTasksQueryResp tasksInWorkflow = dolphinClient.opsForProcessInst().fetchTasksForProcessInstance(projectCode, workflowInstanceId.intValue());

            if (tasksInWorkflow == null || tasksInWorkflow.getTaskList() == null || tasksInWorkflow.getTaskList().isEmpty()) {
                log.warn("No task instances found within workflow instance: {}", workflowInstanceId);
                ProcessInstanceLogResp noTaskResp = new ProcessInstanceLogResp();
                noTaskResp.setMessage("Error: No task nodes found in the workflow instance.");
                return noTaskResp;
            }

            if (tasksInWorkflow.getTaskList().size() > 1) {
                log.warn("Expected one task node in workflow instance {} but found {}. Proceeding with the first one.",
                        workflowInstanceId, tasksInWorkflow.getTaskList().size());
            }
            ProcessInstanceTasksQueryResp.Task taskInstance = tasksInWorkflow.getTaskList().get(0);
            Long taskInstanceId = taskInstance.getId();

            log.info("Found task instance ID: {} for workflow instance ID: {}", taskInstanceId, workflowInstanceId);

            return dolphinClient.opsForProcessInst().log(taskInstanceId, limit, skipLineNum);

        } catch (Exception e) {
            log.error("Failed to get logs for DS workflow instance: {}", workflowInstanceId, e);
            ProcessInstanceLogResp errorResp = new ProcessInstanceLogResp();
            errorResp.setMessage("Error fetching logs: " + e.getMessage());
            return errorResp;
        }
    }

    public ProcessInstanceQueryResp getInstanceDetailsFromDs(Long dsInstanceId) {
        if (dsInstanceId == null) {
            log.warn("无法获取DS实例详情：dsInstanceId 为空。");
            return null;
        }

        try {
            Long projectCode = dolphinProperties.getProjectCode();
            log.info("开始从项目 {} 中获取DS实例 {} 的详情...", projectCode, dsInstanceId);

            ProcessInstanceQueryResp instanceDetails = dolphinClient.opsForProcessInst().getById(projectCode, dsInstanceId);

            if (instanceDetails == null) {
                log.warn("未找到ID为 {} 的DS实例。", dsInstanceId);
            }

            return instanceDetails;

        } catch (Exception e) {
            log.error("获取DS实例 {} 详情时发生异常: {}", dsInstanceId, e.getMessage(), e);
            throw new DolphinException("根据ID获取DS流程实例失败", e);
        }
    }


    public Boolean pauseInstanceByDsInstanceId(Long dsInstanceId) {
        if(dsInstanceId == null) return false;
        log.info("Requesting to pause DS instanceId: {}", dsInstanceId);
        return dolphinClient.opsForProcessInst().pause(dolphinProperties.getProjectCode(), dsInstanceId);
    }

    @Transactional
    public Boolean deleteInstanceByDsInstanceId(Long dsInstanceId) {
        if (dsInstanceId == null) return false;
        log.info("Requesting to delete DS instanceId: {}", dsInstanceId);
        boolean deletedFromDs = dolphinClient.opsForProcessInst().delete(dolphinProperties.getProjectCode(), dsInstanceId);
        if (deletedFromDs) {
            SubtaskInstance subtaskRecord = subtaskInstanceMapper.selectOne(
                    new QueryWrapper<SubtaskInstance>().eq("ds_instance_id", dsInstanceId)
            );
            if (subtaskRecord != null) {
                subtaskRecord.setSubmissionStatus("DELETED_FROM_DS");
                subtaskRecord.setFinalDsStatus("DELETED");
                subtaskRecord.setErrorMessage("Instance deleted from DolphinScheduler.");
                subtaskRecord.setUpdateTime(new Date());
                subtaskInstanceMapper.updateById(subtaskRecord);
            }
        }
        return deletedFromDs;
    }

    public boolean stopYarnApplication(String yarnApplicationId) {
        if (yarnApplicationId == null || yarnApplicationId.isEmpty()) {
            log.warn("YARN Application ID is null or empty, cannot stop.");
            return false;
        }

        String apiUrl = yarnRmBaseUrl + "/ws/v1/cluster/apps/" + yarnApplicationId + "/state";
        log.info("Attempting to kill YARN app {} via REST API to {}", yarnApplicationId, apiUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("state", "KILLED");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
                log.info("Successfully sent KILL command to YARN app {}. Response status: {}, Body: {}",
                        yarnApplicationId, response.getStatusCode(), response.getBody());
                return true;
            } else {
                log.error("Failed to kill YARN app {}. Status: {}, Body: {}",
                        yarnApplicationId, response.getStatusCode(), response.getBody());
                return false;
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP error when trying to kill YARN app {}: {} - {}",
                    yarnApplicationId, e.getStatusCode(), e.getResponseBodyAsString(), e);
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("YARN app {} not found on ResourceManager. It might have already finished or been killed.", yarnApplicationId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Generic exception when trying to kill YARN app {}: {}", yarnApplicationId, e.getMessage(), e);
            return false;
        }
    }

    private void updateSubmissionLogError(String correlationId, String errorMessage) {
        if (correlationId == null) return;
        SubtaskInstance subtask = subtaskInstanceMapper.selectByCorrelationId(correlationId);
        if (subtask != null) {
            updateLogError(subtask, subtask.getSubmissionStatus(), errorMessage, null);
        }
    }
    /**
     * 手动触发指定子任务的Yarn Application ID获取流程。
     * @param correlationId 子任务的唯一关联ID
     */
    public void triggerYarnAppIdFetch(String correlationId) {
        if (!StringUtils.hasText(correlationId)) {
            log.error("无法触发Yarn App ID获取：correlationId为空。");
            throw new TaskException(TaskErrorCode.TASK_PARAMETER_PARSED_ERROR, "用于刷新的correlationId为空");
        }

        SubtaskInstance subtask = subtaskInstanceMapper.selectByCorrelationId(correlationId);
        if (subtask == null) {
            log.error("无法触发Yarn App ID获取：根据correlationId {} 未找到子任务。", correlationId);
            throw new TaskException(TaskErrorCode.TASK_NOT_FOUND, "未找到correlationId为 " + correlationId + " 的子任务。");
        }

        if (subtask.getDsInstanceId() == null) {
            log.warn("无法为correlationId {} 触发Yarn App ID获取：ds_instance_id为空。", correlationId);
            throw new TaskException(TaskErrorCode.TASK_LOG_GOT_FAILED, "该子任务的DS实例ID尚不可用。");
        }

        log.info("手动触发Yarn App ID获取，correlationId: {}", correlationId);
        // 复用现有逻辑，将其提交到日志解析线程池中异步执行
        logParsingExecutor.submit(() -> fetchAndStoreYarnAppIdLogic(
                subtask.getId(),
                subtask.getDsInstanceId(),
                correlationId,
                3, 3000L, "[ManualYarnAppIdFetch]"));
    }
    /**
     * 删除单个DS实例记录（从DS中）- 增加了状态检查的健壮版本
     */
    @Transactional
    public Boolean deleteInstanceByCorrelationId(String correlationId) {
        SubtaskInstance subtask = subtaskInstanceMapper.selectByCorrelationId(correlationId);
        if (subtask == null) {
            log.warn("Cannot delete subtask by correlationId {}, record not found. Assuming success.", correlationId);
            return true; // 如果记录本就不在，也视为成功
        }

        String status = subtask.getSubmissionStatus();

        // 检查是否处于运行中或提交中的状态
        if (SubtaskInstance.STATUS_TRACKING.equals(status) || SubtaskInstance.STATUS_SUBMITTING.equals(status)) {
            log.error("Cannot delete running task. CorrelationId: {}, Status: {}", correlationId, status);
            throw new TaskException(TaskErrorCode.TASK_INSTANCE_EXISTED, "任务正在运行中，请先停止任务再执行删除操作。");
        }

        // 如果任务在队列中，尝试移除它
        if (SubtaskInstance.STATUS_QUEUED.equals(status) || SubtaskInstance.STATUS_RETRY_SCHEDULED.equals(status)) {
            boolean dequeued = this.tryDequeueTask(correlationId);
            if (!dequeued) {
                // 如果在检查和删除的间隙，任务已经被调度器取出，则认为它正在运行
                throw new TaskException(TaskErrorCode.TASK_INSTANCE_EXISTED, "任务正在被调度，请稍后重试或先停止任务。");
            }
        }

        // 对于所有终态或可安全移除的任务，执行删除
        boolean deletedFromDs = true;
        if (subtask.getDsInstanceId() != null) {
            log.info("Requesting to delete DS instanceId: {} for correlationId: {}", subtask.getDsInstanceId(), correlationId);
            try {
                deletedFromDs = dolphinClient.opsForProcessInst().delete(dolphinProperties.getProjectCode(), subtask.getDsInstanceId().longValue());
            } catch (Exception e) {
                // 即使DS删除失败（例如DS中已不存在），也继续删除本地记录
                log.warn("Failed to delete instance from DolphinScheduler (dsInstanceId: {}), but proceeding to delete local record. Error: {}", subtask.getDsInstanceId(), e.getMessage());
            }
        }

        // 无论DS上是否存在或删除是否成功，都删除本地记录（因为任务已处于终态或被移出队列）
        subtaskInstanceMapper.deleteById(subtask.getId());
        log.info("Deleted subtask record from local DB for correlationId: {}", correlationId);

        // 删除后，也需要更新父实例的状态
        if (taskInstanceService != null) {
            taskInstanceService.updateParentTaskInstanceStatus(subtask.getTaskInstanceId());
        }

        return deletedFromDs;
    }
    // ============== 新增方法：通过 CorrelationId 获取日志 ==============
    /**
     * 根据应用的 correlationId 获取子任务的DolphinScheduler日志。
     * @param correlationId 子任务的唯一关联ID
     * @param limit 日志行数
     * @param skipLineNum 跳过的行数
     * @return 日志响应体
     */
    public ProcessInstanceLogResp getLogsByCorrelationId(String correlationId, Long limit, Long skipLineNum) {
        if (!StringUtils.hasText(correlationId)) {
            throw new TaskException(TaskErrorCode.TASK_PARAMETER_PARSED_ERROR, "用于获取日志的CorrelationId为空");
        }
        SubtaskInstance subtask = subtaskInstanceMapper.selectByCorrelationId(correlationId);
        if (subtask == null || subtask.getDsInstanceId() == null) {
            throw new TaskException(TaskErrorCode.TASK_LOG_GOT_FAILED, "未找到任务或任务尚未提交到DS，无法获取日志。");
        }

        Integer processInstanceId = subtask.getDsInstanceId();
        try {
            Long projectCode = dolphinProperties.getProjectCode();

            // DS的日志是根据TaskInstanceId获取的，而不是ProcessInstanceId。
            // 我们需要先找到工作流实例中对应的任务实例。
            ProcessInstanceTasksQueryResp tasksInWorkflow = dolphinClient.opsForProcessInst().fetchTasksForProcessInstance(projectCode, processInstanceId);

            if (tasksInWorkflow == null || tasksInWorkflow.getTaskList() == null || tasksInWorkflow.getTaskList().isEmpty()) {
                log.warn("在工作流实例 {} 中未找到任何任务节点。", processInstanceId);
                throw new TaskException(TaskErrorCode.TASK_LOG_GOT_FAILED, "在DS工作流中未找到对应的任务节点。");
            }

            // 假设每个子任务对应的工作流中只有一个任务节点。
            if (tasksInWorkflow.getTaskList().size() > 1) {
                log.warn("工作流实例 {} 中有多个任务节点，将默认使用第一个节点的日志。", processInstanceId);
            }
            ProcessInstanceTasksQueryResp.Task taskInstance = tasksInWorkflow.getTaskList().get(0);
            Long taskInstanceId = taskInstance.getId();

            log.info("为 [correlationId={}] 查找到对应的DS任务实例ID: {}，开始获取日志。", correlationId, taskInstanceId);
            return dolphinClient.opsForProcessInst().log(taskInstanceId, limit, skipLineNum);

        } catch (Exception e) {
            log.error("为 [correlationId={}] 获取日志失败: {}", correlationId, e.getMessage(), e);
            throw new TaskException(TaskErrorCode.TASK_LOG_GOT_FAILED, "获取DS日志时发生异常: " + e.getMessage());
        }
    }


    // ============== 新增方法：通过 CorrelationId 获取DS实例详情 ==============
    /**
     * 根据应用的 correlationId 获取DolphinScheduler实例的详细信息。
     * @param correlationId 子任务的唯一关联ID
     * @return DS实例详情
     */
    public ProcessInstanceQueryResp getInstanceDetailsByCorrelationId(String correlationId) {
        if (!StringUtils.hasText(correlationId)) {
            throw new TaskException(TaskErrorCode.TASK_PARAMETER_PARSED_ERROR, "用于获取详情的CorrelationId为空");
        }

        SubtaskInstance subtask = subtaskInstanceMapper.selectByCorrelationId(correlationId);
        if (subtask == null || subtask.getDsInstanceId() == null) {
            throw new TaskException(TaskErrorCode.TASK_NOT_FOUND, "未找到任务或任务尚未提交到DS，无法获取详情。");
        }

        Long dsInstanceId = subtask.getDsInstanceId().longValue();

        try {
            Long projectCode = dolphinProperties.getProjectCode();
            log.info("开始从项目 {} 中获取DS实例 {} (correlationId={}) 的详情...", projectCode, dsInstanceId, correlationId);
            return dolphinClient.opsForProcessInst().getById(projectCode, dsInstanceId);
        } catch (Exception e) {
            log.error("获取DS实例 {} (correlationId={}) 详情时发生异常: {}", dsInstanceId, correlationId, e.getMessage(), e);
            throw new DolphinException("获取DS实例详情时发生异常: " + e.getMessage());
        }
    }

}