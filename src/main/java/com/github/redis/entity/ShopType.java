package com.github.redis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @since 2022-12-09 13:14:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_shop_type")
@AcTable(name = "`tb_shop_type`", comment = "商户类型表")
public class ShopType implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @AcColumn(name = "`id`", comment = "主键")
    private Long id;

    @AcColumn(name = "`name`", comment = "类型名称")
    private String name;

    @AcColumn(name = "`icon`", comment = "图标")
    private String icon;

    @AcColumn(name = "`sort`", comment = "顺序")
    private Integer sort;

    @JsonIgnore
    @AcColumn(name = "`create_time`", comment = "创建时间",type = ColumnTypeEnums.DATETIME)
    private LocalDateTime createTime;

    @JsonIgnore
    @AcColumn(name = "`update_time`", comment = "更新时间",type = ColumnTypeEnums.DATETIME)
    private LocalDateTime updateTime;


}
