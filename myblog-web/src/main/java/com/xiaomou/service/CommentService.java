package com.xiaomou.service;

import com.xiaomou.dto.CommentBackDTO;
import com.xiaomou.dto.CommentDTO;
import com.xiaomou.dto.PageDTO;
import com.xiaomou.dto.ReplyDTO;
import com.xiaomou.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomou.vo.CommentVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface CommentService extends IService<Comment> {

    PageDTO<CommentDTO> listComments(Integer articleId, Integer current);

    void saveComment(CommentVO commentVO);

    /**
     * 查看评论下的回复
     *
     * @param commentId 评论id
     * @param current   当前页码
     * @return 回复列表
     */
    List<ReplyDTO> listRepliesByCommentId(Integer commentId, Long current);


    List<CommentBackDTO>  getUserReplyList (Integer current, Integer size , String nickname);
}
