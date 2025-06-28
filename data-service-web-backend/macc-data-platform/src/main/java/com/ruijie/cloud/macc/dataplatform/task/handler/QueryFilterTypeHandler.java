package com.ruijie.cloud.macc.dataplatform.task.handler;

/**
 * @author: zhouliwang
 * @description: TODO
 * @create: 2024/07/29 16:45
 **/
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilter;
import com.ruijie.cloud.macc.dataplatform.metadata.entity.QueryFilterTemplate;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QueryFilterTypeHandler extends BaseTypeHandler<List<QueryFilterTemplate>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<QueryFilterTemplate> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, toJson(parameter));
    }

    @Override
    public List<QueryFilterTemplate> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return fromJson(rs.getString(columnName));
    }

    @Override
    public List<QueryFilterTemplate> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return fromJson(rs.getString(columnIndex));
    }

    @Override
    public List<QueryFilterTemplate> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return fromJson(cs.getString(columnIndex));
    }

    private String toJson(List<QueryFilterTemplate> parameter) {
        try {
            return objectMapper.writeValueAsString(parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<QueryFilterTemplate> fromJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<QueryFilterTemplate>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
