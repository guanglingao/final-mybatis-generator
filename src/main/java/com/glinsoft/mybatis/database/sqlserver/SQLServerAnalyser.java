package com.glinsoft.mybatis.database.sqlserver;

import com.glinsoft.mybatis.database.DatabaseConfig;
import com.glinsoft.mybatis.database.TableAnalyser;
import com.glinsoft.mybatis.database.TableBuilder;

public class SQLServerAnalyser implements TableAnalyser {


    @Override
    public TableBuilder getTableBuilder(DatabaseConfig config) {
        return new SQLServerTableBuilder(config,new SQLServerColumnBuilder(config));
    }


}
