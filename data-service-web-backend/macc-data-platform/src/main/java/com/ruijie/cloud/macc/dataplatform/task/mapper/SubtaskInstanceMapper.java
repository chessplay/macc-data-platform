package com.ruijie.cloud.macc.dataplatform.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruijie.cloud.dc.core.mapper.BaseMapperPlus;
import com.ruijie.cloud.macc.dataplatform.task.domain.SubtaskInstance;
import com.ruijie.cloud.macc.dataplatform.task.domain.vo.SubTaskDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 子任务实例 (SubtaskInstance) 的 Mapper 接口。
 * 由原 TaskSubmissionLogMapper 重构而来，使用注解定义SQL。
 */
@Mapper
public interface SubtaskInstanceMapper extends BaseMapperPlus<SubtaskInstanceMapper,SubtaskInstance, SubTaskDetailVo> {

    /**
     * 查询所有未完成的子任务，用于服务重启后的恢复。
     * [改动点] 表名从 mdp_task_submission_log 更新为 mdp_subtask_instance。
     * @return 未完成的子任务列表
     */
    @Select("SELECT * FROM mdp_subtask_instance WHERE submission_status IN ('QUEUED', 'SUBMITTING', 'TRACKING', 'RETRY_SCHEDULED')")
    List<SubtaskInstance> findUnfinishedSubtasks();

    /**
     * 根据关联ID查询子任务。
     * [改动点] 表名从 mdp_task_submission_log 更新为 mdp_subtask_instance。
     * @param correlationId 关联ID
     * @return 子任务实例
     */
    @Select("SELECT * FROM mdp_subtask_instance WHERE correlation_id = #{correlationId}")
    SubtaskInstance selectByCorrelationId(@Param("correlationId") String correlationId);
}