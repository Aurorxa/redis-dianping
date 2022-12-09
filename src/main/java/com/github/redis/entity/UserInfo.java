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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 10:57:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user_info")
@AcTable(name = "`tb_user_info`", comment = "用户详情表")
public class UserInfo implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    @AcColumn(name = "`user_id`", comment = "用户表的主键，需要程序员手动维护")
    private Long userId;

    @AcColumn(name = "`city`", comment = "城市名")
    private String city;

    @AcColumn(name = "`introduce`", comment = "个人介绍，不要超过128个字符")
    private String introduce;

    @AcColumn(name = "`fans`", comment = "粉丝数量")
    private Integer fans;

    @AcColumn(name = "`followee`", comment = "关注的人的数量")
    private Integer followee;

    @AcColumn(name = "`gender`", comment = "性别，0：男，1：女")
    private Boolean gender;

    @AcColumn(name = "`birthday`", comment = "生日")
    private LocalDate birthday;

    @AcColumn(name = "`credits`", comment = "积分")
    private Integer credits;

    @AcColumn(name = "`level`", comment = "会员级别，0~9级,0代表未开通会员")
    private Integer level;

    @AcColumn(name = "`create_time`", comment = "创建时间",type = ColumnTypeEnums.DATETIME)
    private LocalDateTime createTime;

    @AcColumn(name = "`update_time`", comment = "更新时间",type = ColumnTypeEnums.DATETIME)
    private LocalDateTime updateTime;

}
