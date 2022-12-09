package com.github.redis.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 16:03:30
 */
@Data
public class UserVo implements Serializable {

    private Long id;

    private String nickName;

    private String icon;

}
