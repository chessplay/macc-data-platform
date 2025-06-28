package com.ruijie.cloud.macc.dataplatform.task.controller;


import com.ruijie.cloud.dc.core.controller.BaseController;
import com.ruijie.cloud.dc.core.domain.PageQuery;
import com.ruijie.cloud.dc.core.domain.R;
import com.ruijie.cloud.dc.core.page.TableDataInfo;
import com.ruijie.cloud.dc.core.validate.AddGroup;
import com.ruijie.cloud.dc.core.validate.EditGroup;
import com.ruijie.cloud.macc.dataplatform.task.domain.TaskTemplate;
import com.ruijie.cloud.macc.dataplatform.task.domain.bo.TaskTemplateBo;

import com.ruijie.cloud.macc.dataplatform.task.domain.vo.ParameterDescriptor;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.TaskTemplateVo;

import com.ruijie.cloud.macc.dataplatform.task.entity.TaskQueryDto;
import com.ruijie.cloud.macc.dataplatform.task.service.TaskTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * 评测任务
 *
 * @author zhouliwang
 * @date 2023-10-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/task/template")
public class TaskTemplateController extends BaseController {

    private final TaskTemplateService iTaskTemplateService;

    @GetMapping("/list")
    public TableDataInfo<TaskTemplateVo> list(TaskQueryDto bo, PageQuery pageQuery) {
        return iTaskTemplateService.queryPageList(bo, pageQuery);
    }

    @GetMapping()
    public R<TaskTemplateVo> getInfo(@NotNull(message = "主键不能为空")
                                 @RequestParam Long id) {
        return R.ok(iTaskTemplateService.queryById(id));
    }


    @PostMapping()
    public R<Boolean> add(@Validated(AddGroup.class) @RequestBody TaskTemplateBo bo) {
        return R.ok(iTaskTemplateService.insertByBo(bo));
    }


    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TaskTemplateBo bo) {
        return toAjax(iTaskTemplateService.updateByBo(bo));
    }


    @DeleteMapping()
    public R<Void> remove(@NotNull (message = "主键不能为空")
                          @RequestParam Long[] ids) {
        return toAjax(iTaskTemplateService.deleteWithValidById(ids, true));
    }

    @GetMapping("/fetch-request-example")
    public R<String> generateRequestExample(@NotNull(message = "模板id不能为空") @RequestParam Long id) {
        // 【修正】直接使用 queryById 获取 VO 对象即可，无需调用额外方法
        TaskTemplateVo templateVo = iTaskTemplateService.queryById(id);

        if (templateVo == null) {
            return R.fail("找不到ID为 " + id + " 的模板");
        }

        // 从 VO 对象中直接获取已正确反序列化的 startParams
        List<ParameterDescriptor> startParams = templateVo.getStartParams();

        // 调用服务层的方法，生成并返回示例字符串
        String example = iTaskTemplateService.generateRequestExample(id, startParams);

        return R.ok(example);
    }
    /**
     * [重点] 新增一个 POST 接口，专门用于根据前端实时的、未保存的表单值生成请求示例（实时预览）。
     *
     * @param currentFormParams 前端表单当前的所有值
     * @return 格式化后的 JSON 请求示例字符串
     */
    @PostMapping("/preview-request-example")
    public R<String> previewRequestExample(@RequestBody HashMap<String, Object> currentFormParams) {
        // 调用服务层的新方法来处理预览逻辑
        String previewJson = iTaskTemplateService.generatePreviewRequestExample(currentFormParams);
        return R.ok(previewJson);
    }

    @GetMapping("/{id}/params")
    public R<List<ParameterDescriptor>> getTemplateParams(@PathVariable Long id) {
        TaskTemplateVo templateVo = iTaskTemplateService.queryById(id);
        if (templateVo == null) {
            return R.fail("找不到ID为 " + id + " 的模板");
        }
        return R.ok(templateVo.getStartParams());
    }



}
