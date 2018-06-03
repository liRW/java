package com.rw.common.utils.often;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.rw.common.utils.annotation.AttributeAnnotation;
import com.rw.common.utils.model.ClassUtilsModel;

/**
 * 类工具类
 * 
 * @author lrw
 *
 */
public class ClassUtils<T> {

	private final static Logger logger = LoggerFactory.getLogger(ClassUtils.class);

	private Class<T> clazz;

	public ClassUtils(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 根据属性名获取属性值
	 */
	public static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("属性名{},获取值失败", fieldName);
			return null;
		}
	}

	/**
	 * 给属性赋值-- 专门写给评分用的 后续再改造 写成通用的 换注解极即可 后续改造
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public T setFieldValueByName(Map<String, Object> data) throws Exception

	{
		T entity = null;
		Field[] allFields = clazz.getDeclaredFields();
		for (Field field : allFields) {

			AttributeAnnotation evaluationResultsAnnotation = field.getAnnotation(AttributeAnnotation.class);
			// 屏蔽没有注解的
			if (null != evaluationResultsAnnotation) {
				// 获取注解 -->分数data的key
				String name = evaluationResultsAnnotation.name();
				entity = (entity == null ? clazz.newInstance() : entity);
				// 取得类型,并根据对象类型设置值.
				Class<?> fieldType = field.getType();
				if (fieldType == null) {
					continue;
				}
				// 字符串
				String scoreString = (null == data.get(name) || "".equals(data.get(name))) ? ""
						: data.get(name).toString();
				// 数字处理
				String score = (null == data.get(name) || "".equals(data.get(name))) ? "0" : data.get(name).toString();
				String reportDesc = evaluationResultsAnnotation.reportDesc().trim();// 注解的优先级
																					// 比较高
				if (!"".equals(reportDesc) && !"".equals(scoreString)) {
					JSONObject obj = JSONObject.parseObject(reportDesc);
					String result = obj.getString(scoreString);
					if (null != result) {
						scoreString = result;
					}
				}
				if (String.class == fieldType) {
					field.set(entity, String.valueOf(scoreString));
				} else if (BigDecimal.class == fieldType) {
					field.set(entity, BigDecimal.valueOf(Double.valueOf(score)));
				} else if (Date.class == fieldType) {
					//SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					//时间要处理一下
				} else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
					field.set(entity, Integer.parseInt(score));
				} else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
					field.set(entity, Long.valueOf(score));
				} else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
					field.set(entity, Float.valueOf(score));
				} else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
					field.set(entity, Short.valueOf(score));
				} else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
				} else if (Character.TYPE == fieldType) {
					if ((score != null) && (score.length() > 0)) {
						field.set(entity, Character.valueOf(score.charAt(0)));
					}
				}
			}
		}

		return entity;

	}

	/**
	 * 获取属性名数组
	 */
	/*
	 * public static String[] getFiledName(Object o) { Field[] fields =
	 * o.getClass().getDeclaredFields(); String[] fieldNames = new
	 * String[fields.length]; for (int i = 0; i < fields.length; i++) {
	 * ThirdPartyReport thirdPartyReport = fields[i]
	 * .getAnnotation(ThirdPartyReport.class); if(null==thirdPartyReport){
	 * fieldNames[i] = fields[i].getName(); } } return fieldNames; }
	 */

	/**
	 * 获取计分项目
	 * 
	 * @return
	 */
	public static Map<String, Object> getGraded(Object model) {
		Map<String, Object> gradedMap = new HashMap<String, Object>();

		Field[] fields = model.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			AttributeAnnotation thirdPartyReport = fields[i].getAnnotation(AttributeAnnotation.class);
			if (null != thirdPartyReport) {
				Object value = getFieldValueByName(fields[i].getName(), model);
				gradedMap.put(fields[i].getName(), value);
			}
		}
		return gradedMap;
	}

	public static void main(String[] args) throws Exception {		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("N201", "0");// 测试注解的优先级
		data.put("N202", "哈哈哈000000");
		data.put("N203", 1213232);
		ClassUtilsModel model = new ClassUtils<ClassUtilsModel>(ClassUtilsModel.class).setFieldValueByName(data);
        logger.info("model---->{}",model);
		
		
		ClassUtilsModel info = new ClassUtilsModel();
		info.setN201("哈哈");
		info.setN202("哈哈");
		info.setN203(123);
		Map<String, Object> returnMap = getGraded(info);
		logger.info("获取有助解的属性和值：{}", returnMap);

	}

}
