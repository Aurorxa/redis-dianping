package com.github.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.redis.entity.ShopType;
import com.github.redis.rest.Result;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-10 22:08:00
 */
public interface ShopTypeService extends IService<ShopType> {

    /**
     * 店铺类型查询列表
     *
     * @return
     */
    Result queryTypeList();

}
