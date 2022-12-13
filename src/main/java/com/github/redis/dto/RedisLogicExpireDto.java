package com.github.redis.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Redis 的逻辑过期的数据
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-13 13:16:37
 */
@Data
public class RedisLogicExpireDto<T> {

    private LocalDateTime expireTime;

    private T data;

}
