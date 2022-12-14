package com.github.redis.web;

import com.github.redis.entity.Shop;
import com.github.redis.rest.Result;
import com.github.redis.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-10 22:07:16
 */
@Slf4j
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
@Tag(name = "商铺管理", description = "商铺管理")
public class ShopController {

    @NonNull
    private ShopService shopService;

    @Operation(summary = "查询商铺详情", description = "查询商铺详情", parameters = {
            @Parameter(name = "id", description = "主键", in = ParameterIn.PATH),
    })
    @GetMapping("/{id}")
    public Result view(@PathVariable("id") Long id) {
        return this.shopService.view(id);
    }

    @Operation(summary = "更新店铺信息", description = "更新店铺信息")
    @PutMapping
    public Result edit(@RequestBody Shop shop) {
        return this.shopService.edit(shop);
    }


    @Operation(summary = "根据商铺类型分页查询商铺信息", description = "根据商铺类型分页查询商铺信息",
            parameters = {
                    @Parameter(name = "typeId", description = "商铺类型"),
                    @Parameter(name = "current", description = "页码"),
                    @Parameter(name = "x", description = "经度"),
                    @Parameter(name = "y", description = "纬度"),
            }
    )
    @GetMapping("/of/type")
    public Result queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "x", required = false) Double x,
            @RequestParam(value = "y", required = false) Double y) {
        return this.shopService.queryShopByType(typeId, current, x, y);
    }


}
