package com.minis.minispring.batis;

public interface SqlSessionFactory {
    SqlSession openSession();
    MapperNode getMapperNode(String name);
}
