package com.glinsoft.mybatis.util;

import org.apache.commons.lang.StringUtils;

public class FieldName {


	/**
	 * 将下划线的写法转换为驼峰形式
	 * @param field
	 * @return
	 */
	public static String underlineToCamel(String field) {
		if(StringUtils.isEmpty(field)) {
			throw new NullPointerException("public static String underlineToCamel(String field) field is null");
		}
		
		if (field.contains("_")) {
			field = field.toLowerCase();
			StringBuilder sb = new StringBuilder(field);
			while (sb.indexOf("_") > -1) {
				int index = sb.indexOf("_");
				String upperLetter = sb.substring(index + 1, index + 2).toUpperCase();
				sb.replace(index + 1, index + 2, upperLetter);
				sb.deleteCharAt(index);
			}
			return sb.toString();
		} else {
			return field;
		}
	}

	/**
	 * 将带有点号（.）的写法转换为驼峰命名
	 * @param field
	 * @return
	 */
	public static String removePreviousDot(String field) {
		if(field.indexOf(".") > 0) {
			return field.substring(field.indexOf(".") + 1);
		}
		return field;
	}
	
	/**
	 * 过滤"."
	 * 
	 * @param field
	 * @return
	 */
	public static String dotToCamel(String field) {
		if (StringUtils.isNotEmpty(field)) {
			if (field.indexOf(".") > -1) {
				String[] words = field.split("\\.");
				String ret = "";
				for (String str : words) {
					ret += upperFirstLetter(str);
				}
				return ret;
			}
		}
		return field;
	}

	/**
	 * 将第一个字母转换成大写
	 * 
	 * @param str
	 * @return
	 */
	public static String upperFirstLetter(String str) {
		if (StringUtils.isNotEmpty(str)) {
			String firstUpper = str.substring(0, 1).toUpperCase();
			str = firstUpper + str.substring(1, str.length());
		}
		return str;
	}

	/**
	 * 将第一个字母转换成小写
	 * 
	 * @param str
	 * @return
	 */
	public static String lowerFirstLetter(String str) {
		if (StringUtils.isNotEmpty(str)) {
			String firstLower = str.substring(0, 1).toLowerCase();
			str = firstLower + str.substring(1, str.length());
		}
		return str;
	}


}
