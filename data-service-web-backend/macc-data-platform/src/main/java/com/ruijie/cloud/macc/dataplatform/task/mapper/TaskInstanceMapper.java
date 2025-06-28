package com.ruijie.cloud.macc.dataplatform.task.mapper;




import com.ruijie.cloud.dc.core.mapper.BaseMapperPlus;
import com.ruijie.cloud.macc.dataplatform.task.domain.TaskInstance;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.TaskInstanceSummaryVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务实例 (TaskInstance) 的 Mapper 接口
 */
@Mapper
public interface TaskInstanceMapper extends BaseMapperPlus<TaskInstanceMapper, TaskInstance, TaskInstanceSummaryVo> {
    // BaseMapperPlus 提供了常用的 CRUD 和分页查询能力
    // 如果有特殊的自定义查询，可以在这里添加方法，并在对应的 XML 中实现
}