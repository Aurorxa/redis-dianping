package com.github.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.redis.entity.SeckillVoucher;
import com.github.redis.entity.Voucher;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-10 22:08:00
 */
public interface SeckillVoucherService extends IService<SeckillVoucher> {

    /**
     * 新增秒杀卷
     *
     * @param voucher
     */
    void addSeckillVoucher(Voucher voucher);
}
