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
 * @since 2022-12-09 13:16:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_blog")
@AcTable(name = "`tb_blog`", comment = "用户日记表（达人探店日记）")
public class Blog implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @AcColumn(name = "`id`", comment = "主键")
    private Long id;

    @AcColumn(name = "`shop_id`", comment = "商户id")
    private Long shopId;

    @AcColumn(name = "`user_id`", comment = "用户id")
    private Long userId;

    /**
     * 用户图标
     */
    @TableField(exist = false)
    private String icon;

    /**
     * 用户姓名
     */
    @TableField(exist = false)
    private String name;

    /**
     * 是否点赞过了
     */
    @TableField(exist = false)
    private Boolean isLike;

    @AcColumn(name = "`title`", comment = "标题")
    private String title;

    @AcColumn(name = "`images`", comment = "探店的照片，最多9张，多张以,隔开", length = 2048)
    private String images;

    @AcColumn(name = "`content`", comment = "探店的文字描述", length = 2048)
    private String content;

    @AcColumn(name = "`liked`", comment = "点赞数量")
    private Integer liked;

    @AcColumn(name = "`comments`", comment = "评论数量")
    private Integer comments;

    @AcColumn(name = "`create_time`", comment = "创建时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime createTime;

    @AcColumn(name = "`update_time`", comment = "更新时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime updateTime;

}
