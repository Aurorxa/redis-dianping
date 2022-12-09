package com.github.redis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.redis.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 13:37:13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
