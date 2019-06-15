package com.glinsoft.mybatis.database.mysql;

import com.glinsoft.mybatis.database.ColumnBuilder;
import com.glinsoft.mybatis.database.DatabaseConfig;
import com.glinsoft.mybatis.database.Table;
import com.glinsoft.mybatis.database.TableBuilder;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MySQLTableBuilder extends TableBuilder {

    public MySQLTableBuilder(DatabaseConfig config, ColumnBuilder columnBuilder){
        super(config,columnBuilder);
    }

    @Override
    protected String sql(String database) {

        String sql = "SHOW TABLE STATUS FROM " + database;
        return sql;
    }

    @Override
    protected String sql(String database, String tablesIn) {
        String sql = "SHOW TABLE STATUS FROM " + database;
        List<String> names = Arrays.asList(tablesIn.split(","));
        StringBuilder tables = new StringBuilder();
        for (String table : names) {
            if(!StringUtils.isEmpty(table)) {
                tables.append(",'").append(table).append("'");
            }
        }
        sql += " WHERE NAME IN (" + tables.substring(1) + ")";
        return sql;
    }

    @Override
    protected Table build(Map<String, Object> map) {
        Table table = new Table();
        table.setName((String)map.get("NAME"));
        table.setComment((String)map.get("COMMENT"));
        return table;
    }

}
