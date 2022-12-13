package com.github.redis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.redis.config.RedisBloomFilter;
import com.github.redis.dto.RedisLogicExpireDto;
import com.github.redis.entity.Shop;
import com.github.redis.mapper.ShopMapper;
import com.github.redis.rest.Result;
import com.github.redis.service.ShopService;
import com.github.redis.utils.BloomFilterHelper;
import com.github.redis.utils.CacheClient;
import com.github.redis.utils.RedisConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-10 22:08:24
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
    @NonNull
    private RedisTemplate<String, Object> redisTemplate;
    @NonNull
    private StringRedisTemplate stringRedisTemplate;
    @NonNull
    private RedisBloomFilter redisBloomFilter;
    @NonNull
    private BloomFilterHelper bloomFilterHelper;

    @NonNull
    private CacheClient cacheClient;

    @Override
    public Result view(Long id) {
        // Shop shop = this.viewWithLogicExpire(id);
        Shop shop = this.cacheClient.getWithPassThrough(RedisConstants.CACHE_SHOP_KEY_PREFIX, id, Shop.class, this::getById, RedisConstants.CACHE_SHOP_KEY_TTL, TimeUnit.MINUTES);
        if (ObjectUtil.isEmpty(shop)) {
            return Result.fail("店铺不存在");
        }
        return Result.ok(shop);
    }

    /**
     * 基于逻辑过期解决缓存击穿问题
     *
     * @return
     */
    private Shop viewWithLogicExpire(Long id) {
        Map<Object, Object> entries = this.redisTemplate.opsForHash().entries(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.CACHE_SHOP_KEY_PREFIX));
        // 如果缓存中没有数据，直接返回
        if (ObjectUtil.isEmpty(entries)) {
            return null;
        }
        // 判断缓存中的逻辑过期是否过期
        RedisLogicExpireDto<?> shopRedisLogicExpireDto = BeanUtil.fillBeanWithMap(entries, new RedisLogicExpireDto<>(), false);
        LocalDateTime expireTime = shopRedisLogicExpireDto.getExpireTime();
        Object data = shopRedisLogicExpireDto.getData();
        System.out.println("data = " + data);
        Shop shop = BeanUtil.fillBeanWithMap((Map<?, ?>) data, new Shop(), true);
        // 如果缓存中的逻辑过期在此刻之后，那么就认为没有过期，则返回商铺信息；否则就认为过期，过期就需要获取互斥锁，并判断互斥锁能够获取
        if (expireTime.isAfter(LocalDateTime.now())) {
            return shop;
        }
        // 如果逻辑过期已经过期了，则需要获取互斥锁，如果获取不到，返回过期的商铺信息；如果获取到了，返回过期的商铺信息，并开启新的线程去将商品信息更新到 Redis 中，并释放锁
        boolean isLock = this.tryLock(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.LOCK_SHOP_KEY_PREFIX));
        if (!isLock) {
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 重建缓存
                    this.saveShop2Redis(id, 50L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    this.unlock(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.LOCK_SHOP_KEY_PREFIX));
                }
            });
        }
        return shop;
    }


    /**
     * 基于互斥锁解决缓存击穿问题
     *
     * @param id
     * @return
     */
    private Shop viewWithMutex(Long id) {
        log.info("ShopServiceImpl.view {}", id);
        // 使用布隆过滤器，需要注意新增业务数据的时候需要将数据维护到布隆过滤器中
     /*   boolean b = this.redisBloomFilter.includeByBloomFilter(this.bloomFilterHelper, StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.CACHE_SHOP_BLOOM_KEY_PREFIX), String.valueOf(id));
        if (!b) {
            return Result.fail("该店铺不存在！");
        }*/
        // ① 根据商铺 id 从 Redis 中查询商铺的缓存
        Map<Object, Object> entries = this.redisTemplate.opsForHash().entries(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.CACHE_SHOP_KEY_PREFIX));
        // ② 如果 Redis 中存在，直接将信息返回给前端
        if (!ObjectUtil.isEmpty(entries)) {
            return BeanUtil.fillBeanWithMap(entries, new Shop(), false);
        }
        // ③ 实现缓存重建
        // 获取互斥锁，并判断是否获取成功，如果成功，则查询数据库，并将数据写入到 Redis 中，并释放锁；如果失败，则休眠并重试
        Shop shop;
        try {
            boolean isLock = this.tryLock(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.LOCK_SHOP_KEY_PREFIX));
            if (!isLock) {
                Thread.sleep(50);
                return this.viewWithMutex(id);
            }
            // 如果 Redis 中没有找到商铺的缓存，就需要查询数据库
            shop = this.getById(id);
            // 模拟重建的延迟
            Thread.sleep(1000);
            // 如果数据库中不存在，就返回错误信息
            if (ObjectUtil.isEmpty(shop)) {
                return null;
            }
            // 如果商铺存在，就将商铺信息写入到 Redis 中，并将商铺信息返回给前端
            this.redisTemplate.opsForHash().putAll(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.CACHE_SHOP_KEY_PREFIX), BeanUtil.beanToMap(shop));
            // 设置过期时间为 30 分钟
            this.redisTemplate.expire(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.CACHE_SHOP_KEY_PREFIX), Duration.ofMinutes(RedisConstants.CACHE_SHOP_KEY_TTL));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放锁
            this.unlock(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.LOCK_SHOP_KEY_PREFIX));
        }
        return shop;
    }


    /**
     * 对缓存进行预热
     *
     * @param id
     * @param expireSeconds
     */
    public void saveShop2Redis(Long id, Long expireSeconds) {
        // 查询店铺数据
        Shop shop = this.getById(id);
        // 封装逻辑过期时间
        RedisLogicExpireDto<Shop> redisLogicExpireDto = new RedisLogicExpireDto<>();
        redisLogicExpireDto.setData(shop);
        redisLogicExpireDto.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
        // 写入 Redis
        this.redisTemplate.opsForHash().putAll(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.CACHE_SHOP_KEY_PREFIX), BeanUtil.beanToMap(redisLogicExpireDto));
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

    @Override
    public Result edit(Shop shop) {
        // 先操作数据库
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("店铺id不能为空");
        }
        this.updateById(shop);
        // 再删除缓存
        this.redisTemplate.delete(StrUtil.addPrefixIfNot(String.valueOf(shop.getId()), RedisConstants.CACHE_SHOP_KEY_PREFIX));
        return Result.ok();
    }
}
