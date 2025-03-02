package com.minis.minispring.beans;

/**
 * 属性参数
 */

public class PropertyValue {
    private final Object value;
    private final String type;
    private final String name;

    private final boolean isRef;

    public PropertyValue(Object value, String type, String name, boolean ifRef) {
        this.value = value;
        this.type = type;
        this.name = name;
        this.isRef = ifRef;
    }

    public PropertyValue(Object value, String type, String name) {
        this(value, type, name, false);
    }

    public PropertyValue(Object value, String name) {
        this(value, name, "", false);
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean getIsRef() {
        return isRef;
    }
}
