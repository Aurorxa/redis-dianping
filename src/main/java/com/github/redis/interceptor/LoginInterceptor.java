package com.github.redis.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.github.redis.utils.UserHolder;
import com.github.redis.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 15:22:49
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserVo user = UserHolder.getUser();

        // 判断 ThreadLocal 中是否有用户，如果没有，直接拦截
        if (ObjectUtil.isEmpty(user)) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
