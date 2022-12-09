package com.github.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.redis.dto.LoginFormDto;
import com.github.redis.entity.User;
import com.github.redis.rest.Result;

import javax.servlet.http.HttpSession;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 13:45:19
 */
public interface UserService extends IService<User> {

    /**
     * 发送验证码
     *
     * @param phone
     * @param session
     * @return
     */
    Result sendCode(String phone, HttpSession session);

    /**
     * 登录
     *
     * @param loginFormDto 登录的信息
     * @param session
     * @return
     */
    Result login(LoginFormDto loginFormDto, HttpSession session);

    /**
     * 用户个人信息
     *
     * @return
     */
    Result me();
}
