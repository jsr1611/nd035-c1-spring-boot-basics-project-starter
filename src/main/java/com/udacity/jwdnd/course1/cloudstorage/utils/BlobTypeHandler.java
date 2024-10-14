package com.udacity.jwdnd.course1.cloudstorage.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class BlobTypeHandler extends BaseTypeHandler<byte[]> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, byte[] bytes, JdbcType jdbcType) throws SQLException {
        preparedStatement.setBytes(i, bytes);
    }

    @Override
    public byte[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getBytes(columnName);
    }

    @Override
    public byte[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getBytes(columnIndex);
    }

    @Override
    public byte[] getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return callableStatement.getBytes(columnIndex);
    }
}
