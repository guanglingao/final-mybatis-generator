package com.glinsoft.mybatis.database.mysql;

import com.glinsoft.mybatis.database.DatabaseConfig;
import com.glinsoft.mybatis.database.TableAnalyser;
import com.glinsoft.mybatis.database.TableBuilder;

public class MySQLAnalyser implements TableAnalyser {


    @Override
    public TableBuilder getTableBuilder(DatabaseConfig config) {
        return new MySQLTableBuilder(config,new MySQLColumnBuilder(config));
    }


}
