package com.ruijie.cloud.macc.dataplatform.task.constant;

public class TaskParamsConstant {
    // 私有构造方法，防止外部实例化
    private TaskParamsConstant() {
        throw new UnsupportedOperationException("Constant class cannot be instantiated");
    }


    // 分组1: 任务相关常量
    public static final class MaxCompute {
        public static final String PARTITION_SPEC = "partition_spec";
        public static final String OVERWRITE = "overwrite";

    }
    public static final class Engine {
        public static final String FLINK = "partition_spec";
    }
}
