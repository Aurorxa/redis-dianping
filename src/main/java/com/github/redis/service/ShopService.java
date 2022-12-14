package com.github.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.redis.entity.Shop;
import com.github.redis.rest.Result;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-10 22:08:00
 */
public interface ShopService extends IService<Shop> {

    /**
     * 查询详情信息
     *
     * @param id
     * @return
     */
    Result view(Long id);

    /**
     * 更新店铺信息
     *
     * @param shop
     * @return
     */
    Result edit(Shop shop);

    /**
     * 根据商铺类型分页查询商铺信息
     *
     * @param typeId
     * @param current
     * @param x
     * @param y
     * @return
     */
    Result queryShopByType(Integer typeId, Integer current, Double x, Double y);
}
