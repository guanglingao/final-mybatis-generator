package com.glinsoft.mybatis.util;

import de.hunsicker.jalopy.Jalopy;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * 代码格式化
 * @author tanghc
 */
public class StringFormat {
    
    public static String formatJava(String input) {
        try {
            StringBuffer output = new StringBuffer();
            Jalopy j = new Jalopy();
            j.setEncoding("UTF-8");
            j.setInput(input, "StringFormatToJava.java");
            j.setOutput(output);
            j.format();
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("------------------------------------------------------------------------");
            System.out.println(input);
            return input;
        }
    }

    public static String formatXml(String input) {
        try{
            SAXReader reader = new SAXReader();
            StringReader in = new StringReader(input);
            Document doc = reader.read(in);
            OutputFormat formater = OutputFormat.createPrettyPrint();
            formater.setEncoding("utf-8");
            formater.setIndent("    ");
            StringWriter out = new StringWriter();
            XMLWriter writer = new XMLWriter(out, formater);
            writer.write(doc);
            writer.close();
            return out.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }
    
    
    
}
