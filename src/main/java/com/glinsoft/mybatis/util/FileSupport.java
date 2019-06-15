package com.glinsoft.mybatis.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class FileSupport {

    /**
     * 读取配置文件
     * @param fileName
     * @return 以字符串形式返回全部内容
     */
    public static String readFromClassPath(String fileName) {
        Resource resource = new ClassPathResource(fileName);
        try {
            return IOUtils.toString(resource.getInputStream());
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 写入文件内容
     * @param txt 将要写入的字符串（内容）
     * @param path 文件存储路径
     * @param charset 文件编码格式
     */
    public static void write(String txt, String path, String charset) {
        OutputStreamWriter osw = null;
        try {
            if (StringUtils.isEmpty(charset)) {
                charset = "UTF-8";
            }
            osw = new OutputStreamWriter(new FileOutputStream(path), charset);
            osw.write(txt);
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清理文件夹或文件
     * @param file
     */
    public static void remove(File file) {
        if(file.exists()){
            if (file.isFile()) {
                file.delete();
            }else{
                Arrays.asList(file.listFiles()).forEach(FileSupport::remove);
            }
        }
    }
}
