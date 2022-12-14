package com.github.redis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2022-12-09 13:29:07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_voucher")
@AcTable(name = "`tb_voucher`", comment = "优惠券表")
public class Voucher implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @AcColumn(name = "`id`", comment = "主键")
    private Long id;

    @AcColumn(name = "`shop_id`", comment = "商铺id")
    private Long shopId;

    @AcColumn(name = "`title`", comment = "代金券标题")
    private String title;

    @AcColumn(name = "`sub_title`", comment = "副标题")
    private String subTitle;

    @AcColumn(name = "`rules`", comment = "使用规则")
    private String rules;

    @AcColumn(name = "`pay_value`", comment = "支付金额")
    private Long payValue;

    @AcColumn(name = "`actual_value`", comment = "抵扣金额")
    private Long actualValue;

    @AcColumn(name = "`type`", comment = "优惠券类型 0 普通卷 1 秒杀卷，默认为 0", defaultValue = "0")
    private Integer type;


    @AcColumn(name = "`status`", comment = "优惠券状态 1 上架 2 下架 3 过期", defaultValue = "1")
    private Integer status;

    /**
     * 库存
     */
    @TableField(exist = false)
    private Integer stock;

    /**
     * 生效时间
     */
    @TableField(exist = false)
    private LocalDateTime beginTime;

    /**
     * 失效时间
     */
    @TableField(exist = false)
    private LocalDateTime endTime;

    @AcColumn(name = "`create_time`", comment = "创建时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime createTime;

    @AcColumn(name = "`update_time`", comment = "更新时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime updateTime;


}
