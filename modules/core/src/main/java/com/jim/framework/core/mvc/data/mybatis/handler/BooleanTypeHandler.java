package com.jim.framework.core.mvc.data.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by celiang.hu on 2018-11-10.
 */
public class BooleanTypeHandler extends BaseTypeHandler<Boolean> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType.equals(JdbcType.INTEGER) || jdbcType.equals(JdbcType.TINYINT) || jdbcType.equals(JdbcType.BIGINT) || jdbcType.equals(JdbcType.BIT)) {
            ps.setByte(i, parameter ? (byte) 1 : (byte) 0);
        } else if (jdbcType.equals(JdbcType.CHAR) || jdbcType.equals(JdbcType.VARCHAR)) {
            ps.setString(i, parameter ? "1" : "0");
        } else {
            throw new SQLException("Can not convert column value to [Boolean], supported column types are [INT, TINYINT, BIGINT, CHAR, VARCHAR]");
        }
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return "1".equals(s);
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return "1".equals(s);
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return "1".equals(s);
    }
}
