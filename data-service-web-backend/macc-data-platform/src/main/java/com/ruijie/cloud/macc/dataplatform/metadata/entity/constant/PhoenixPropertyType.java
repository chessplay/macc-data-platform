package com.ruijie.cloud.macc.dataplatform.metadata.entity.constant;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/04/09 15:05
 **/
public enum PhoenixPropertyType implements DataPropertyType {
    BOOLEAN,
    FLOAT,
    BIGINT,
    DOUBLE,
    INTEGER,
    INT,
    SMALLINT,
    TINYINT,
    NUMERIC,
    DECIMAL,
    TIMESTAMP,
    DATE,
    VARCHAR,
    CHAR;
    public static PhoenixPropertyType fromString(String name) {
        try {
            return PhoenixPropertyType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown PhoenixPropertyType: " + name);
        }
    }

}