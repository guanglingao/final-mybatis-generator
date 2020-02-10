package com.glinsoft.mybatis.database;

import com.glinsoft.mybatis.util.SQLExec;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class TableBuilder {


    private ColumnBuilder columnBuilder;
    private DatabaseConfig config;

    public TableBuilder(DatabaseConfig config, ColumnBuilder columnBuilder){
        this.config = config;
        this.columnBuilder = columnBuilder;
    }


    protected abstract String sql(String database);

    protected abstract String sql(String database,String tablesIn);

    protected abstract Table build(Map<String,Object> map);

    protected Table fulfillTable(Table table){
        boolean hasDateField = false;
        boolean hasDateTimeField = false;
        boolean hasTimeField = false;
        boolean hasBigDecimalField = false;
        List<Column> columns = table.getColumns();
        for (Column column : columns) {
            if("LocalDate".equals(column.getJavaType())) {
                hasDateField = true;
            }
            if("LocalDateTime".equals(column.getJavaType())) {
                hasDateTimeField = true;
            }
            if("BigDecimal".equals(column.getJavaType())) {
                hasBigDecimalField=true;
            }
            if("LocalTime".equals(column.getJavaType())) {
                hasTimeField=true;
            }
        }
        table.setHasBigDecimalField(hasBigDecimalField);
        table.setHasDateField(hasDateField);
        table.setHasDateTimeField(hasDateTimeField);
        table.setHasTimeField(hasTimeField);
        return table;
    }

    public List<Table> tables(DataSource dataSrc,String database,String tablesIn){
        List<Map<String,Object>> result = null;
        if(StringUtils.isEmpty(tablesIn)){
            result = SQLExec.run(dataSrc,sql(database),null);
        }else{
            result = SQLExec.run(dataSrc,sql(database,tablesIn),null);
        }
        if(result.size()==0){
            throw new RuntimeException("查询不到数据库表，请检查配置是否正确");
        }
        List<Table> tables = new ArrayList<>(result.size());

        for(Map<String,Object> m : result){
            Table table = build(m);
            table.setUseUUID(config.isUseUUID());
            table.setColumns(columnBuilder.columns(dataSrc,table.getName()));
            table = fulfillTable(table);
            tables.add(table);
        }
        return tables;
    }


    protected void setConfig(DatabaseConfig config) {
        this.config = config;
    }

    protected DatabaseConfig getConfig() {
        return config;
    }
}
