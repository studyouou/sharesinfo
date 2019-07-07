package com.hand.sharesinfo.datadownload;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionUtil {
    private static String url = "jdbc:mysql://192.168.43.201:3306/shares?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false";
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String username = "root";
    private static String password = "mysql123";
    private static Map<String,String> map = new HashMap<String,String>();
    static {
        map.put("url",url);
        map.put("username",username);
        map.put("password",password);
        map.put("driverClassName",driverName);
        map.put("initialSize","10");
        map.put("maxActive","20");
        map.put("minIdle","10");
        map.put("initialSize","10");
    }
    private static DruidDataSource druidDataSource;

    static {
        try {
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        try {
            Connection connection = druidDataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("/获取连接失败");
        }
    }
    public static void close(){
        druidDataSource.close();
    }
}

