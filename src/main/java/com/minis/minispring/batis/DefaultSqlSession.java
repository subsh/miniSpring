package com.minis.minispring.batis;

import com.minis.minispring.jdbc.core.JdbcTemplate;
import com.minis.minispring.jdbc.core.PreparedStatementCallback;

public class DefaultSqlSession implements SqlSession{
    JdbcTemplate jdbcTemplate;
    SqlSessionFactory sqlSessionFactory;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback) {
        String sql = this.sqlSessionFactory.getMapperNode(sqlid).getSql();
        return jdbcTemplate.query(sql,args, pstmtcallback);
    }

    private void buildParameter(){

    }

    private Object resultSet2Obj(){
        return null;
    }
}
