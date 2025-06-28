package com.ruijie.cloud.macc.dataplatform.task.domain.vo;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 任务启动参数的UI描述符.
 * 用于由后端驱动，动态生成前端的表单项.
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // 序列化时忽略null字段
public class ParameterDescriptor {

    /**
     * 参数的唯一名称/键 (e.g., "user_id")
     */
    private String name;

    /**
     * 在UI上显示的标签 (e.g., "用户ID")
     */
    private String label;

    /**
     * 建议前端渲染的组件类型.
     * (e.g., "input", "list-input", "select", "date-picker")
     */
    private String component;

    /**
     * 该参数是否为必填项
     */
    private boolean required;

    /**
     * 参数的默认值
     */
    private Object defaultValue;

    /**
     * 传递给前端组件的其他属性.
     * e.g., { "placeholder": "请输入...", "options": [ ... ] }
     */
    private Map<String, Object> props;

    private String dataType;
}
