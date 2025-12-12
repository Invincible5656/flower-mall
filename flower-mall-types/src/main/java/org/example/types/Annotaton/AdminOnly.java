package org.example.types.Annotaton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lanjiajun
 * @description
 * 自动拦截非管理员。
 * 实现登录鉴权；
 * 最开始时候忘了这码事，
 * 暂时用个注解把只能管理员用的接口管理起来吧？
 * 这个方法也是第一次使用，问AI解决的。
 * @create 2025-12-12 23:45
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminOnly {
}
