package com.github.redis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.redis.entity.ShopType;
import com.github.redis.mapper.ShopTypeMapper;
import com.github.redis.rest.Result;
import com.github.redis.service.ShopTypeService;
import com.github.redis.utils.RedisConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-12 09:34:52
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {

    @NonNull
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result queryTypeList() {
        // ① 查询缓存中是否有商品类型查询列表
        List<Object> range = this.redisTemplate.opsForList().range(RedisConstants.CACHE_SHOP_TYPE_LIST_KEY_PREFIX, 0, -1);
        // ② 如果缓存中有数据，直接返回给前端
        if (!ObjectUtil.isEmpty(range)) {
            return Result.ok(BeanUtil.copyToList(range, ShopType.class));
        }
        // ③ 查询数据库，获取店铺类型查询列表
        List<ShopType> list = this.list();
        // ④ 如果数据库没有数据，直接返回空信息给前端
        if (CollUtil.isEmpty(list)) {
            return Result.ok();
        }
        // ⑤ 如果数据库中有数据，就将数据写入到 Redis 中，然后返回给前端
        list.forEach(shopType -> {
            this.redisTemplate.opsForList().leftPush(RedisConstants.CACHE_SHOP_TYPE_LIST_KEY_PREFIX, shopType);
        });
        return Result.ok(list);
    }
}
