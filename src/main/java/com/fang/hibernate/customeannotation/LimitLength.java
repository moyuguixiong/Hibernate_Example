package com.fang.hibernate.customeannotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义注解的生命周期
 */
@Retention(RetentionPolicy.RUNTIME)
/**
 * 定义注解能够修饰的类型
 */
@Target({ElementType.METHOD, ElementType.TYPE})
/**
 * 该定义让javadoc工具提取文档时，文档中包含该注解
 */
@Documented
/**
 * @Inherited:定义后，使注解具有继承功能，即：父类有注解，继承该父类的子类也具有该注解
 */
public @interface LimitLength {

  /**
   * 定义成员变量，并指定默认值
   */
  int min() default 1;

  int max() default 100;
}
