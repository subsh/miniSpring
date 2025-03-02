package com.minis.minispring.context;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个简单的事件发布者
 */
public class SimpleApplicationEventPublisher implements ApplicationEventPublisher{
    List<ApplicationListener> listeners = new ArrayList<>();

    public void publishEvent(ApplicationEvent event){
        for(ApplicationListener listener : listeners){
            listener.onApplicationEvent(event);
        }
    }

    public void addApplicationListener(ApplicationListener listener){
        this.listeners.add(listener);
    }
}
