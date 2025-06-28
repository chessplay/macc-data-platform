
CREATE TABLE IF NOT EXISTS `privilege_module` (
  `system` varchar(16) NOT NULL COMMENT '所属系统',
  `module_id` varchar(64) NOT NULL DEFAULT '' COMMENT '模块ID',
  `module_name` varchar(64) NOT NULL DEFAULT '' COMMENT '模块名称',
  `type` varchar(64) DEFAULT '' COMMENT '类别',
  `description` varchar(256) DEFAULT '' COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`system`, `module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模块表';

CREATE TABLE IF NOT EXISTS `privilege_role` (
  `system` varchar(16) NOT NULL COMMENT '所属系统',
  `role_id` varchar(64) NOT NULL DEFAULT '' COMMENT '角色ID',
  `role_name` varchar(64) NOT NULL DEFAULT '' COMMENT '角色名称',
  `description` varchar(256) DEFAULT '' COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`system`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `privilege_menu` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `system` varchar(16) NOT NULL COMMENT '所属系统',
  `namespace` varchar(16) NOT NULL DEFAULT 'MACC' COMMENT '空间',
  `parent_path` varchar(64) NOT NULL DEFAULT '' COMMENT '父菜单路径',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT '名称',
  `url` varchar(128) NOT NULL DEFAULT '' COMMENT '路由',
  `icon` varchar(64) NOT NULL DEFAULT '' COMMENT '图标',
  `priority` int(10) NOT NULL DEFAULT '0' COMMENT '序号',
  `params` varchar(1024) DEFAULT '' COMMENT '参数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY ik_parentpath(`parent_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

CREATE TABLE IF NOT EXISTS `privilege_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `system` varchar(16) NOT NULL COMMENT '所属系统',
  `role_id` varchar(64) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY uk_roleid_menuid(`system`, `role_id`, `menu_id`),
  KEY ik_menuid(`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单表';

CREATE TABLE IF NOT EXISTS `privilege_role_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `system` varchar(16) NOT NULL COMMENT '所属系统',
  `role_id` varchar(64) NOT NULL DEFAULT '' COMMENT '角色ID',
  `module_id` varchar(64) NOT NULL DEFAULT '' COMMENT '模块ID',
  `privilege` varchar(16) DEFAULT '' COMMENT '权限值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY uk_roleid_moduleid(`system`, `role_id`, `module_id`),
  KEY ik_moduleid(`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色模块表';

CREATE TABLE IF NOT EXISTS `privilege_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `system` varchar(16) NOT NULL COMMENT '所属系统',
  `user_id` int(11) NOT NULL COMMENT '账号id',
  `user_name` varchar(64) NOT NULL COMMENT '账号名',
  `role_id` varchar(64) NOT NULL DEFAULT '' COMMENT '角色ID',
  `state` varchar(16) NOT NULL DEFAULT '' COMMENT '账号状态',
  `description` varchar(256) NOT NULL DEFAULT '' COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY uk_userid(`system`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账号状态信息表';

CREATE TABLE IF NOT EXISTS `privilege_namespace` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `system` varchar(16) NOT NULL COMMENT '所属系统',
  `name` varchar(64) NOT NULL default 'DEFAULT' COMMENT '空间名称',
  `namespace` varchar(64) NOT NULL default 'DEFAULT' COMMENT '空间标识',
  `description` varchar(256) NOT NULL DEFAULT '' COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY uk_namespace(`system`, `namespace`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='空间管理';

CREATE TABLE IF NOT EXISTS `privilege_role_namespace` (
  `system` varchar(16) NOT NULL COMMENT '所属系统',
  `namespace` varchar(64) NOT NULL DEFAULT '' COMMENT '空间标识',
  `role_id` varchar(64) NOT NULL DEFAULT '' COMMENT '角色名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`system`, `namespace`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色空间权限表';

