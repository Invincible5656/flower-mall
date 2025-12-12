package org.example.config;

import org.example.trigger.http.interceptor.JwtInterceptor;
import org.example.types.util.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author lanjiajun
 * @description
 * 配置拦截路径（排除登录、注册接口）
 * @create 2025-12-12 22:54
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(jwtUtil))
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns(
                        "/api/user/login",     // 排除登录
                        "/api/user/register",  // 排除注册
                        "/api/flower/list",      // 查看商品不需要登录
                        "/api/species/list"
                );
    }
}

