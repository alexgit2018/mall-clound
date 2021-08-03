package com.mall.ordercenter.batisalia;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonIntegerArrayTypeHandler extends BaseTypeHandler<Integer[]> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Integer[] parameter, JdbcType jdbcType) throws SQLException {

        ps.setString(i, toJson(parameter));
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toObject(rs.getString(columnName));
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toObject(rs.getString(columnIndex));
    }

    @Override
    public Integer[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toObject(cs.getString(columnIndex));
    }

    private String toJson(Integer[] para) {
        try {
            return mapper.writeValueAsString(para);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "[]";
    }

    public Integer[] toObject(String para) {
        try {
            if (!StringUtils.isEmpty(para)) {
                return (Integer[]) mapper.readValue(para, Integer[].class);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
