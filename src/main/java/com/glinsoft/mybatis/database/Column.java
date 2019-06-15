package com.glinsoft.mybatis.database;


import lombok.Data;

/**
 * 数据库，列
 */
@Data
public class Column {

    private String name;
    private String type;
    private String comment;
    private boolean autoIncrement;
    private boolean isPrimaryKey;
    private String javaType;
    private String javaTypeBox;
    private String javaFieldName;
    private String javaFieldNameUF;


}
