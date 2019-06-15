package com.glinsoft.mybatis.database.sqlserver;

import com.glinsoft.mybatis.database.Column;
import com.glinsoft.mybatis.database.ColumnBuilder;
import com.glinsoft.mybatis.database.DatabaseConfig;

import java.util.Map;
import java.util.Set;

public class SQLServerColumnBuilder extends ColumnBuilder {

    private static String TABKE_DETAIL_SQL = new StringBuilder()
            .append("SELECT")
            .append("	 col.name AS column_name")
            .append("	, bt.name AS type")
            .append("	, col.is_identity")
            .append("	, ext.value AS comment")
            .append("	,(")
            .append("		SELECT COUNT(1) FROM sys.indexes IDX ")
            .append("		INNER JOIN sys.index_columns IDXC ")
            .append("		ON IDX.[object_id]=IDXC.[object_id] ")
            .append("		AND IDX.index_id=IDXC.index_id ")
            .append("		LEFT JOIN sys.key_constraints KC ")
            .append("		ON IDX.[object_id]=KC.[parent_object_id] ")
            .append("		AND IDX.index_id=KC.unique_index_id ")
            .append("		INNER JOIN sys.objects O ")
            .append("		ON O.[object_id]=IDX.[object_id] ")
            .append("		WHERE O.[object_id]=col.[object_id] ")
            .append("		AND O.type='U' ")
            .append("		AND O.is_ms_shipped=0 ")
            .append("		AND IDX.is_primary_key=1 ")
            .append("		AND IDXC.Column_id=col.column_id ")
            .append("	) AS is_pk ")
            .append("FROM sys.columns col ")
            .append("LEFT OUTER JOIN sys.types bt on bt.user_type_id = col.system_type_id ")
            .append("LEFT JOIN sys.extended_properties ext ON ext.major_id = col.object_id AND ext.minor_id = col.column_id ")
            .append("WHERE col.object_id = object_id('%s') ")
            .append("ORDER BY col.column_id").toString();

    public SQLServerColumnBuilder(DatabaseConfig config){
        super(config);
    }

    @Override
    protected String sql(String table) {
        return String.format(TABKE_DETAIL_SQL, table);
    }

    @Override
    protected Column build(Map<String, Object> map) {
        Set<String> columnSet = map.keySet();

        for (String columnInfo : columnSet) {
            map.put(columnInfo.toUpperCase(), map.get(columnInfo));
        }
        Column column = new Column();
        column.setName((String)map.get("COLUMN_NAME"));
        column.setAutoIncrement((Boolean)map.get("IS_IDENTITY"));
        boolean isPk = (Integer)map.get("IS_PK") == 1;
        column.setPrimaryKey(isPk);
        column.setType((String)map.get("TYPE"));
        column.setComment((String)map.get("COMMENT"));
        return column;
    }
}
