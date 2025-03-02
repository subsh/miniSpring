package com.minis.minispring.batis;

import com.minis.minispring.jdbc.core.JdbcTemplate;
import com.minis.minispring.jdbc.core.PreparedStatementCallback;

public interface SqlSession {
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);
    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);
    Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback);
}
