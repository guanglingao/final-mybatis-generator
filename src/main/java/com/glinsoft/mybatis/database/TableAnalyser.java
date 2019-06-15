package com.glinsoft.mybatis.database;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;


@FunctionalInterface
public interface TableAnalyser {

    TableBuilder getTableBuilder(DatabaseConfig config);

    default DataSource getDataSource(DatabaseConfig config){
        Properties properties = new Properties();
        properties.setProperty("driver", config.getDriver());
        properties.setProperty("url", config.getUrl());
        properties.setProperty("username", config.getUsername());
        properties.setProperty("password", config.getPassword());
        PooledDataSourceFactory pooledDataSourceFactory = new PooledDataSourceFactory();
        pooledDataSourceFactory.setProperties(properties);
        return pooledDataSourceFactory.getDataSource();
    }

}
