package com.glinsoft.mybatis.database;


import lombok.Data;

/**
 * 数据库配置
 */
@Data
public class DatabaseConfig {

    // 是否使用UUID，默认使用ID
    private boolean useUUID;

    // JDBC连接URL
    private String url;
    // 数据库登录，用户名
    private String username;
    // 密码
    private String password;
    // 数据库名称
    private String database;
    // 数据库驱动类名称
    private String driver;



}
