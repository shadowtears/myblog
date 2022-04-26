package com.xiaomou.dto;

import lombok.Data;

/**
 * @author MouHongDa
 * @date 2022/4/24 16:05
 */
@Data
public class CommentBackDTO {

    private  Integer userId;
    private  String avatar;
    private  String nickname;
    private  String replyId;
    private  String replyNickname;
    private  Integer article_id;
    private  String articleTitle;
    private  String commentContent;
    private  String createTime;

}
