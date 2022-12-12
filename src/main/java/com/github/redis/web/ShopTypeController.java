package com.github.redis.web;

import com.github.redis.rest.Result;
import com.github.redis.service.ShopTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-12 09:19:57
 */
@Slf4j
@RestController
@RequestMapping("/shop-type")
@RequiredArgsConstructor
public class ShopTypeController {

    @NonNull
    private ShopTypeService shopTypeService;

    /**
     * 店铺类型查询列表
     */
    @GetMapping("/list")
    public Result queryTypeList() {
        return this.shopTypeService.queryTypeList();
    }

}
