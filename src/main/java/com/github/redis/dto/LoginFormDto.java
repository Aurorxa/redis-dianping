package com.github.redis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 14:42:52
 */
@Data
@Schema(name = "登录参数", description = "登录参数")
public class LoginFormDto implements Serializable {

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "验证码")
    private String code;
}
