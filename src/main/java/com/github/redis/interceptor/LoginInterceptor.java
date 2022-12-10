package com.github.redis.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.redis.utils.RedisConstants;
import com.github.redis.utils.UserHolder;
import com.github.redis.vo.UserVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;

/**
 * 登录拦截器
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 15:22:49
 */
@Slf4j
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    @NonNull
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ① 从请求头中获取 token
        String token = request.getHeader("Authorization".toLowerCase());
        if (StrUtil.isBlank(token)) {
            response.setStatus(401);
            return false;
        }
        // ② 根据 token 从 Redis 中获取用户信息
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(StrUtil.addPrefixIfNot(token, RedisConstants.LOGIN_TOKEN_KEY_PREFIX));
        // ③ 判断用户信息是否存在
        if (ObjectUtil.isEmpty(entries)) {
            response.setStatus(401);
            return false;
        }
        // ④ 将信息转换为 UserVo 对象
        UserVo userVo;
        try {
            userVo = BeanUtil.fillBeanWithMap(entries, new UserVo(), false);
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }

        // ⑤ 将用户信息保存在 ThreadLocal 中
        UserHolder.saveUser(userVo);

        // ⑥ 刷新 token 的有效期
        redisTemplate.expire(StrUtil.addPrefixIfNot(token, RedisConstants.LOGIN_TOKEN_KEY_PREFIX), Duration.ofMinutes(RedisConstants.LOGIN_TOKEN_TTL));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
