package com.minis.minispring.beans;

/**
 * 一个通用接口，让字符串和Object之间进行双向转换
 */
public interface PropertyEditor {
    void setAsText(String text);
    void setValue(Object value);
    Object getValue();
    Object getAsText();
}
