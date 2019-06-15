package com.glinsoft.mybatis.util;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.log.NullLogChute;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;


public class VelocityReferer {
	
	static {
		// 禁用日志
		org.apache.velocity.app.Velocity.setProperty(org.apache.velocity.app.Velocity.RUNTIME_LOG_LOGSYSTEM, new NullLogChute());
		org.apache.velocity.app.Velocity.init();
	}

	/**
	 * 合并模板与内容，以字符串的形式输出
	 * @param context
	 * @param template
	 * @return
	 */
	public static String merge(VelocityContext context, String template) {
		StringReader reader = new StringReader(template);
		StringWriter writer = new StringWriter();
		org.apache.velocity.app.Velocity.evaluate(context, writer, "mystring", reader);
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
}
