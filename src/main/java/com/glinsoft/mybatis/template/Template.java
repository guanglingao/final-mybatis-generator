package com.glinsoft.mybatis.template;

import lombok.Data;

@Data
public class Template {

    // 模板文件名称
    private String fileName;
    // 保存文件夹名称
    private String saveFolder;
    // 模板内容
    private String content;
    
}
