package com.github.redis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-12-09 13:23:50
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_blog_comments")
@AcTable(name = "`tb_blog_comments`", comment = "用户日记表（达人探店日记）")
public class BlogComments implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @AcColumn(name = "`id`", comment = "主键")
    private Long id;

    @AcColumn(name = "`user_id`", comment = "用户id")
    private Long userId;

    @AcColumn(name = "`blog_id`", comment = "探店id")
    private Long blogId;

    @AcColumn(name = "`parent_id`", comment = "关联的1级评论id，如果是一级评论，则值为0")
    private Long parentId;

    @AcColumn(name = "`answer_id`", comment = "回复的评论id")
    private Long answerId;


    @AcColumn(name = "`content`", comment = "回复的内容", length = 255)
    private String content;

    @AcColumn(name = "`liked`", comment = "点赞数")
    private Integer liked;

    /**
     * 状态，0：正常，1：被举报，2：禁止查看
     */
    @AcColumn(name = "`status`", comment = "状态，0：正常，1：被举报，2：禁止查看")
    private Integer status;

    @AcColumn(name = "`create_time`", comment = "创建时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime createTime;

    @AcColumn(name = "`update_time`", comment = "更新时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime updateTime;
}
