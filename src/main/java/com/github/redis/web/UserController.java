package com.github.redis.web;

import com.github.redis.dto.LoginFormDto;
import com.github.redis.rest.Result;
import com.github.redis.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 13:42:25
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @NonNull
    private UserService userService;

    /**
     * 发送验证码
     *
     * @param phone   手机号
     * @param session
     * @return
     */
    @PostMapping("/code")
    public Result code(String phone, HttpSession session) {
        return this.userService.sendCode(phone, session);
    }

    /**
     * 登录
     *
     * @param loginFormDto
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDto loginFormDto, HttpSession session) {
        return this.userService.login(loginFormDto, session);
    }

    /**
     * 用户个人信息
     *
     * @return
     */
    @GetMapping("/me")
    public Result me() {
        return this.userService.me();
    }


}
