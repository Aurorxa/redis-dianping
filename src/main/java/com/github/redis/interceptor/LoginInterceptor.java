package com.github.redis.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.github.redis.utils.UserHolder;
import com.github.redis.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 15:22:49
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ① 获取 session
        HttpSession session = request.getSession();

        // ② 从 Session 中获取用户信息
        UserVo userVo = null;
        try {
            userVo = (UserVo) session.getAttribute("user");
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }

        // ③ 如果用户信息不存在，直接返回 false
        if (ObjectUtil.isNull(userVo)) {
            response.setStatus(401);
            return false;
        }

        // ④ 将用户信息保存在 ThreadLocal 中
        UserHolder.saveUser(userVo);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
