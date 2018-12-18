package com.coffee.saber.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelUtil {
	
	@SuppressWarnings("rawtypes")
	public static Object parseModel(ResultSet rs, Class clazz) {
		/**
		 * 1、 获取model中的所有属性
		 * 2、获取属性的类型
		 * 3、根据类型创建对应的ResultSet 的get方法
		 * 4、获取所有属性所在列
		 * 5、执行get方法
		 */
		Object baseModel = null;
		try {
			baseModel = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			List<Map<String, Method>> modelMethods = new ArrayList<>();
			
			for (Field field : fields) {
				String fieldName = field.getName();
				String methodField = toUpperCaseFirstOne(fieldName);
				Method modelMethod = clazz.getMethod("set"+methodField, field.getType());
				Map<String, Method> map = new HashMap<>();
//				System.out.println("field = " + fieldName);
				map.put(fieldName, modelMethod);
				modelMethods.add(map);
			}
			if (rs.next()) {
				ResultSetMetaData rsMetaData = rs.getMetaData();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					for (int j = 0; j < modelMethods.size(); j++) {
						Map<String, Method> map = modelMethods.get(j);
						String columnName = rsMetaData.getColumnName(i);
						if (columnName.contains("_")) {
							String[] fieldNameSplit = columnName.split("_");
							String newColumnName = fieldNameSplit[0];
							for (int x = 1; x < fieldNameSplit.length; x++) {
								newColumnName += toUpperCaseFirstOne(fieldNameSplit[x]);
							}
							columnName = newColumnName;
						}
//						System.out.println("column = " + columnName);
						if (map.containsKey(columnName)) {
							String classNameWithPackage = rsMetaData.getColumnClassName(i);
							String classNameWithPackageArray[] = classNameWithPackage.split("\\.");
							String className = classNameWithPackageArray[classNameWithPackageArray.length-1];
							if (className.equals("Integer")) {
									className = "Int";
							}
							Method sqlMethod = rs.getClass().getMethod("get"+className, Integer.TYPE);
							Method modelMethod = map.get(columnName);
							modelMethod.invoke(baseModel, sqlMethod.invoke(rs, i));
						}
					}
				}
				return baseModel;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//首字母转大写
	private static String toUpperCaseFirstOne(String s){
	  if(Character.isUpperCase(s.charAt(0)))
	    return s;
	  else
	    return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
}
