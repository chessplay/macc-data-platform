package com.ruijie.cloud.macc.dataplatform.task.exception;


import com.ruijie.cloud.dc.core.response.ResultCode;
import org.omg.CORBA.UNKNOWN;

public enum TaskErrorCode implements ResultCode {

    METADATA_IS_NOT_EXISTED(10001, "元数据{}已过期或不存在,请刷新重新获取"),
    INDEX_OR_PRIMARY_KEY_MISSING(10002, "索引字段或者主键字段缺失,受到集群资源的限制,为了保障查询效率,需要传{}或其中之一"),
    COLUMN_NOT_EXIST(10003,"{}列不存在" ),
    UNSUPPORTED_FILTER_TYPE(10004, "不支持的过滤器类型"),
    UNSUPPORTED_FILTER_TYPE_FOR_REQUIRED_COLUMN(10005,"必传过滤条件不支持的过滤器类型" ),
    VALUE_TYPE_MISMATCH(10006, "{},值与列类型{}不匹配"),
    FULL_TABLE_SCAN_TRIGGERED(10007, "查询失败：触发了全表扫描，这可能会严重影响性能。请优化您的查询条件，避免全表扫描。"),
    PAGE_SIZE_TOO_LARGE(10008,"单页数据量不能超过{}" ),
    PAGE_INDEX_TOO_LARGE(10009,"页数不能超过{}" ),
    VALUE_LENGTH_IS_NOT_VALID(10010,"过滤值的列表长度不合法" ),
    UNSUPPORTED_VALUE_TYPE(10011,"不被支持的数据类型" ),

    EXPORT_TASK_NOT_EXISTS(10012,"导出任务不存在"),

    EXPLAIN_EXECUTE_ERROR(10013,"查询计划执行异常,请检查输入条件是否合法"),
    EXPORT_TASK_UNFINISHED(10014,"导出任务未完成"),
    QUERY_FILTERED_IS_EMPTY(10015,"查询条件未传"),
    TASK_START_ERROR(20001, "任务启动异常"),
    TASK_NAME_DUPLICATED(20002,"任务名称重复"),
    TASK_INSTANCE_EXISTED(20003,"该任务下存在任务实例,不允许删除"),

    TASK_UPDATED_PERMITTED(20004,"任务禁止编辑,请删除后重新创建"),

    TASK_NOT_FOUND(20005,"任务未找到"),

    TASK_LOG_GOT_FAILED(20006,"任务日志获取失败"),
    TASK_PARAMETER_PARSED_ERROR(20007,"任务参数解析失败"),
    DS_TASK_NAME_DUPLICATED(20008,"DS任务名称重复"),
    INVALID_PARTITION_SPEC_FORMAT(20009, "MaxCompute分区参数(partition_spec)不合法: {}"),
    MAXCOMPUTE_PARTITION_NOT_EXIST(20010, "MaxCompute分区 '{}' 在表 '{}' 中不存在"),
    TASK_TEMPLATE_NAME_DUPLICATED(30001,"任务模板名称重复"),
    TASK_TEMPLATE_NOT_EXISTED(30002,"任务模板id为{}的模板不存在"),


    UNKNOWN_ERROR(000000,"未知错误 {}");
    TaskErrorCode(Integer errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    private final Integer errCode;

    private final String errDesc;



    @Override
    public int code() {
        return errCode;
    }

    @Override
    public String msg() {
        return errDesc;
    }
}
