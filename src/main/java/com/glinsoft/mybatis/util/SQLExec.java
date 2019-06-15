package com.glinsoft.mybatis.util;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SQLExec {


    /**
     * 执行SQL语句，并返回执行结果
     * @param dataSrc 数据源
     * @param sql 需执行的SQL语句
     * @param params SQL参数
     * @return
     */
    public static List<Map<String,Object>> run(DataSource dataSrc, String sql, Map<String,Object> params){
        String runSql = getSqlWithParam(dataSrc, sql, params);
        String[] sqls = runSql.split(";");

        Connection conn = null;
        try {
            conn = dataSrc.getConnection();
            SqlRunner runner = new SqlRunner(conn);
            int sqlCount = sqls.length;
            if(sqlCount == 1) {
                return runner.selectAll(sqls[0],new Object[]{});
            }else {
                for (int i = 0; i < sqlCount-1; i++) {
                    runner.run(sqls[i]);
                    System.out.println("执行："+sqls[i]);
                }
                return runner.selectAll(sqls[sqlCount - 1],new Object[]{});
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            try {
                conn.setReadOnly(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }finally{
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 构建待执行SQL
     * @param dataSrc 数据源
     * @param sql SQL
     * @param params 参数
     * @return
     */
    private static String getSqlWithParam(DataSource dataSrc, String sql, Map<String,Object> params){
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSrc);
        Configuration configuration = new Configuration(environment);
        TextSqlNode node = new TextSqlNode(sql);
        DynamicSqlSource dynamicSqlSource = new DynamicSqlSource(configuration,node);
        BoundSql boundSql = dynamicSqlSource.getBoundSql(params);
        return boundSql.getSql();
    }



}
