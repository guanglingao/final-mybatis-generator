package com.glinsoft.mybatis.database;

import com.glinsoft.mybatis.database.mysql.MySQLAnalyser;
import com.glinsoft.mybatis.database.sqlserver.SQLServerAnalyser;

import java.util.HashMap;
import java.util.Map;

public class TableAnalyserFactory {

    private static Map<String,TableAnalyser> storage = new HashMap<>();

    public static TableAnalyser get(DatabaseConfig config){
        String driver = config.getDriver();
        TableAnalyser analyser = null;
        if((analyser = storage.get(driver))==null){
            analyser = match(driver);
            storage.put(driver,analyser);
        }
        return analyser;
    }

    private static TableAnalyser match(String driverClassName){
        driverClassName = driverClassName.toLowerCase();
        if(driverClassName.contains("mysql")){
            return new MySQLAnalyser();
        }
        if(driverClassName.contains("jtds")){
            return new SQLServerAnalyser();
        }
        if(driverClassName.contains("sqlserver")){
            return new SQLServerAnalyser();
        }
        System.out.println("数据库驱动未设置或不支持此类数据库类型，正在使用MySQL解析器处理。");
        return new MySQLAnalyser();
    }
}
