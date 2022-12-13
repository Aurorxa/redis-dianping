package com.github.redis.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.zerowsh.actable.annotation.AcColumn;
import io.gitee.zerowsh.actable.annotation.AcTable;
import io.gitee.zerowsh.actable.emnus.ColumnTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 13:32:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_voucher_order")
@AcTable(name = "`tb_voucher_order`", comment = "优惠券的订单表")
public class VoucherOrder implements Serializable {

    @TableId(value = "id")
    @AcColumn(name = "`id`", comment = "主键")
    private Long id;

    @AcColumn(name = "`user_id`", comment = "下单的用户id")
    private Long userId;

    @AcColumn(name = "`voucher_id`", comment = "购买的代金券id")
    private Long voucherId;

    @AcColumn(name = "`pay_type`", comment = "支付方式 1：余额支付；2：支付宝；3：微信")
    private Integer payType;

    @AcColumn(name = "`status`", comment = "订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款")
    private Integer status;

    @AcColumn(name = "`create_time`", comment = "下单时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime createTime;

    @AcColumn(name = "`pay_time`", comment = "支付时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime payTime;

    @AcColumn(name = "`use_time`", comment = "核销时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime useTime;

    @AcColumn(name = "`refund_time`", comment = "退款时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime refundTime;

    @AcColumn(name = "`update_time`", comment = "更新时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime updateTime;

}
