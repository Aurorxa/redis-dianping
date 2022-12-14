package com.github.redis.web;

import com.github.redis.dto.LoginFormDto;
import com.github.redis.rest.Result;
import com.github.redis.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "用户管理", description = "用户管理")
public class UserController {

    @NonNull
    private UserService userService;

    @Operation(summary = "发送验证码", description = "发送验证码", parameters = {@Parameter(name = "phone", description = "手机号码"),})
    @PostMapping("/code")
    public Result code(@RequestParam("phone") String phone, HttpSession session) {
        return this.userService.sendCode(phone, session);
    }

    @Operation(summary = "登录", description = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDto loginFormDto, HttpSession session) {
        return this.userService.login(loginFormDto, session);
    }

    @Operation(summary = "用户个人信息", description = "用户个人信息")
    @GetMapping("/me")
    public Result me() {
        return this.userService.me();
    }

}
