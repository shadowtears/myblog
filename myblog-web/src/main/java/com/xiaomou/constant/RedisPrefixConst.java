package com.xiaomou.constant;

/**
 * * redis常量
 *
 * @author MouHongDa
 * @date 2022/4/26 14:30
 */
public class RedisPrefixConst {
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
     * ip集合
     */
    public static final String IP_SET = "ip_set";
}
