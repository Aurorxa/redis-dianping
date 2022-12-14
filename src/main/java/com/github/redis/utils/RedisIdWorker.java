package com.github.redis.utils;

import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-13 17:24:19
 */
@Component
@RequiredArgsConstructor
public class RedisIdWorker {

    /**
     * 起始时间
     */
    private static final long BEGIN_TIMESTAMP = 1670952615L;

    /**
     * 移动的位数
     */
    private static final int COUNT_BITS = 32;

    @NonNull
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 全局 ID，格式是：符号位+时间戳+序列号
     *
     * @param prefixKey 业务前缀
     * @return
     */
    public long nextId(String prefixKey) {
        // ① 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // ② 生成序列号 icr:product:20221214
        String key = StrUtil.join(":", "icr", now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd")));
        long increment = Objects.requireNonNull(this.stringRedisTemplate.opsForValue().increment(key));
        // ③ 拼接返回
        return timestamp << COUNT_BITS | increment;
    }


}
