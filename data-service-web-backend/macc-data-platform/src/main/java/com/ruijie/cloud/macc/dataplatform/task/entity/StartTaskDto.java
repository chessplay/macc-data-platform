package com.ruijie.cloud.macc.dataplatform.task.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2023/12/11 9:48
 **/
@Data
public class StartTaskDto {
    @NotNull(message = "项目码能不能为空")
    private Long processCode;
    @NotNull
    private String startParams;

    private String dataset;
}
