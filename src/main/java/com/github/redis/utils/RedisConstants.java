package com.github.redis.utils;

/**
 * Redis 中的相关常量抽取
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-10 13:20:06
 */
public interface RedisConstants {
    /**
     * 手机号登录验证码的前缀
     */
    String LOGIN_CODE_PHONE_KEY_PREFIX = "login:code:phone:";

    /**
     * 手机号登录验证码的过期时间
     */
    Long LOGIN_CODE_TTL = 5L;

    /**
     * 登录 token 前缀
     */
    String LOGIN_TOKEN_KEY_PREFIX = "login:token:";

    /**
     * 登录 token 的过期时间
     */
    Long LOGIN_TOKEN_TTL = 30L;

    /**
     * 店铺的缓存前缀
     */
    String CACHE_SHOP_KEY_PREFIX = "cache:shop:";

    /**
     * 店铺类型查询列表缓存前缀
     */
    String CACHE_SHOP_TYPE_LIST_KEY_PREFIX = "cache:shop-type:list";

}
