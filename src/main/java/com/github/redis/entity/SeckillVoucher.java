package com.github.redis.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.zerowsh.actable.annotation.AcColumn;
import io.gitee.zerowsh.actable.annotation.AcTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 秒杀优惠券表，与优惠券是一对一关系
 *
 * @TableName tb_seckill_voucher
 */

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 13:27:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_seckill_voucher")
@AcTable(name = "`tb_seckill_voucher`", comment = "秒杀优惠券表")
public class SeckillVoucher implements Serializable {

    @TableId(value = "voucher_id")
    @AcColumn(name = "`voucher_id`", comment = "关联的优惠券的id")
    private Long voucherId;

    @AcColumn(name = "`stock`", comment = "库存")
    private Integer stock;

    @AcColumn(name = "`create_time`", comment = "创建时间")
    private LocalDateTime createTime;

    @AcColumn(name = "`begin_time`", comment = "生效时间")
    private LocalDateTime beginTime;

    @AcColumn(name = "`end_time`", comment = "失效时间")
    private LocalDateTime endTime;

    @AcColumn(name = "`update_time`", comment = "更新时间")
    private LocalDateTime updateTime;


}