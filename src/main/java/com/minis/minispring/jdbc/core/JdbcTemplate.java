package com.minis.minispring.jdbc.core;

import java.sql.*;
import java.util.List;
import javax.sql.DataSource;

public class JdbcTemplate {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public JdbcTemplate() {
    }

    // 静态sql查询
    public Object query(StatementCallback stmtcallback) {
        Connection con = null;
        Statement stmt = null;

        try {
            con = dataSource.getConnection();

            stmt = con.createStatement();

            return stmtcallback.doInStatement(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {

            }
        }

        return null;

    }

    // 动态sql查询
    public Object query(String sql, Object[] args, PreparedStatementCallback pstmtcallback) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // 通过datasource拿数据库连接
            con = dataSource.getConnection();

            pstmt = con.prepareStatement(sql);
            // 通过argumentSetter统一设置参数值
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);

            return pstmtcallback.doInPreparedStatement(pstmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception e) {

            }
        }

        return null;

    }

    // 查询多条数据方法
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper){
        RowMapperResultSetExtractor<T> resultExtractor = new RowMapperResultSetExtractor<>(rowMapper);
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            // 建立数据库连接
            con = dataSource.getConnection();

            // 准备SQL命令语句
            pstmt = con.prepareStatement(sql);

            // 设置参数
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);

            // 数据库结果集映射为对象列表返回
            return resultExtractor.extractData(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                pstmt.close();
                con.close();
            } catch (Exception e) {
            }
        }
        return null;
    }
}
