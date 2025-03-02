package com.minis.minispring.context;

public abstract class ApplicationContextEvent extends ApplicationEvent{
    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
