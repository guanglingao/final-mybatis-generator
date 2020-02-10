package com.glinsoft.mybatis;

import com.glinsoft.mybatis.database.DatabaseConfig;
import com.glinsoft.mybatis.target.FileConfig;

import java.io.File;
import java.io.InputStream;
import java.util.*;

public class GeneratorConfig {

    private final static String TEMPLATES_REST = "entity.tpl,mapper.tpl,controller.tpl,service.tpl,vo.tpl";
    private final static String TEMPLATES_MAPPERS = "entity.tpl,mapper.tpl";
    private static Map<Object,Object> params = null;
    private static Map<String,Object> configuration = null;

    // 常量名称定义
    private final static String CHARSET ="UTF-8";
    private volatile static boolean CAMEL = true;
    private final static boolean USE_COLUMN_NAME = false;
    private final static String MAPPER_PACKAGE = "mapper";
    private final static String CLASS_MAPPER_SUFFIX = "Mapper";
    private final static String ENTITY_PACKAGE ="entity.po";
    private final static String ENTITY_SUFFIX = "";
    private final static boolean SERIALIZABLE = true;
    private final static String DELETE_COLUMN = "is_deleted";
    private final static boolean USE_UUID = false;
    private final static String TABLE_PREFIX = "";
    private final static String TABLE_SUFFIX = "";

    private final static String DRIVER_CLASS_NAME = "";
    private final static String JDBC_URL = "";
    private final static String USERNAME = "";
    private final static String PASSWORD = "";
    private final static String DATABASE_NAME = "";
    private final static String TABLES = "";

    private final static String PARENT_PACKAGE="";
    private final static String DESTINATION="";

    private final static boolean REST_FUL = false;
    private final static String CONTROLLER_PACKAGE = "controller.rest";
    private final static String CLASS_CONTROLLER_SUFFIX = "Textuer";
    private final static String SERVICE_PACKAGE = "entity.so";
    private final static String CLASS_SERVICE_SUFFIX = "Entity";

    private final static String VO_PACKAGE = "vo";
    private final static String CLASS_VO_SUFFIX = "VO";

    // 其他文件的生成定义
    private final static String configResource = File.separator + "use"+ File.separator+"config"+File.separator;
    private final static String configCacheResource = File.separator + "use"+ File.separator+"config"+File.separator+"cache"+File.separator;
    private final static String controllerResource = File.separator + "use"+ File.separator+"controller"+File.separator;
    private final static String utilResource = File.separator + "use"+ File.separator+"util"+File.separator;
    private final static String entityResource = File.separator + "use"+ File.separator+"entity"+File.separator;
    private final static String eventResource = File.separator + "use"+ File.separator+"event"+File.separator;

    private final static String serviceLocal = File.separator + "use"+ File.separator+"service"+File.separator+"local"+File.separator;
    private final static String serviceRemote = File.separator + "use"+ File.separator+"service"+File.separator+"remote"+File.separator;


    private final static List<String> useFileList =
            Arrays.asList(
                    configResource+"WebConfiguration.java.txt",
                    configResource+"SSLConfiguration.java.txt",
                    configResource+"TxAdviceInterceptor.java.txt",
                    configResource+"RequestParamBinding.java.txt",
                    configResource+"ErrorPageRouter.java.txt",
                    configResource+"CacheLocalAdvice.java.txt",
                    configResource+"JSON.java.txt",
                    configResource+"Consts.java.txt",
                    configResource+"ErrorOccurHandler.java.txt",
                    configResource+"SwaggerConfiguration.java.txt",
                    configCacheResource+"Cache.java.txt",
                    configCacheResource+"CacheProvider.java.txt",
                    configCacheResource+"GuavaCache.java.txt",
                    configCacheResource+"RedisCache.java.txt",
                    controllerResource+"view"+File.separator+"BaseController.java.txt",
                    controllerResource+"view"+File.separator+"DefaultController.java.txt",
                    controllerResource+"interceptor"+File.separator+"GlobalInterceptor.java.txt",
                    controllerResource+"filter"+File.separator+"SecurityFilter.java.txt",

                    utilResource+"Cookier.java.txt",
                    utilResource+"Download.java.txt",
                    utilResource+"HttpClient.java.txt",
                    utilResource+"HttpInfo.java.txt",
                    utilResource+"ImageBase64.java.txt",
                    utilResource+"MD5.java.txt",
                    utilResource+"RandomStr.java.txt",
                    utilResource+"Redissor.java.txt",
                    utilResource+"XOR.java.txt",
                    utilResource+"Unique.java.txt",

                    entityResource+"Entities.java.txt",
                    entityResource+"Page.java.txt",
                    entityResource+"PageBuilder.java.txt",

                    eventResource+"Event.java.txt",
                    eventResource+"EventCenter.java.txt",
                    eventResource+"EventHandler.java.txt",

                    serviceLocal+"package-info.java.txt",
                    serviceRemote+"package-info.java.txt",

                    File.separator + "use"+File.separator+"application.yml.txt",
                    File.separator + "use"+File.separator+"Application.java.txt",
                    File.separator + "use"+File.separator+"banner.txt.txt"


            );






