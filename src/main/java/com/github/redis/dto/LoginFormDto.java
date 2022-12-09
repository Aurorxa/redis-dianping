package com.github.redis.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 14:42:52
 */
@Data
public class LoginFormDto implements Serializable {

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;
}
