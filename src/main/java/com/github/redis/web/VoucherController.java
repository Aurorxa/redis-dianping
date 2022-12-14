package com.github.redis.web;

import com.github.redis.entity.Voucher;
import com.github.redis.rest.Result;
import com.github.redis.service.SeckillVoucherService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-14 10:32:10
 */
@Slf4j
@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
public class VoucherController {

    @NonNull
    private SeckillVoucherService seckillVoucherService;


    /**
     * 新增秒杀卷
     *
     * @param voucher 优惠券的信息
     * @return 优惠卷id
     */
    @PostMapping("/seckill")
    public Result addSeckillVoucher(@RequestBody Voucher voucher) {
        this.seckillVoucherService.addSeckillVoucher(voucher);
        return Result.ok(voucher.getId());
    }

}
