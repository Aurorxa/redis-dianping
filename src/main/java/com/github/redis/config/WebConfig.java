package com.github.redis.config;

import com.github.redis.interceptor.LoginInterceptor;
import com.github.redis.interceptor.RefreshTokenInterceptor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 15:25:45
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @NonNull
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 刷新 Token 拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(this.redisTemplate)).addPathPatterns("/**").excludePathPatterns("/images/**", "/swagger**/**", "/**/api-docs/**", "/webjars/**", "/voucher/**").order(1);

        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor()).excludePathPatterns("/shop/**", "/shop-type/**", "/blog/hot", "/user/login", "/user/code", "/images/**", "/swagger**/**", "/**/api-docs/**", "/webjars/**", "/voucher/**").order(2);
    }

}
