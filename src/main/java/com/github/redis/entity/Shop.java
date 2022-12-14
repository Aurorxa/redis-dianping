package com.github.redis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2022-12-09 10:57:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_shop")
@AcTable(name = "`tb_shop`", comment = "商户信息表")
public class Shop implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @AcColumn(name = "`id`", comment = "主键")
    private Long id;

    @AcColumn(name = "`name`", comment = "商铺名称", length = 128)
    private String name;

    @AcColumn(name = "`type_id`", comment = "商铺类型的 id")
    private Long typeId;

    @AcColumn(name = "`images`", comment = "商铺图片，多个图片以,隔开", length = 1024)
    private String images;

    @AcColumn(name = "`area`", comment = "商圈，例如：陆家嘴")
    private String area;

    @AcColumn(name = "`address`", comment = "地址")
    private String address;

    @AcColumn(name = "`x`", comment = "经度", type = ColumnTypeEnums.DECIMAL)
    private Double x;

    @AcColumn(name = "`y`", comment = "维度", type = ColumnTypeEnums.DECIMAL)
    private Double y;

    @AcColumn(name = "`avg_price`", comment = "均价，取整数")
    private Long avgPrice;

    @AcColumn(name = "`sold`", comment = "销量")
    private Integer sold;

    @AcColumn(name = "`comments`", comment = "评论数量")
    private Integer comments;

    @AcColumn(name = "`score`", comment = "评分，1~5分，乘10保存，避免小数")
    private Integer score;

    @AcColumn(name = "`open_hours`", comment = "营业时间，例如：10:00-22:00")
    private String openHours;

    @TableField(exist = false)
    private Double distance;

    @AcColumn(name = "`create_time`", comment = "创建时间", type = ColumnTypeEnums.DATETIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @AcColumn(name = "`update_time`", comment = "更新时间", type = ColumnTypeEnums.DATETIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
