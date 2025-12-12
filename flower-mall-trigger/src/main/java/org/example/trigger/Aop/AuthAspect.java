package org.example.trigger.Aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.types.common.UserContext;
import org.example.types.exception.AppException;
import org.springframework.stereotype.Component;

/**
 * @author lanjiajun
 * @description
 * 编写切面,完成注解开发
 * 面向切面编程！！！AOP
 * @create 2025-12-12 23:48
 */
@Aspect
@Component
public class AuthAspect {
    @Around("@annotation(org.example.types.Annotaton.AdminOnly)")
    public Object checkAdmin(ProceedingJoinPoint point) throws Throwable {
        // 1. 获取当前用户角色
        String role = UserContext.getRole();

        // 2. 鉴权
        if (!"admin".equals(role)) {
            // 抛出 403 禁止访问异常
            throw new AppException("4003", "无权访问，仅限管理员操作");
        }

        // 3. 放行
        return point.proceed();
    }
}
