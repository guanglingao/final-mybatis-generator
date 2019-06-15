package com.glinsoft.mybatis.database;

import com.glinsoft.mybatis.util.FieldName;
import com.glinsoft.mybatis.util.SQLExec;
import com.glinsoft.mybatis.util.SQLTypes;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ColumnBuilder {

    private DatabaseConfig config;

    protected ColumnBuilder(DatabaseConfig config){
        this.config = config;
    }

    protected abstract String sql(String table);

    protected abstract Column build(Map<String,Object> map);

    /**
     * 执行build方法完成后使用此方法返回，补全其他信息（用于文件生成）
     * @param column
     * @return
     */
    protected Column fulfillColumn(Column column){
        column.setJavaTypeBox(SQLTypes.convertToJavaBoxType(column.getType().toLowerCase()));
        column.setJavaType(SQLTypes.convertToJavaType(column.getType().toLowerCase()));
        String columnName = column.getName();
        if(isAllUpperCaseLettersWith_(columnName)) {
            columnName = columnName.toLowerCase();
            columnName = FieldName.underlineToCamel(columnName);
        }else if(isAllUpperCaseLetters(columnName)) {
            columnName = columnName.toLowerCase();
        }else if(isAllLowerCaseLettersWith_(columnName)) {
            columnName = FieldName.underlineToCamel(columnName);
        }
        column.setJavaFieldName(columnName);
        column.setJavaFieldNameUF(FieldName.upperFirstLetter(columnName));
        return column;
    }

    public List<Column> columns(DataSource dataSource,String table){
        List<Map<String,Object>> result = SQLExec.run(dataSource,sql(table),null);
        List<Column> cols = new ArrayList<>(result.size());
        for(Map<String,Object> m : result){
            cols.add(fulfillColumn(build(m)));
        }
        return cols;
    }


    private static boolean isAllUpperCaseLettersWith_(String columnName) {
        boolean has_ = columnName.contains("_");
        return has_ && isAllUpperCaseLetters(columnName);
    }

    private static boolean isAllUpperCaseLetters(String columnName) {
        boolean isAllUpperCase = columnName.replaceAll("[^a-zA-Z]", "").matches("^[A-Z]+$");
        return isAllUpperCase;
    }

    private static boolean isAllLowerCaseLettersWith_(String columnName) {
        boolean has_ = columnName.contains("_");
        return has_ && isAllLowerCaseLetters(columnName);
    }

    private static boolean isAllLowerCaseLetters(String columnName) {
        boolean isAllUpperCase = columnName.replaceAll("[^a-zA-Z]", "").matches("^[a-z]+$");
        return isAllUpperCase;
    }



}
