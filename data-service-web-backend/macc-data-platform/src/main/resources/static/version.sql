CREATE TABLE `mdp_datasource` (
  `id` int(11) unsigned AUTO_INCREMENT COMMENT 'ID',
  `datasource_key` varchar(64) NOT NULL COMMENT '数据库标识',
  `name` varchar(64) NOT NULL COMMENT '名称',
  `db_type` varchar(16) NOT NULL COMMENT '数据库类型',
  `db_properties_json` text COMMENT '数据库连接信息',
  `description` varchar(256) NOT NULL COMMENT '备注',
  `creator` varchar(16) NOT NULL default '' COMMENT '最后更新者',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_updater` varchar(16) NOT NULL default '' COMMENT '最后更新者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY uk_datasource(`datasource_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源配置';

CREATE TABLE `mdp_access_authorization` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `access_key` varchar(64) NOT NULL COMMENT '通行密钥',
  `state` varchar(10) NOT NULL COMMENT '状态',
  `business_module` varchar(64) NOT NULL COMMENT '业务模块',
  `business_user` varchar(64) NOT NULL COMMENT '业务负责人',
  `description` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
  `expire_time` timestamp NOT NULL COMMENT '过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_access_key` (`access_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务访问密钥管理';


CREATE TABLE `mdp_task` (
    `id` bigint(20) unsigned AUTO_INCREMENT COMMENT 'ID',
    `name` varchar(64) NOT NULL COMMENT '名称',
    `source_table_name` varchar(64) NOT NULL COMMENT '源表名称',
    `sink_table_name` varchar(64) NOT NULL COMMENT '目标表名称',
    `source_data_key` varchar(64) NOT NULL COMMENT '源数据键',
    `sink_data_key` varchar(64) NOT NULL COMMENT '目标数据键',
    `field_mapping` varchar(256) NOT NULL COMMENT '字段映射',
    `description` varchar(256) NOT NULL COMMENT '备注',
    `process_code` bigint(20) NOT NULL COMMENT '工作流码',
    `creator` varchar(16) NOT NULL COMMENT '创建者',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updater` varchar(16) NOT NULL COMMENT '最后更新者',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_name(`name`),
    UNIQUE KEY uk_process_code(`process_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据同步任务';
-- guardian.mdp_task_template definition

CREATE TABLE `mdp_task_template` (
     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
     `name` varchar(64) NOT NULL COMMENT '名称',
     `source_table_name` varchar(64) NOT NULL COMMENT '源表名称',
     `sink_table_name` varchar(64) NOT NULL COMMENT '目标表名称',
     `source_data_key` varchar(64) NOT NULL COMMENT '源数据键',
     `sink_data_key` varchar(64) NOT NULL COMMENT '目标数据键',
     `field_mapping` varchar(512) NOT NULL COMMENT '字段映射',
     `source_query_filter` varchar(512) DEFAULT NULL COMMENT '过滤条件',
     `start_params` varchar(512) DEFAULT NULL COMMENT '启动参数',
     `request_example` varchar(1024) DEFAULT NULL COMMENT '请求示例',
     `description` varchar(256) NOT NULL COMMENT '备注',
     `process_code` bigint(20) NOT NULL COMMENT '工作流码',
     `task_code` bigint(20) NOT NULL COMMENT '任务码',
     `default_priority_level` int(11) DEFAULT '10' COMMENT '默认应用层优先级(越小越高)',
     `default_ds_priority` varchar(10) DEFAULT 'MEDIUM' COMMENT '默认DS实例优先级(HIGHEST/HIGH/MEDIUM/LOW/LOWEST)',
     `creator` varchar(16) NOT NULL COMMENT '创建者',
     `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `last_updater` varchar(16) NOT NULL COMMENT '最后更新者',
     `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='数据同步任务模板';

CREATE TABLE `mdp_task_submission_log` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
   `correlation_id` varchar(64)  NOT NULL COMMENT '关联ID (UUID)',
   `batch_id` varchar(64)  DEFAULT NULL COMMENT '批次ID (可选)',
   `template_id` bigint NOT NULL COMMENT '任务模板ID',
   `request_params_json` text  COMMENT '提交给DS的实际参数(JSON)',
   `ds_instance_id` int DEFAULT NULL COMMENT '成功获取到的DolphinScheduler实例ID',
   `submission_status` varchar(50)  COMMENT '提交与跟踪状态 ',
   `final_ds_status` varchar(50)  DEFAULT NULL COMMENT '从DS获取的最终实例状态 (SUCCESS, FAILURE, STOP, PAUSE等)',
   `retry_count` int NOT NULL DEFAULT '0' COMMENT '当前重试次数',
   `error_message` text  COMMENT '错误信息',
   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
   PRIMARY KEY (`id`),
   UNIQUE KEY `uk_correlation_id` (`correlation_id`),
   KEY `idx_ds_instance_id` (`ds_instance_id`),
   KEY `idx_submission_status` (`submission_status`),
   KEY `idx_template_id` (`template_id`),
   KEY `idx_batch_id` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT='任务提交与状态跟踪日志';