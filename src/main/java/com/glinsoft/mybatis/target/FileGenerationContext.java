package com.glinsoft.mybatis.target;

import com.glinsoft.mybatis.database.Table;
import lombok.Data;

import java.util.Map;

@Data
public class FileGenerationContext {


    private String parentPackage;
    private Table table;
    private Map<Object, Object> param;
    private String javaBeanName;



}
