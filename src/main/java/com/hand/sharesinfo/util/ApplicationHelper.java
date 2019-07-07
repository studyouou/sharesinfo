package com.hand.sharesinfo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Author: OuGen
 * Discription: 帮助quartz获取bean
 * Date: 22:50 2019/7/7
 */
@Component
public class ApplicationHelper implements ApplicationContextAware {
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context=applicationContext;
    }
    public static Object getBean(String beanName){return context.getBean(beanName);}

    public static <T> T getBean(Class<T> tClass){return context.getBean(tClass);}
}

