package com.github.redis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.redis.entity.Shop;
import com.github.redis.mapper.ShopMapper;
import com.github.redis.rest.Result;
import com.github.redis.service.ShopService;
import com.github.redis.utils.RedisConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;

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

    @NonNull
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result view(Long id) {
        // ① 根据商铺 id 从 Redis 中查询商铺的缓存
        Map<Object, Object> entries = this.redisTemplate.opsForHash().entries(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.CACHE_SHOP_KEY_PREFIX));
        // ② 如果 Redis 中存在，直接将信息返回给前端
        if (!ObjectUtil.isEmpty(entries)) {
            return Result.ok(BeanUtil.fillBeanWithMap(entries, new Shop(), false));
        }
        // ③ 如果 Redis 中没有找到商铺的缓存，就需要查询数据库
        Shop shop = this.getById(id);
        // ④ 如果数据库中不存在，就返回错误信息
        if (ObjectUtil.isEmpty(shop)) {
            return Result.fail("该店铺不存在！");
        }
        // ⑤ 如果商铺存在，就将商铺信息写入到 Redis 中，并将商铺信息返回给前端
        this.redisTemplate.opsForHash().putAll(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.CACHE_SHOP_KEY_PREFIX), BeanUtil.beanToMap(shop));
        // 设置过期时间为 30 分钟
        this.redisTemplate.expire(StrUtil.addPrefixIfNot(String.valueOf(id), RedisConstants.CACHE_SHOP_KEY_PREFIX), Duration.ofMinutes(RedisConstants.CACHE_SHOP_KEY_TTL));
        return Result.ok(shop);
    }

    @Override
    public Result edit(Shop shop) {
        // 先操作数据库再删除缓存
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("店铺id不能为空");
        }
        this.updateById(shop);
        this.redisTemplate.delete(StrUtil.addPrefixIfNot(String.valueOf(shop.getId()), RedisConstants.CACHE_SHOP_KEY_PREFIX));
        return Result.ok();
    }
}
