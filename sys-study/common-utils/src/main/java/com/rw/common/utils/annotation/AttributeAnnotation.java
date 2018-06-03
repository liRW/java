package com.rw.common.utils.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 综合评分注解
 * @author lrw
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface AttributeAnnotation {
	/**
	 * 映射的字段
	 * @return
	 */
	 public abstract String name();
	 
	 /**
	  * 描述
	  * @return
	  */
	 public abstract String desc();
	 
	 /**
	  * 根据报告转义中文  如在大黄蜂返回 0命中 1 不命中 2 未知   注解{'0':'实名认证','1':'实名验证失败','2':''}
	  * 如果属性设置有reportDesc并且能找到对应关系  优先级优先于name设置的值
	  * @return
	  */
	 public abstract String reportDesc() default "";

}
