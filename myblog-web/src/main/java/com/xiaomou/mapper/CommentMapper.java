package com.xiaomou.mapper;

import com.xiaomou.dto.CommentBackDTO;
import com.xiaomou.dto.CommentDTO;
import com.xiaomou.dto.ReplyCountDTO;
import com.xiaomou.dto.ReplyDTO;
import com.xiaomou.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface CommentMapper extends BaseMapper<Comment> {
    List<CommentDTO> listComments(@Param("articleId") Integer articleId,@Param("current")Integer current);


    /**
     * 查看评论id集合下的回复
     *
     * @param commentIdList 评论id集合
     * @return 回复集合
     */
    List<ReplyDTO> listReplies(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 根据评论id查询回复总量
     *
     * @param commentIdList 评论id集合
     * @return 回复数量
     */
    List<ReplyCountDTO> listReplyCountByCommentId(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 查看当条评论下的回复
     *
     * @param commentId 评论id
     * @param current   当前页码
     * @return 回复集合
     */
    List<ReplyDTO> listRepliesByCommentId(@Param("commentId") Integer commentId, @Param("current") Long current);

    List<CommentBackDTO> getUserReplyList (@Param("current") Integer current, @Param("size") Integer size , @Param("nickname") String nickname);
}
