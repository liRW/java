package com.rw.common.utils.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mr. Li
 * @Description:Excel配置注解自定义接口
 * @Date: Created in 16:55 2018/5/6
 * @Modified By:
 * 注解类 方法返回值必须为primitive类型、Class类型、枚举类型、Annotation类型或者由前面类型之一作为元素的一位数组
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface ExcelAttribute {
    /**
     * Excel中的列名
     *
     * @return
     */
    public abstract String name();

    /**
     * 列名对应的A,B,C,D...,不指定按照默认顺序排序
     *
     * @return
     */
    public abstract String column() default "";

    /**
     * 提示信息
     *
     * @return
     */
    public abstract String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容
     *
     * @return
     */
    public abstract String[] combo() default {};

    /**
     * 是否导出数据
     *
     * @return
     */
    public abstract boolean isExport() default true;

    /**
     * 是否为重要字段（整列标红,着重显示）
     *
     * @return
     */
    public abstract boolean isMark() default false;

    /**
     * 是否合计当前列
     *
     * @return
     */
    public abstract boolean isSum() default false;


}