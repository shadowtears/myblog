package com.xiaomou.dto;

import lombok.Data;

/**
 * @author MouHongDa
 * @date 2022/4/24 16:01
 */
@Data
public class ReplyCountDTO {
    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 回复数量
     */
    private Integer replyCount;

}