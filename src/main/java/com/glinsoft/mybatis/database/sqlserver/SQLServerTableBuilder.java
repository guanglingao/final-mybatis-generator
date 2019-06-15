package com.glinsoft.mybatis.database.sqlserver;

import com.glinsoft.mybatis.database.ColumnBuilder;
import com.glinsoft.mybatis.database.DatabaseConfig;
import com.glinsoft.mybatis.database.Table;
import com.glinsoft.mybatis.database.TableBuilder;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SQLServerTableBuilder extends TableBuilder {

    private static final  String sql = "SELECT "+
        "SS.name + '.' + t.name AS table_name "+
        ",ISNULL(ext.value, '') as comment "+
        "FROM sysobjects t "+
        "INNER JOIN sys.objects SO ON t.name = SO.name "+
        "INNER JOIN sys.schemas  SS ON SO.schema_id = SS.schema_id "+
        "LEFT JOIN sys.extended_properties ext ON ext.major_id = SO.object_id and ext.minor_id=0 "+
        "WHERE t.xtype='u' ";

    public SQLServerTableBuilder(DatabaseConfig config, ColumnBuilder columnBuilder){
        super(config,columnBuilder);
    }

    @Override
    protected String sql(String database) {
       return sql+" ORDER BY SS.name ASC,t.name ASC";
    }

    @Override
    protected String sql(String database, String tablesIn) {

        return sql+buildTablesIn(tablesIn)+" ORDER BY SS.name ASC,t.name ASC";
    }

    @Override
    protected Table build(Map<String, Object> map) {
        Table table = new Table();
        table.setName((String)map.get("TABLE_NAME"));
        table.setComment((String)map.get("COMMENT"));
        return table;
    }

    private String buildTablesIn(String tablesIn) {
        List<String> names = Arrays.asList(tablesIn.split(","));
        int i = 0;
        StringBuilder tables = new StringBuilder(" and ( ");
        for (String table : names) {
            if(!StringUtils.isEmpty(table)) {
                String[] tableArr = table.split("\\.");
                if (i > 0) {
                    tables.append(" or ");
                }
                if (tableArr.length == 1) {
                    tables.append("(1=1 ").append(" and t.name='").append(tableArr[0]).append("') ");
                } else {
                    tables.append("(SS.name='").append(tableArr[0]).append("' and t.name='").append(tableArr[1]).append("') ");
                }
                i++;
            }
        }
        tables.append(" )");
        return tables.toString();
    }

}
