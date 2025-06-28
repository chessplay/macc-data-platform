package com.ruijie.cloud.macc.dataplatform.metadata.entity.constant;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/04/09 15:05
 **/
public enum MysqlPropertyType implements DataPropertyType {
    BOOLEAN,
    FLOAT,
    BIGINT,
    DOUBLE,
    INTEGER,
    INT,
    INT_UNSIGNED,
    BIGINT_UNSIGNED,
    SMALLINT,
    TINYINT,
    NUMERIC,
    DECIMAL,
    TIMESTAMP,
    DATE,
    VARCHAR,
    CHAR,
    TEXT,
    LONGTEXT,
    ENUM;
    public static MysqlPropertyType fromString(String name) {
        name = name.toUpperCase();



        try {
            // 尝试返回对应的枚举值
            return MysqlPropertyType.valueOf(name);
        } catch (IllegalArgumentException e) {
            // 如果没有匹配的枚举值，抛出异常
            throw new IllegalArgumentException("Unknown MysqlPropertyType: " + name);
        }
    }

}