package com.minis.minispring.web.bind;

import com.minis.minispring.beans.AbstractPropertyAccessor;
import com.minis.minispring.beans.PropertyEditor;
import com.minis.minispring.beans.PropertyValues;
import com.minis.minispring.util.WebUtils;
import com.minis.minispring.web.BeanWrapperImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用于将 Request 请求内的字符串参数转换成不同类型的参数，来进行适配。
 */
public class WebDataBinder {
    private Object target;
    private Class<?> clz;
    private String objectName;
    AbstractPropertyAccessor propertyAccessor;

    public WebDataBinder(Object target){
        this(target, "");
    }

    public WebDataBinder(Object target, String targetName){
        this.target = target;
        this.objectName = targetName;
        this.clz = this.target.getClass();
        this.propertyAccessor = new BeanWrapperImpl(this.target);
    }

    // 核心绑定方法，将request里面的参数值绑定到目标对象的属性上
    public void bind(HttpServletRequest request) throws NoSuchFieldException {
        PropertyValues mpvs = assignParameters(request);
        addBindValues(mpvs, request);
        doBind(mpvs);
    }

    private void doBind(PropertyValues mpvs) throws NoSuchFieldException {
        applyPropertyValues(mpvs);
    }

    // 实际将参数值与对象属性进行绑定的方法
    protected void applyPropertyValues(PropertyValues mpvs) throws NoSuchFieldException {
        getPropertyAccessor().setPropertyValues(mpvs);
    }



    // 设置属性值的工具
    protected AbstractPropertyAccessor getPropertyAccessor() {
        return this.propertyAccessor;
    }

    // 将Request参数解析成PropertyValues
    private PropertyValues assignParameters(HttpServletRequest request){
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }

    protected void addBindValues(PropertyValues mpvs, HttpServletRequest request){

    }

    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor){
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }
}
