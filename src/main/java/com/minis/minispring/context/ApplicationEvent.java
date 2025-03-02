package com.minis.minispring.context;

import java.util.EventObject;

/**
 * 事件监听
 */
public abstract class ApplicationEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    protected String msg = null;

    public ApplicationEvent(Object arg0){
        super(arg0);
        this.msg = arg0.toString();
    }
}
