package com.minis.minispring.web.bind.support;

import com.minis.minispring.web.bind.WebDataBinder;

/**
 * 为自定义的CustomEditor注册做准备
 */
public interface WebBindingInitializer {
    void initBinder(WebDataBinder binder);
}
