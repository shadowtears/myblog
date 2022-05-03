package com.xiaomou.constant;

/**
 * * redis常量
 *
 * @author MouHongDa
 * @date 2022/4/26 14:30
 */
public final class RedisPrefixConst {
    /**
     * 验证码过期时间
     */
    public static final long CODE_EXPIRE_TIME = 15 * 60 * 1000;
    /**
     * 验证码
     */
    public static final String CODE_KEY = "code_";
    /**
     * 公告
     */
    public static final String NOTICE = "notice";
    /**
     * 上次登录时间
     */
    public static final String LAST_LONG_TIME = "lastLongTime";
}
