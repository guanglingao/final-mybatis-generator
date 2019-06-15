package com.glinsoft.mybatis.target;

import lombok.Data;

/**
 * 文件（生成）配置
 */
@Data
public class FileConfig {

    // 表名
    private String table;
    // 文件编码
    private String charset;
    // 存放mapper的包名称
    private String mapperPackage;
    // 存放entity的包名称
    private String entityPackage;
    // 存放controller的包名称
    private String controllerPackage;
    // 存放service的包名称
    private String servicePackage;
    // mapper类名后缀
    private String mapperSuffix;
    // entity类名后缀
    private String entitySuffix;
    // controller类名后缀
    private String controllerSuffix;
    // service类名后缀
    private String serviceSuffix;
    // 文件所在的父级包
    private String parentPackage;



}