    /**
     * 加载数据库配置
     * @return
     *
     * TODO 此方法应当是被调用的第一个方法，所以你在IDEA开发工具中看到此处高亮了
     */
    protected static DatabaseConfig loadDatabaseConfig(boolean camel2underline){
        init();
        // 设置数据库表列名是否使用驼峰形式
        configuration.put("camel2underline",camel2underline);
        GeneratorConfig.CAMEL = camel2underline;

        DatabaseConfig databaseConfig = new DatabaseConfig();
        databaseConfig.setDatabase((String)configuration.get("dbName"));
        databaseConfig.setDriver((String)configuration.get("driverClass"));
        databaseConfig.setPassword((String)configuration.get("password"));
        databaseConfig.setUrl((String)configuration.get("jdbcUrl"));
        databaseConfig.setUsername((String)configuration.get("username"));
        databaseConfig.setUseUUID((boolean)configuration.get("uuid"));
        return databaseConfig;
    }

    /**
     * 预生成文件的配置
     * @param table 表名
     * @return
     */
    protected static FileConfig loadFileConfig(String table) {
        checkNull(configuration);
        FileConfig fileConfig = new FileConfig();
        fileConfig.setTable(table);
        fileConfig.setCharset((String)configuration.get("charset"));
        fileConfig.setMapperPackage((String)configuration.get("mapperPackageName"));
        fileConfig.setEntityPackage((String)configuration.get("entityPackageName"));
        fileConfig.setMapperSuffix((String)configuration.get("mapperClassSuffix"));
        fileConfig.setEntitySuffix((String)configuration.get("entitySuffix"));
        fileConfig.setParentPackage((String)configuration.get("packageName"));
        fileConfig.setControllerPackage((String)configuration.get("controllerPackageName"));
        fileConfig.setServicePackage((String)configuration.get("servicePackageName"));
        fileConfig.setControllerSuffix((String)configuration.get("controllerSuffix"));
        fileConfig.setServiceSuffix((String)configuration.get("serviceSuffix"));
        return fileConfig;
    }


    public static FileConfig defaultFileConfig() {
        checkNull(configuration);
        FileConfig fileConfig = new FileConfig();
        fileConfig.setTable(null);
        fileConfig.setCharset((String)configuration.get("charset"));
        fileConfig.setMapperPackage((String)configuration.get("mapperPackageName"));
        fileConfig.setEntityPackage((String)configuration.get("entityPackageName"));
        fileConfig.setMapperSuffix((String)configuration.get("mapperClassSuffix"));
        fileConfig.setEntitySuffix((String)configuration.get("entitySuffix"));
        fileConfig.setParentPackage((String)configuration.get("packageName"));
        fileConfig.setControllerPackage((String)configuration.get("controllerPackageName"));
        fileConfig.setServicePackage((String)configuration.get("servicePackageName"));
        fileConfig.setControllerSuffix((String)configuration.get("controllerSuffix"));
        fileConfig.setServiceSuffix((String)configuration.get("serviceSuffix"));
        return fileConfig;
    }



    /**
     * 将要生成的数据库表
     * 使用英文逗号分隔（tableName1,tableName2,tableName3...）
     * @return
     */
    protected static String tables(){
        checkNull(configuration);
        return (String)configuration.get("tableName");
    }

    /**
     * 将要生成的数据库名称
     * @return
     */
    protected static String database(){
        checkNull(configuration);
        return (String)configuration.get("dbName");
    }


    /**
     * 将要生成文件的父级包名
     * @return
     */
    protected static String parentPackage(){
        checkNull(configuration);
        return (String)configuration.get("packageName");
    }

    /**
     * 将要生成文件的存放路径
     * @return
     */
    protected static String destination(){
        checkNull(configuration);
        String destination = System.getProperty("user.dir")+
                File.separator+"src"+
                File.separator+"main"+
                File.separator+"java"+
                File.separator+
                (configuration.get("packageName").toString().trim().replaceAll("\\.",File.separator));
        if(destination.startsWith("file:")){
            destination = destination.substring(5);
        }
        if(!((String)configuration.get("destination")).trim().equals("")){
            destination = ((String) configuration.get("destination")).trim();
        }
        return destination;
    }


