package com.minis.minispring.beans;


import com.minis.minispring.util.NumberUtils;
import com.minis.minispring.util.StringUtils;

import java.text.NumberFormat;

/**
 * 处理Number类型，并进行类型转换
 */
public class CustomNumberEditor implements PropertyEditor{
    // 数据类型
    private Class<? extends Number> numberClass;

    // 指定格式
    private NumberFormat numberFormat;

    private boolean allowEmpty;
    private Object value;

    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) throws IllegalArgumentException{
        this(numberClass, null, allowEmpty);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException{
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }

    // 将一个字符串转换成number赋值
    public void setAsText(String text){
        if(this.allowEmpty && !StringUtils.hasText(text)){
            setValue(null);
        }else if(this.numberFormat != null){
            // 给定格式
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        }else{
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }

    // 接收Object作为参数
    public void setValue(Object value){
        if(value instanceof Number){
            this.value = (NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass));
        }else{
            this.value =value;
        }
    }

    public Object getValue(){
        return this.value;
    }

    // 将number表示成格式化串
    public Object getAsText(){
        Object value = this.value;
        if(value == null){
            return "";
        }
        if(this.numberFormat != null){
            // 给定格式
            return this.numberFormat.format(value);
        }else{
            return value.toString();
        }
    }
}
