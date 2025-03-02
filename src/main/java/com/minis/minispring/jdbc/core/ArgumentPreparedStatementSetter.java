package com.minis.minispring.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 处理sql语句参数传入
 */
public class ArgumentPreparedStatementSetter {
    // 参数数组
    private final Object[] args;

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    // 设置SQL参数
    public void setValues(PreparedStatement pstmt) throws SQLException{
        if(this.args != null){
            for(int i = 0; i < this.args.length; i++){
                Object arg = this.args[i];
                doSetValue(pstmt, i + 1, arg);
            }
        }
    }

    // 对某个参数，设置参数值
    protected void doSetValue(PreparedStatement pstmt, int parameterPosition, Object argValue) throws SQLException {
        Object arg = argValue;

        // 判断参数类型，调用对应的JDBC set方法
        if (argValue instanceof String) {
            pstmt.setString(parameterPosition, (String)argValue);
        }
        else if (argValue instanceof Integer) {
            pstmt.setInt(parameterPosition, (int)argValue);
        }
        else if (argValue instanceof java.util.Date) {
            pstmt.setDate(parameterPosition, new java.sql.Date(((java.util.Date)argValue).getTime()));
        }
    }
}
