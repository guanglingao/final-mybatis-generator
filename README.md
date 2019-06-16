## 支持的数据库

#### MySQL 、 MS-SQLServer


## 〇、为什么使用final-mybatis

- 无需mybatis映射xml文件配置SQL查询
- 链式SQL支持
- 代码自动生成工具


## 一、用法

- 新建SpringBoot项目


- 在pom.xml中添加依赖

```        
    <dependency>
        <groupId>com.github.glinsoft</groupId>
        <artifactId>final-mybatis-generator</artifactId>
        <version>0.0.7</version>
    </dependency>
```    
  
   
- 创建数据库连接配置文件resources/generator.properties （必须使用此文件名）    

  简洁配置请参考：
```
   
    generator.database.driver_class=com.mysql.cj.jdbc.Driver
    generator.database.name=db_weizhan
    generator.database.jdbc_url=jdbc:mysql://localhost:3306/db_weizhan?useUnicode=true&characterEncoding=utf-8
    generator.database.username=root
    generator.database.password=123456
    generator.database.table_prefix=tb_   
    generator.file.package=com.zhisland.bigant

```     
     
  完整配置请参考：
```
    
    ## 数据库启动类
    generator.database.driver_class=
    ## 数据库名称
    generator.database.name=
    ## JDBC连接URL
    generator.database.jdbc_url=
    ## 数据库登录用户名
    generator.database.username=
    ## 数据库登录密码
    generator.database.password=
    ## 主键是否使用uuid，默认为id
    generator.database.use_uuid=false
    ## 表名前缀，例如：tb_user,为『tb_』,生成类文件时将与删除
    generator.database.table_prefix=
    ## 表名后缀，生成类文件时将与删除
    generator.database.table_suffix=
    ## 将要生成的表，设置为空表示生成数据库所有表
    generator.database.tables=
    ## 生成JAVA文件使用的编码
    generator.file.charset=UTF-8
    ## 生成文件所在的包名称
    generator.file.package=
    ## 文件存放的本地路径，默认为: Idea的src响应目录，生成过程将首先清空该路径
    generator.file.destination=

   
    
```    
- 在 SpringBoot的 main方法中执行，如下：

```    
        . . . 
    import FinalMyBatisGenerator;
        . . . 
      
    public static void main(String ... args){
        FinalMyBatisGenerator generator = new FinalMyBatisGenerator();
        generator.generateRESTs(); // 生成完整的RESTful结构和文件
        // generator.generateMappersOnly();  只生成Entity与Mapper文件
        . . .
    }
    

    
```    
- 运行 main 方法后，将在 out目录中生成 mapper、entity、service、controller 的java文件
- generateRESTs() 生成全部REST结构；generateMappersOnly() 只生成指定的表对应的Mapper和Entity；

## 二、配置其他依赖，并调整其他已生成文件的位置

- 调整application.yml的位置
- 数据库驱动依赖等；

## 三、生成项目已经自动启用Swagger，启动项目可使用API文档

## 四、 [final-mybatis详细用法请参见此链接](https://github.com/glinsoft/final-mybatis/)

### API文档访问：http://localhost:8080/doc







