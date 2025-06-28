package com.ruijie.cloud.macc.dataplatform.task.controller;

import com.ruijie.cloud.dc.core.controller.BaseController;
import com.ruijie.cloud.dc.core.domain.R;
import com.ruijie.cloud.dc.privilege.core.IgnoreAuth;
import com.ruijie.cloud.macc.dataplatform.security.RequireAccessKey;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.TaskInstanceDetailVo;
import com.ruijie.cloud.macc.dataplatform.task.ds.instance.ProcessInstanceLogResp;
import com.ruijie.cloud.macc.dataplatform.task.service.TaskInstanceService;
import com.ruijie.cloud.macc.dataplatform.task.service.TaskSubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/task/public")
public class TaskOpenApiController extends BaseController {

    private final TaskInstanceService taskInstanceService;
    private final TaskSubmissionService taskSubmissionService;

    /**
     * OpenAPI: 启动一个新的任务实例
     * @param templateId 任务模板ID
     * @param accessKey 访问密钥
     * @param taskParamsWrapper 任务参数
     * @return R<String> 包含新创建的任务实例ID (instanceId)
     */
    @RequireAccessKey
    @IgnoreAuth
    @PostMapping("/instance/start")
    public R<String> startNewTaskInstance(@RequestParam("id") Long templateId,
                                          @RequestParam(value = "access_key") String accessKey,
                                          @RequestBody HashMap<String, HashMap<String, Object>> taskParamsWrapper) {
        String instanceId = taskInstanceService.startNewTaskInstance(templateId, taskParamsWrapper);
        return R.ok(instanceId, "任务实例已提交，实例ID: " + instanceId);
    }

    /**
     * OpenAPI: 获取指定任务实例的详细信息
     * @param instanceId 任务实例ID
     * @param accessKey 访问密钥
     * @return TaskInstanceDetailVo 实例详情
     */
    @RequireAccessKey
    @IgnoreAuth
    @GetMapping("/instance/{instanceId}")
    public R<TaskInstanceDetailVo> getTaskInstanceDetails(@PathVariable String instanceId,
                                                          @RequestParam(value = "access_key") String accessKey) {
        TaskInstanceDetailVo detailVo = taskInstanceService.getTaskInstanceDetails(instanceId);
        return R.ok(detailVo);
    }

    /**
     * OpenAPI: 停止整个任务实例
     * @param instanceId 任务实例ID
     * @param accessKey 访问密钥
     * @return R<Void>
     */
    @RequireAccessKey
    @IgnoreAuth
    @PutMapping("/instance/{instanceId}/stop")
    public R<Void> stopTaskInstance(@PathVariable String instanceId,
                                    @RequestParam(value = "access_key") String accessKey) {
        boolean success = taskInstanceService.stopTaskInstance(instanceId);
        return success ? R.ok("停止任务实例的请求已发送。") : R.fail("停止请求失败。");
    }

    // --- 以下是针对单个“子任务”的操作 ---

    /**
     * OpenAPI: 停止单个子任务
     * @param correlationId 子任务的唯一关联ID
     * @param accessKey 访问密钥
     * @return R<Void>
     */
    @RequireAccessKey
    @IgnoreAuth
    // [修改点 1] 路径中的标识符从 {dsInstanceId} 改为 {correlationId}
    @PutMapping("/subtask/{correlationId}/stop")
    public R<Void> stopSubtask(@PathVariable String correlationId,
                               @RequestParam(value = "access_key") String accessKey) {
        // [修改点 2] 调用 service 中基于 correlationId 的方法
        boolean success = taskSubmissionService.stopInstanceByCorrelationId(correlationId);
        return success ? R.ok("停止子任务的请求已发送。") : R.fail("停止子任务操作失败。");
    }

    /**
     * OpenAPI: 通过应用逻辑重跑子任务
     * @param correlationId 子任务的Correlation ID
     * @param accessKey 访问密钥
     * @return R<Void>
     */
    @RequireAccessKey
    @IgnoreAuth
    // [修改点 3] 统一路径风格，使其更符合 RESTful
    @PutMapping("/subtask/{correlationId}/rerun")
    public R<Void> rerunSubtaskByCorrelationId(@PathVariable String correlationId,
                                               @RequestParam(value = "access_key") String accessKey) {
        try {
            taskSubmissionService.rerunInstanceByCorrelationId(correlationId);
            return R.ok("子任务已重新排队执行。");
        } catch (Exception e) {
            return R.fail("重跑子任务失败: " + e.getMessage());
        }
    }

    /**
     * OpenAPI: 获取单个子任务的日志
     * @param correlationId 子任务的唯一关联ID
     * @param accessKey 访问密钥
     * @return R<ProcessInstanceLogResp>
     */
    @RequireAccessKey
    @IgnoreAuth
    // [修改点 4] 路径中的标识符从 {dsInstanceId} 改为 {correlationId}
    @GetMapping("/subtask/{correlationId}/log")
    public R<ProcessInstanceLogResp> getSubtaskLog(
            @PathVariable String correlationId,
            @RequestParam(value = "access_key") String accessKey,
            @RequestParam(required = false) Long limit,
            @RequestParam(required = false) Long skipLineNum) {
        // [修改点 5] 调用 service 中基于 correlationId 的新方法
        return R.ok(taskSubmissionService.getLogsByCorrelationId(correlationId, limit, skipLineNum));
    }
}