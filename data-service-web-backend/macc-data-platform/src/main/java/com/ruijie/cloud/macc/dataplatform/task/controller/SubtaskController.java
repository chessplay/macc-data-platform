package com.ruijie.cloud.macc.dataplatform.task.controller;



import com.ruijie.cloud.dc.core.controller.BaseController;
import com.ruijie.cloud.dc.core.domain.PageQuery;
import com.ruijie.cloud.dc.core.domain.R;
import com.ruijie.cloud.dc.core.page.TableDataInfo;
import com.ruijie.cloud.dc.privilege.core.IgnoreAuth;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.SubTaskDetailVo;
import com.ruijie.cloud.macc.dataplatform.task.ds.instance.ProcessInstanceLogResp;
import com.ruijie.cloud.macc.dataplatform.task.ds.instance.ProcessInstanceQueryResp;
// 1. [依赖变更] 引入新的核心服务 TaskSubmissionService
import com.ruijie.cloud.macc.dataplatform.task.entity.SubTaskQueryDto;
import com.ruijie.cloud.macc.dataplatform.task.service.TaskSubmissionService;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor

@RestController
@RequestMapping("/task/subtask")
public class SubtaskController extends BaseController {


    private final TaskSubmissionService taskSubmissionService;

    /**
     * 停止单个子任务实例
     * @param correlationId 子任务的唯一关联ID
     * @return R<Void>
     */
    @PutMapping("/{correlationId}/stop")
    public R<Void> stopSubtask(@PathVariable String correlationId) {
        // 调用已合并了相关功能的 TaskSubmissionService
        boolean success = taskSubmissionService.stopInstanceByCorrelationId(correlationId);
        return success ? R.ok("停止请求已发送。") : R.fail("停止任务失败。");
    }



    /**
     * 通过应用的逻辑重跑子任务 (推荐)
     * @param correlationId 子任务的Correlation ID
     * @return R<Void>
     */
    @PutMapping("/rerun-by-correlation/{correlationId}")
    public R<Void> rerunSubtaskViaApp(@PathVariable String correlationId) {
        try {
            taskSubmissionService.rerunInstanceByCorrelationId(correlationId); // false表示通过应用队列重跑
            return R.ok("子任务已重新排队执行。");
        } catch (Exception e) {
            return R.fail("重跑子任务失败: " + e.getMessage());
        }
    }



    /**
     * 删除单个子任务实例记录 (从DS中)
     * @param correlationIds 要删除的子任务的Correlation ID数组
     * @return R<Void>
     */
    @DeleteMapping
    public R<Void> deleteSubtasks(@RequestParam String[] correlationIds) {
        boolean allSucceeded = true;
        for (String id : correlationIds) {
            if (!taskSubmissionService.deleteInstanceByCorrelationId(id)) {
                allSucceeded = false;
            }
        }
        return allSucceeded ? R.ok() : R.fail("部分或全部实例删除失败。");
    }

    /**
     * 获取单个子任务实例的日志
     * @param correlationId 子任务的唯一关联ID
     * @param limit 日志行数限制
     * @param skipLineNum 跳过的起始行数
     * @return R<ProcessInstanceLogResp> 日志内容
     */
    @GetMapping("/{correlationId}/log")
    public R<ProcessInstanceLogResp> getSubtaskLog(
            @PathVariable String correlationId,
            @RequestParam(required = false) Long limit,
            @RequestParam(required = false) Long skipLineNum) {
        return R.ok(taskSubmissionService.getLogsByCorrelationId(correlationId, limit, skipLineNum));
    }

    /**
     * 获取单个子任务实例的详细信息 (从DS API)
     * @param correlationId 子任务的唯一关联ID
     * @return R<ProcessInstanceQueryResp>
     */
    @GetMapping("/{correlationId}/details")
    public R<ProcessInstanceQueryResp> getSubtaskDetailsFromDs(@PathVariable String correlationId) {
        ProcessInstanceQueryResp details = taskSubmissionService.getInstanceDetailsByCorrelationId(correlationId);
        return details != null ? R.ok(details) : R.fail("未能获取到实例详情。");
    }
    @IgnoreAuth
    @GetMapping("/page")
    public TableDataInfo<SubTaskDetailVo> listSubtasks(SubTaskQueryDto dto, PageQuery pageQuery) {
        // 调用 Service 层的方法来获取数据
        // 注意：这个 pageSubTaskDetails 方法现在需要由 TaskInstanceService 提供，
        // 或者您也可以将其逻辑移动到 TaskSubmissionService 中，以保持职责统一。
        // 假设我们将其保留在 TaskInstanceService 中。
        return taskSubmissionService.pageSubTaskDetails(dto, pageQuery);
    }
    /**
     * 手动触发刷新指定子任务的Yarn Application ID
     * @param correlationId 子任务的唯一关联ID
     * @return R<Void>
     */
    @PutMapping("/refresh-yarn-id/{correlationId}")
    public R<Void> refreshYarnAppId(@PathVariable String correlationId) {
        taskSubmissionService.triggerYarnAppIdFetch(correlationId);
        return R.ok("Yarn App ID刷新请求已触发，请稍后查看结果。");
    }

}