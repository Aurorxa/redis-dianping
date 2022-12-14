package com.github.redis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.redis.entity.Voucher;
import com.github.redis.mapper.VoucherMapper;
import com.github.redis.rest.Result;
import com.github.redis.service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-10 22:08:24
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher>
        implements VoucherService {


    @Override
    public Result queryVoucherOfShop(Long shopId) {
        // 查询优惠券信息
        LambdaQueryWrapper<Voucher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Voucher::getShopId, shopId);
        List<Voucher> vouchers = this.list(queryWrapper);
        // 返回结果
        return Result.ok(vouchers);
    }
}




