package com.macro.cloud.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

/**
 * 自定义 Qualifier 注解，用于标记一个 Bean
 *
 * @author Sumkor
 * @since 2023/9/21
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Qualifier
public @interface MyQualifier {
}
