package com.minis.minispring.core.env;

/**
 *  用于获取属性
 */

public interface Environment extends PropertyResolver{
    String[] getActiveProfiles();
    String[] getDefaultProfiles();
    boolean acceptsProfiles(String... profiles);
}
