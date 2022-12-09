package com.github.redis.utils;

import com.github.redis.vo.UserVo;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-09 15:47:13
 */
public class UserHolder {
    private static final ThreadLocal<UserVo> USER_HOLDER = new ThreadLocal<>();

    public static void saveUser(UserVo user){
        USER_HOLDER.set(user);
    }

    public static UserVo getUser(){
        return USER_HOLDER.get();
    }

    public static void removeUser(){
        USER_HOLDER.remove();
    }

}
