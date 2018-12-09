package com.jim.framework.core.mvc.data.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by celiang.hu on 2018-11-10.
 */
public class DateTypeHandler extends BaseTypeHandler<Date> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.getTime());
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        long l = rs.getLong(columnName);
        return new Date(l);
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        long l = rs.getLong(columnIndex);
        return new Date(l);
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        long l = cs.getLong(columnIndex);
        return new Date(l);
    }
}
