package com.github.redis.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.redis.dto.RedisLogicExpireDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-13 15:19:56
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheClient {

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
    @NonNull
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据指定的 key 查询缓存，并反序列化为指定类型，利用缓存空值的方法解决缓存穿透问题
     *
     * @param prefixKey  缓存的 key 前置
     * @param id         主键
     * @param clazz      返回值实体的类型
     * @param dbFallback
     * @param expireTime 过期时间
     * @param timeUnit   过期时间的单位
     * @param <R>        返回值类型
     * @param <ID>       主键类型
     * @return
     */
    public <R, ID> R getWithPassThrough(String prefixKey, ID id, Class<R> clazz, Function<ID, R> dbFallback, Long expireTime, TimeUnit timeUnit) {
        String key = prefixKey + id;
        String json = this.stringRedisTemplate.opsForValue().get(key);
        // 判断是否存在，并判断是否为空值
        if (StrUtil.isNotBlank(json)) {
            // 如果不是空值，直接返回
            return JSONUtil.toBean(json, clazz);
        }
        // 判断命中的值为空值
        if ("".equals(json)) {
            return null;
        }
        // 如果不存在，则查询数据库
        R result = dbFallback.apply(id);
        // 如果数据库中不存在，将空值写入到 Redis 中，并返回 null
        if (ObjectUtil.isEmpty(result)) {
            this.stringRedisTemplate.opsForValue().set(key, "", expireTime, timeUnit);
            return null;
        }
        this.set(key, result, expireTime, timeUnit);
        return result;
    }


    /**
     * 根据指定的 key 查询缓存，并反序列化为指定类型，需要利用逻辑过期解决缓存击穿问题
     *
     * @param prefixKey  缓存的 key 前缀
     * @param id         主键
     * @param clazz      返回值实体的类型
     * @param dbFallback
     * @param expireTime 过期时间
     * @param timeUnit   过期时间的单位
     * @param <R>        返回值类型
     * @param <ID>       主键类型
     * @return
     */
    public <R, ID> R getWithLogicExpire(String prefixKey, ID id, Class<R> clazz, Function<ID, R> dbFallback, Long expireTime, TimeUnit timeUnit, String lockKeyPrefix) {
        String key = prefixKey + id;
        String json = this.stringRedisTemplate.opsForValue().get(key);
        // 如果缓存中没有数据，直接返回
        if (StrUtil.isBlank(json)) {
            return null;
        }
        // 判断缓存中的逻辑过期是否过期
        RedisLogicExpireDto redisLogicExpireDto = JSONUtil.toBean(json, RedisLogicExpireDto.class);
        R result = JSONUtil.toBean((JSONObject) redisLogicExpireDto.getData(), clazz);
        LocalDateTime actualExpireTime = redisLogicExpireDto.getExpireTime();
        // 如果缓存中的逻辑过期在此刻之后，那么就认为没有过期，则返回商铺信息；否则就认为过期，过期就需要获取互斥锁，并判断互斥锁能够获取
        if (actualExpireTime.isAfter(LocalDateTime.now())) {
            return result;
        }
        // 如果逻辑过期已经过期了，则需要获取互斥锁，如果获取不到，返回过期的商铺信息；如果获取到了，返回过期的商铺信息，并开启新的线程去将商品信息更新到 Redis 中，并释放锁
        boolean isLock = this.tryLock(StrUtil.addPrefixIfNot(String.valueOf(id), lockKeyPrefix));
        if (!isLock) {
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 查数据库
                    R apply = dbFallback.apply(id);
                    // 写入 Redis
                    this.setWithLogicExpire(key, apply, expireTime, timeUnit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    this.unlock(StrUtil.addPrefixIfNot(String.valueOf(id), lockKeyPrefix));
                }
            });
        }
        return result;
    }

    /**
     * 获取锁
     *
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        Boolean aBoolean = this.stringRedisTemplate.opsForValue().setIfAbsent(key, String.valueOf(Thread.currentThread().getId()), Duration.ofSeconds(10));
        return BooleanUtil.isTrue(aBoolean);
    }

    /**
     * 释放锁
     *
     * @param key
     */
    private void unlock(String key) {
        this.stringRedisTemplate.delete(key);
    }

    /**
     * 将任意的 Java 对象序列化成 JSON 并存储在 string 类型的 key 中，并可以设置 TTL 过期时间
     *
     * @param key
     * @param value
     * @param expireTime
     * @param timeUnit
     */
    public void set(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        this.stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), expireTime, timeUnit);
    }

    /**
     * 将任意的 Java 对象序列化成 JSON 并存储在 string 类型的 key 中，并且可以设置逻辑过期时间，用于处理缓存击穿问题
     *
     * @param key
     * @param value
     * @param expireTime
     * @param timeUnit
     */
    public void setWithLogicExpire(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        // 设置逻辑过期
        RedisLogicExpireDto<Object> redisLogicExpireDto = new RedisLogicExpireDto<>();
        redisLogicExpireDto.setData(value);
        redisLogicExpireDto.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(expireTime)));
        this.stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisLogicExpireDto));
    }

}
