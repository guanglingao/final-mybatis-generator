package com.glinsoft.mybatis.database.mysql;

import com.glinsoft.mybatis.database.Column;
import com.glinsoft.mybatis.database.ColumnBuilder;
import com.glinsoft.mybatis.database.DatabaseConfig;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Set;

public class MySQLColumnBuilder extends ColumnBuilder {

    public MySQLColumnBuilder(DatabaseConfig config){
        super(config);
    }

    @Override
    protected String sql(String table) {
        return "SHOW FULL COLUMNS FROM " + table;
    }

    @Override
    protected Column build(Map<String, Object> map) {
        Set<String> columnSet = map.keySet();

        for (String columnInfo : columnSet) {
            map.put(columnInfo.toUpperCase(), map.get(columnInfo));
        }
        Column column = new Column();
        column.setName((String)map.get("FIELD"));
        boolean autoIncrement = "auto_increment".equalsIgnoreCase((String)map.get("EXTRA"));
        column.setAutoIncrement(autoIncrement);
        boolean isPk = "PRI".equalsIgnoreCase((String)map.get("KEY"));
        column.setPrimaryKey(isPk);
        String type = (String)map.get("TYPE");
        column.setType(buildType(type));
        column.setComment((String)map.get("COMMENT"));
        return column;
    }

    private String buildType(String type){
        if (StringUtils.isNotEmpty(type)) {
            int index = type.indexOf("(");
            if (index > 0) {
                return type.substring(0, index).toUpperCase();
            }
            return type;
        }
        return "VARCHAR";
    }
}
