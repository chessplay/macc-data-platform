package com.ruijie.cloud.macc.dataplatform.task.domain.vo;



import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskInstanceDetailVo extends TaskInstanceSummaryVo {
    // 字段名是 subTasks (T大写)
    private List<SubTaskDetailVo> subTasks;
}