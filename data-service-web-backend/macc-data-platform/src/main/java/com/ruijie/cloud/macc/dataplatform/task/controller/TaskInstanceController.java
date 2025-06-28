package com.ruijie.cloud.macc.dataplatform.task.controller;

import com.ruijie.cloud.dc.core.controller.BaseController;
import com.ruijie.cloud.dc.core.domain.PageQuery;
import com.ruijie.cloud.dc.core.domain.R;
import com.ruijie.cloud.dc.core.page.TableDataInfo;
import com.ruijie.cloud.dc.privilege.core.IgnoreAuth;
// [改动点] 引入重命名后的 DTO 和 VO
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.SubTaskDetailVo;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.TaskInstanceSummaryVo;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.TaskInstanceDetailVo;
import com.ruijie.cloud.macc.dataplatform.task.entity.SubTaskQueryDto;
import com.ruijie.cloud.macc.dataplatform.task.entity.TaskInstanceQueryDto;
// [改动点] 引入重命名后的 Service
import com.ruijie.cloud.macc.dataplatform.task.service.TaskInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Validated
@RequiredArgsConstructor
// 1. [类名和路径变更] 类名和API路径都已更新
@RestController
@RequestMapping("/task/instance")
public class TaskInstanceController extends BaseController {

    // 2. [依赖变更] 注入新的 TaskInstanceService
    private final TaskInstanceService taskInstanceService;

    /**
     * 启动一个新的任务实例 (Job)
     * @param templateId 任务模板ID
     * @param taskParamsWrapper 包含 "taskParams": { ... } 的请求体
     * @return R<String> 包含 instanceId
     */
    @PostMapping("/start")
    public R<String> startNewTaskInstance(@RequestParam("id") Long templateId, // 参数名保持'id'与前端一致
                                          @RequestBody HashMap<String, HashMap<String, Object>> taskParamsWrapper) {
        // 3. [方法调用变更] 调用新的 service 方法
        String instanceId = taskInstanceService.startNewTaskInstance(templateId, taskParamsWrapper);
        return R.ok(instanceId, "任务实例已提交，实例ID: " + instanceId);
    }

    /**
     * 分页查询任务实例列表
     * @param dto       查询参数
     * @param pageQuery 分页参数
     * @return TableDataInfo<TaskInstanceSummaryVo>
     */
    @IgnoreAuth
    @GetMapping("/page")
    // 4. [DTO/VO变更] 使用重命名后的 DTO 和 VO
    public TableDataInfo<TaskInstanceSummaryVo> listTaskInstances(TaskInstanceQueryDto dto, PageQuery pageQuery) {
        return taskInstanceService.pageTaskInstances(dto, pageQuery);
    }


    /**
     * 获取指定任务实例的详细信息
     * @param instanceId 任务实例的ID
     * @return TaskInstanceDetailVo 实例详情
     */
    @GetMapping("/{instanceId}")
    // 5. [路径变量和VO变更]
    public R<TaskInstanceDetailVo> getTaskInstanceDetails(@PathVariable String instanceId) {
        TaskInstanceDetailVo detailVo = taskInstanceService.getTaskInstanceDetails(instanceId);
        return R.ok(detailVo);
    }

    /**
     * 停止整个任务实例下的所有活动子任务
     * @param instanceId 任务实例ID
     * @return R<Void>
     */
    @PutMapping("/{instanceId}/stop")
    public R<Void> stopTaskInstance(@PathVariable String instanceId) {
        boolean success = taskInstanceService.stopTaskInstance(instanceId);
        return success ? R.ok("停止任务实例的请求已发送。") : R.fail("停止请求部分或全部失败，请检查详情。");
    }

    /**
     * 重跑任务实例中所有失败的子任务
     * @param instanceId 任务实例ID
     * @return R<Void>
     */
    @PutMapping("/{instanceId}/rerun-failed")
    public R<Void> rerunFailedSubtasksInInstance(@PathVariable String instanceId) {
        boolean success = taskInstanceService.rerunFailedSubtasksInInstance(instanceId);
        return success ? R.ok("重跑失败子任务的请求已发送。") : R.fail("重跑请求部分或全部失败，请检查详情。");
    }


}