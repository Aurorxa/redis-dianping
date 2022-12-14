package com.github.redis.web;

import com.github.redis.entity.Voucher;
import com.github.redis.rest.Result;
import com.github.redis.service.SeckillVoucherService;
import com.github.redis.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-14 10:32:10
 */
@Slf4j
@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
@Tag(name = "优惠券管理", description = "优惠券管理")
public class VoucherController {
    @NonNull
    private VoucherService voucherService;
    @NonNull
    private SeckillVoucherService seckillVoucherService;

    @Operation(summary = "新增秒杀卷", description = "新增秒杀卷")
    @PostMapping("/seckill")
    public Result addSeckillVoucher(@RequestBody Voucher voucher) {
        this.seckillVoucherService.addSeckillVoucher(voucher);
        return Result.ok(voucher.getId());
    }

    @Operation(summary = "查询店铺的优惠券列表", description = "查询店铺的优惠券列表")
    @GetMapping("/list/{shopId}")
    public Result queryVoucherOfShop(@PathVariable("shopId") Long shopId) {
        return this.voucherService.queryVoucherOfShop(shopId);
    }

}
