package com.glinsoft.mybatis;

import com.alibaba.fastjson.JSON;
import com.glinsoft.mybatis.database.*;
import com.glinsoft.mybatis.template.Template;
import com.glinsoft.mybatis.target.FileConfig;
import com.glinsoft.mybatis.target.FileGenerationContext;
import com.glinsoft.mybatis.util.FieldName;
import com.glinsoft.mybatis.util.FileSupport;
import com.glinsoft.mybatis.util.StringFormat;
import com.glinsoft.mybatis.util.VelocityReferer;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FinalMyBatisGenerator {


    /**
     * 生成全部文件，数据库列名使用下划线连接
     */
    public static void generateRESTs(){
        new FinalMyBatisGenerator().generateTableWithREST(true,false);
    }

    /**
     * 生成全部文件
     * @param columnUseCamel 列名使用驼峰形式
     */
    public static void generateRESTs(boolean columnUseCamel){
        new FinalMyBatisGenerator().generateTableWithREST(true,columnUseCamel);
    }

    /**
     * 只生成MyBatis映射文件，数据库列名使用下划线连接
     */
    public static void generateMappersOnly(){
        new FinalMyBatisGenerator().generateTableWithREST(false,false);
    }

    /**
     * 只生成MyBatis映射文件
     * @param columnUseCamel 列名使用驼峰形式
     */
    public static void generateMappersOnly(boolean columnUseCamel){
        new FinalMyBatisGenerator().generateTableWithREST(false,columnUseCamel);
    }

    private List<String>  tableNames;

    /**
     * 执行文件生成
     * @param rest true:生成REST的全部文件，false:仅生成mappers和entities
     * @param columnUseCamel 列名是否使用驼峰方式
     */
    private void generateTableWithREST(boolean rest,boolean columnUseCamel) {
        System.out.println("开始生成，此代码生成工具仅支持『MySQL』、『MSSQLServer』两种数据库的代码自动生成。");
        // 数据库表部分
        DatabaseConfig databaseConfig = GeneratorConfig.loadDatabaseConfig(!columnUseCamel);
        TableAnalyser analyser = TableAnalyserFactory.get(databaseConfig);
        List<Table> tables = analyser.getTableBuilder(databaseConfig).tables(analyser.getDataSource(databaseConfig), GeneratorConfig.database(), GeneratorConfig.tables());
        Map<String, String> exceptions = new HashMap<>();
        try {
            System.out.print("正在生成文件.");
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(new TimerTask(){
                public void run()
                {
                    System.out.print(" . . .");
                }
            },100,100);
            int tableCount = 0;
            tableNames = new ArrayList<>();
            for (Table table : tables) {
                Column column = table.getPrimaryKeyColumn();
                if (column == null) {
                    exceptions.put(table.getName(),"未设置主键，未生成此表映射。");
                    continue;
                } else {
                    FileConfig fileConfig = GeneratorConfig.loadFileConfig(table.getName());
                    generateTableFile(table,fileConfig,rest);
                    tableCount++;
                    tableNames.add(FieldName.upperFirstLetter(getTableName(table.getName())));
                }

            }
            timer.cancel();
            // 数据库表相关，生成完成。开始生成其他文件
            // REST相关文件
            if(rest){
                generateUseFile(GeneratorConfig.destination(),GeneratorConfig.defaultFileConfig());
            }
            // 生成结果输出
            System.out.println("\n==============================================================================");
            exceptions.forEach((key,value)->{
                System.out.println("『表』 "+key.toUpperCase()+"，"+value);
            });
            System.out.println("\n总生成映射 " +tableCount+" 张表。");
            System.out.println("生成完成，请查看此路径："+GeneratorConfig.destination());
            System.out.println("==============================================================================");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 生成一张表响应的Mapper和Entity文件
     * @param config
     */
    private void generateTableFile(Table table, FileConfig config, boolean rest) {

        FileGenerationContext context = buildGenerateContext(table);
        Path path = Paths.get(GeneratorConfig.destination());
        List<String> templates;
        if(rest){
            templates = Arrays.asList(GeneratorConfig.loadTemplateREST().split(","));
        }else{
            templates = Arrays.asList(GeneratorConfig.loadTemplateMapper().split(","));
        }
        try{
            if(!Files.exists(path)){
                Files.createDirectory(path);
            }
            context.setParentPackage(GeneratorConfig.parentPackage());
            for(String tpl : templates){
                if(StringUtils.isEmpty(tpl)){
                    continue;
                }
                Template template = buildTemplate(tpl);
                String fileName = buildFileContent(context,template.getFileName());
                String saveFolder = buildFileContent(context,template.getSaveFolder());
                String tplCont = buildFileContent(context,template.getContent());
                tplCont = formatCode(fileName, tplCont);
                String saveDir = GeneratorConfig.destination() + File.separator + saveFolder;
                saveDir = saveDir.replaceAll("\\.",File.separator);
                if(!Files.exists(Paths.get(saveDir))){
                    Files.createDirectories(Paths.get(saveDir));
                }
                String filePathName = saveDir + File.separator +fileName;
                File target = new File(filePathName);
                // 判断文件是否已经存在 @since 2.7.8
                if(target.exists()){
                    target.delete();
                }
                FileSupport.write(tplCont,filePathName,config.getCharset());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    private String formatCode(String fileName, String content) {
        if (fileName.endsWith(".xml")) {
            return StringFormat.formatXml(content);
        }
        if(fileName.toLowerCase().endsWith(".java")) {
            return StringFormat.formatJava(content);
        }
        return content;
    }


    /**
     * 对于每一张表构建文件生成Context（上下文）
     * @param table
     * @return
     */
    private FileGenerationContext buildGenerateContext(Table table){
        FileGenerationContext fileGenerateContext = new FileGenerationContext();
        fileGenerateContext.setTable(table);
        fileGenerateContext.setParam(GeneratorConfig.param());
        fileGenerateContext.setParentPackage(GeneratorConfig.parentPackage());
        String tableName = getTableName(table.getName());
        tableName = FieldName.upperFirstLetter(tableName) + getEntityStuffix();
        fileGenerateContext.setJavaBeanName(tableName);
        return fileGenerateContext;
    }



    // 生成正确样式的表名
    private String getTableName(String tableName){
        tableName = this.removeTablePrefix(tableName);
        tableName = this.removeTableSuffix(tableName);

        tableName = FieldName.underlineToCamel(tableName);
        tableName = FieldName.removePreviousDot(tableName);
        return FieldName.lowerFirstLetter(tableName);
    }
    // Entity类后缀
    private String getEntityStuffix() {
        String stuffix = (String) GeneratorConfig.param().get("entitySuffix");
        if (stuffix == null) {
            return "";
        }
        return stuffix;
    }

    // 删除前缀
    private String removeTablePrefix(String tableName) {
        String tablePrefix = (String)GeneratorConfig.param().get("tablePrefix");
        if(org.apache.commons.lang.StringUtils.isNotBlank(tablePrefix)) {
            String[] arrTablePrefix = tablePrefix.split(",");
            for(String prefixOne : arrTablePrefix){
                if(org.apache.commons.lang.StringUtils.isNotBlank(prefixOne)){
                    if(tableName.startsWith(prefixOne)) {
                        tableName = tableName.substring(prefixOne.length());
                    }
                }
            }
        }
        return tableName;
    }

    // 删除后缀
    private String removeTableSuffix(String tableName) {
        String tableSuffix = (String)GeneratorConfig.param().get("tableSuffix");
        if(org.apache.commons.lang.StringUtils.isNotBlank(tableSuffix)) {
            if(tableName.endsWith(tableSuffix)) {
                tableName = tableName.substring(0, tableSuffix.length() + 1);
            }
        }
        return tableName;
    }


    /**
     * 构建（velocity）生成模板
     * @param tplName
     * @return
     */
    private Template buildTemplate(String tplName){
        String json = FileSupport.readFromClassPath(File.separator+"tpl"+File.separator+tplName);
        Template template = JSON.parseObject(json, Template.class);
        String contentTplPath = File.separator+"tpl"+File.separator+tplName+"_cont";
        String content = FileSupport.readFromClassPath(contentTplPath);
        template.setContent(content);
        return template;
    }


    /**
     * 构建将要写入文件的内容
     * @param generationContext
     * @param template
     * @returne
     */
    private String buildFileContent(FileGenerationContext generationContext, String template) {
        if(StringUtils.isEmpty(template)){
            return "";
        }
        VelocityContext context = new VelocityContext();
        Table table = generationContext.getTable();
        context.put("context", generationContext);
        context.put("param", generationContext.getParam());
        context.put("table", table);
        context.put("pk", table.getPrimaryKeyColumn());
        context.put("columns", table.getColumns());
        return VelocityReferer.merge(context,template);
    }


    /**
     * 生成其他文件
     * @param destination
     * @param fileConfig
     */
    private void generateUseFile(String destination,FileConfig fileConfig){
        try{
            Map<String,String> fileTemplateMap = new HashMap<>();
            GeneratorConfig.useFile().forEach(e->{
                String filePath = destination+File.separator+e.replaceAll(File.separator + "use"+ File.separator,"");
                fileTemplateMap.put(filePath,FileSupport.readFromClassPath(e));
            });
            //将模板内容转合并为实际内容，并输出（写入文件）
            fileTemplateMap.forEach((filePath,template)->{
                if(filePath.endsWith(".txt")){
                    filePath =  filePath.substring(0,filePath.lastIndexOf(".txt"));
                }
                String folder = filePath.substring(0,filePath.lastIndexOf(File.separator));
                File dirs = new File(folder);
                if(!dirs.exists()){
                    try {
                        Files.createDirectories(Paths.get(folder));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                VelocityContext context = new VelocityContext();
                context.put("parentPackage", fileConfig.getParentPackage());
                context.put("param", GeneratorConfig.param());
                context.put("tableNames",tableNames);
                String content = VelocityReferer.merge(context,template);
                FileSupport.write(content,filePath,fileConfig.getCharset());
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }






}
