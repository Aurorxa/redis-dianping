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
 * @since 2022-12-09 13:27:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_follow")
@AcTable(name = "`tb_follow`", comment = "用户关注表")
public class Follow implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @AcColumn(name = "`id`", comment = "主键")
    private Long id;

    @AcColumn(name = "`user_id`", comment = "用户id")
    private Long userId;

    @AcColumn(name = "`follow_user_id`", comment = "关联的用户id")
    private Long followUserId;

    @AcColumn(name = "`create_time`", comment = "创建时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime createTime;

    @AcColumn(name = "`update_time`", comment = "更新时间", type = ColumnTypeEnums.DATETIME)
    private LocalDateTime updateTime;


}
