package com.glinsoft.mybatis.database;

import com.glinsoft.mybatis.util.UUID;
import lombok.Data;

import java.util.List;

/**
 * 数据库，表
 */
@Data
public class Table {

     private String name;
     private String comment;
     private boolean useUUID;
     private final long serializeId= UUID.getUUID();
     private boolean hasDateField;
     private boolean hasBigDecimalField;

     private List<Column> columns;

     public Column getPrimaryKeyColumn(){
         for(Column column : columns){
             if(column.isPrimaryKey()){
                 return column;
             }
         }
         return null;
     }





}
