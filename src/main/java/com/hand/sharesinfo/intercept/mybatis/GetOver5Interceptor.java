package com.hand.sharesinfo.intercept.mybatis;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Author: OuGen
 * Discription:
 * Date: 13:05 2019/7/7
 */

@Component
@Intercepts(@Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class,Integer.class}))
public class GetOver5Interceptor implements Interceptor {
    Map<Integer,Integer> yue_to_day = new HashMap<Integer, Integer>();
    {
        yue_to_day.put(1,31);
        yue_to_day.put(2,29);
        yue_to_day.put(3,31);
        yue_to_day.put(4,30);
        yue_to_day.put(5,31);
        yue_to_day.put(6,30);
        yue_to_day.put(7,31);
        yue_to_day.put(8,31);
        yue_to_day.put(9,30);
        yue_to_day.put(10,31);
        yue_to_day.put(11,30);
        yue_to_day.put(12,31);
    }
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取RoutingStatementHandler
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        while (metaObject.hasGetter("h")){
            metaObject = SystemMetaObject.forObject(metaObject.getValue("h"));
        }
        while (metaObject.hasGetter("target")){
            metaObject=SystemMetaObject.forObject(metaObject.getValue("target"));
        }
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        String selectId = mappedStatement.getId();
        selectId = selectId.substring(selectId.lastIndexOf(".")+1);
        //拦截getNumOver5select方法
        if ("getNumOver5".equals(selectId)){
            ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
            Map<String,Object> parameterMap= (Map<String, Object>) parameterHandler.getParameterObject();
            int size = (int) parameterMap.get("size");
            double zhangfu = (double) parameterMap.get("zhangfu");
            int[] getNowDate = getNowDateYMD();
            int intDate = getThirtyAgoDay(size,getNowDate[0],getNowDate[1],getNowDate[2]);
            String sql = (String) metaObject.getValue("delegate.boundSql.sql");
            sql = sql+" and time>"+String.valueOf(intDate)+" and zhangfu>"+zhangfu;
            metaObject.setValue("delegate.boundSql.sql",sql);
        }
        return invocation.proceed();
    }

    private int[] getNowDateYMD() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] dateString = dateFormat.format(date).split("-");
        return new int[]{Integer.parseInt(dateString[0]),Integer.parseInt(dateString[1]),Integer.parseInt(dateString[2])};
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
    private int getThirtyAgoDay(int size,int year, int month,int day){

        //查询天数小于当前日期
        if (day>size){
            int agoday = day-size;
            int[] monthday = {year,month,agoday};
            return year*1000+month*10+agoday;
        }
        //查询天数跨越2个月
        int days = size-day;
        if (month==1){
            return ifmonth1(year,1,days);
        }else {
            month = month-1;
            while (days>yue_to_day.get(month)){
                days = days-yue_to_day.get(month);
                month--;
                if (month==1){
                    return ifmonth1(year,1,days);
                }
            }
            int lastday = yue_to_day.get(month)-days;
            return year*10000+month*100+lastday;
        }
    }

    private int ifmonth1(int year,int month,int days) {
        year = year-1;
        month=12;
        while (days>yue_to_day.get(month)){
            days = days -yue_to_day.get(month);
            month--;
        }
        int lastday = yue_to_day.get(month)-days;
        return year*1000+month*10+lastday;
    }
}
