package org.example.trigger.http.interceptor;

import io.jsonwebtoken.Claims;
import lombok.var;
import org.example.types.common.UserContext;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.example.types.util.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lanjiajun
 * @description
 * 拦截器负责在 HTTP 请求到达 Controller 之前，
 * 检查 Header 中的 Token，并解析出 UserId 放入 UserContext
 * @create 2025-12-12 22:47
 */
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取 Token
        String authHeader = request.getHeader("Authorization");

        // 简单判断
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ResponseCode.UNAUTHORIZED.getCode(), "未登录或Token缺失");
        }

        String token = authHeader.substring(7); // 去掉 "Bearer "

        // 2. 校验并解析
        try {
            Claims claims = jwtUtil.parseToken(token);
            String userId = (String) claims.get("userId");
            String username = (String) claims.get("username");
            String role = (String) claims.get("role");

            System.out.println(">>> 拦截器解析Token: userId=" + userId + ", role=" + role);

            // 3. 存入 ThreadLocal 上下文
            UserContext.set(userId, username, role);
            return true;
        } catch (Exception e) {
            throw new AppException(ResponseCode.UNAUTHORIZED.getCode(), "Token无效或已过期");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {
        // 请求结束后清理 ThreadLocal，防止内存泄漏
        UserContext.remove();
    }
}
