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

    /**
     * 文章浏览量
     */
    public static final String ARTICLE_VIEWS_COUNT = "article_views_count";

    /**
     * 文章点赞量
     */
    public static final String ARTICLE_LIKE_COUNT = "article_like_count";

    /**
     * 用户点赞文章
     */
    public static final String ARTICLE_USER_LIKE = "article_user_like";
    /**
     * 用户点赞评论
     */
    public static final String COMMENT_USER_LIKE = "comment_user_like";
    /**
     * 评论点赞量
     */
    public static final String COMMENT_LIKE_COUNT = "comment_like_count";

    /**
     * ip集合
     */
    public static final String IP_SET = "ip_set";
}