    /**
     * 加载模板文件
     * @return 返回模板文件路径
     */
    protected static String loadTemplateREST(){
            return TEMPLATES_REST;
    }

    protected static String loadTemplateMapper(){
        return TEMPLATES_MAPPERS;
    }







    /**
     * 填充其他参数
     * @return
     */
    protected static Map<Object,Object> param(){
        if(params!=null&&!params.isEmpty()){
            return params;
        }else{
            params = new HashMap<>();
            if(configuration==null || configuration.isEmpty()){
                init();
            }
            params.putAll(configuration);
            return params;
        }

    }


    private static void init(){

        configuration = new HashMap<>();
        configuration.put("charset",GeneratorConfig.CHARSET);
        configuration.put("camel2underline",GeneratorConfig.CAMEL);
        configuration.put("useDbColumn",GeneratorConfig.USE_COLUMN_NAME);
        configuration.put("mapperPackageName",GeneratorConfig.MAPPER_PACKAGE);
        configuration.put("mapperClassSuffix",GeneratorConfig.CLASS_MAPPER_SUFFIX);
        configuration.put("entityPackageName",GeneratorConfig.ENTITY_PACKAGE);
        configuration.put("entitySuffix",GeneratorConfig.ENTITY_SUFFIX);
        configuration.put("serializable",GeneratorConfig.SERIALIZABLE);
        configuration.put("deleteColumn",GeneratorConfig.DELETE_COLUMN);
        configuration.put("uuid",GeneratorConfig.USE_UUID);
        configuration.put("tablePrefix",GeneratorConfig.TABLE_PREFIX);
        configuration.put("tableSuffix",GeneratorConfig.TABLE_SUFFIX);
        configuration.put("driverClass",GeneratorConfig.DRIVER_CLASS_NAME);
        configuration.put("jdbcUrl",GeneratorConfig.JDBC_URL);
        configuration.put("username",GeneratorConfig.USERNAME);
        configuration.put("password",GeneratorConfig.PASSWORD);
        configuration.put("dbName",GeneratorConfig.DATABASE_NAME);
        configuration.put("tableName",GeneratorConfig.TABLES);
        configuration.put("packageName",GeneratorConfig.PARENT_PACKAGE);
        configuration.put("destination",GeneratorConfig.DESTINATION);
        configuration.put("restful",GeneratorConfig.REST_FUL);
        configuration.put("controllerPackageName",GeneratorConfig.CONTROLLER_PACKAGE);
        configuration.put("controllerSuffix",GeneratorConfig.CLASS_CONTROLLER_SUFFIX);
        configuration.put("servicePackageName",GeneratorConfig.SERVICE_PACKAGE);
        configuration.put("serviceSuffix",GeneratorConfig.CLASS_SERVICE_SUFFIX);
        configuration.put("voPackageName",GeneratorConfig.VO_PACKAGE);
        configuration.put("voClassSuffix",GeneratorConfig.CLASS_VO_SUFFIX);
        // 加载配置，并覆盖默认配置
        Properties properties = new Properties();
        try{

            InputStream inputStream = GeneratorConfig.class.getClassLoader().getResourceAsStream("cnf/generator_default.properties");
            properties.load(inputStream);
            Properties self = new Properties();
            self.load(GeneratorConfig.class.getClassLoader().getResourceAsStream("generator.properties"));
            properties.putAll(self);

            configuration.put("charset",properties.get("generator.file.charset"));
            configuration.put("uuid",properties.get("generator.database.use_uuid").toString().trim().equalsIgnoreCase("true"));
            configuration.put("tablePrefix",properties.get("generator.database.table_prefix"));
            configuration.put("tableSuffix",properties.get("generator.database.table_suffix"));
            configuration.put("driverClass",properties.get("generator.database.driver_class"));
            configuration.put("jdbcUrl",properties.get("generator.database.jdbc_url"));
            configuration.put("username",properties.get("generator.database.username"));
            configuration.put("password",properties.get("generator.database.password"));
            configuration.put("dbName",properties.get("generator.database.name"));
            configuration.put("tableName",properties.get("generator.database.tables"));
            configuration.put("packageName",properties.get("generator.file.package"));
            configuration.put("destination",properties.get("generator.file.destination"));

        }catch(Exception e){
            System.out.println("配置文件读取发生错误。");
            e.printStackTrace();

        }

    }

    /**
     * 其他文件生成
     * @return
     */
    public static List<String> useFile(){
        return useFileList;
    }


    private static void checkNull(Object o){
        if(o==null){
            throw new RuntimeException("配置尚未初始化");
        }
    }

}
