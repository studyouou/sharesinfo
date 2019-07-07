package com.hand.sharesinfo.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Author: OuGen
 * Discription:
 * Date: 18:06 2019/7/7
 */
@Configuration
public class TenMToGetDataQuart {
    @Bean("jobDetail")
    public MethodInvokingJobDetailFactoryBean getmethodInvokingJobDetailFactoryBean(){
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setName("gengxin");
        bean.setTargetObject(new GenXinJob());
        bean.setTargetMethod("genxin");
        return bean;
    }

    @Bean("simpleTrigger")
    public SimpleTriggerFactoryBean getTriggerBean(MethodInvokingJobDetailFactoryBean jobDetail){
        SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
        bean.setBeanName("dingshiqi");
        bean.setJobDetail(jobDetail.getObject());
        bean.setRepeatInterval(5000);
        return bean;
    }
    @Bean("scheduler")
    public SchedulerFactoryBean getSchFactoryBean(Trigger simpleTrigger){
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setOverwriteExistingJobs(true);
        bean.setStartupDelay(10);
        bean.setTriggers(simpleTrigger);
        return bean;
    }
}
