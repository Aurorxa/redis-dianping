package com.github.redis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.redis.entity.SeckillVoucher;
import com.github.redis.entity.Voucher;
import com.github.redis.mapper.SeckillVoucherMapper;
import com.github.redis.mapper.VoucherMapper;
import com.github.redis.service.SeckillVoucherService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-10 22:08:24
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class SeckillVoucherServiceImpl extends ServiceImpl<SeckillVoucherMapper, SeckillVoucher>
        implements SeckillVoucherService {

    @NonNull
    private VoucherMapper voucherMapper;

    @Override
    public void addSeckillVoucher(Voucher voucher) {
        // 保存优惠卷
        this.voucherMapper.insert(voucher);
        // 保存秒杀卷信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        this.save(seckillVoucher);
    }
}
