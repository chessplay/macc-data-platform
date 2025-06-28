package com.ruijie.cloud.macc.dataplatform.task.mapper.ds;



import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruijie.cloud.macc.dataplatform.task.domain.DsProcessInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 用于查询 DolphinScheduler 数据库中 t_ds_process_instance 表的 Mapper 接口
 * 注意：这个 Mapper 会使用名为 "dsSqlSessionTemplate" 的 SqlSessionTemplate
 */
@Mapper
@DS("ds")
public interface DsProcessInstanceMapper extends BaseMapper<DsProcessInstance> {

    /**
     * 查询指定工作流定义 Code 且在某个时间点之后启动的实例列表，按启动时间降序排列。
     * @param processDefinitionCode 工作流定义 Code
     * @param startTimeAfter        查询此时间点之后的实例 (>=)
     * @param limit                 限制返回的记录数
     * @return 实例列表
     */
    @Select("SELECT id, process_definition_code, command_param, start_time " +
            "FROM t_ds_process_instance " + // **确认实际表名**
            "WHERE process_definition_code = #{processDefinitionCode} " +
            "  AND start_time >= #{startTimeAfter} " +
            "ORDER BY start_time DESC " +
            "LIMIT #{limit}"
    )
    List<DsProcessInstance> findRecentInstances(@Param("processDefinitionCode") Long processDefinitionCode,
                                                @Param("startTimeAfter") Date startTimeAfter,
                                                @Param("limit") int limit);

    // 可以根据需要添加其他查询方法，例如直接按 ID 查询
    @Select("SELECT id, process_definition_code, command_param, start_time, state " +
            "FROM t_ds_process_instance " +
            "WHERE id = #{instanceId} ")
    DsProcessInstance findById(@Param("instanceId") Integer instanceId);
}