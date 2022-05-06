package com.xiaomou.controller;


import com.xiaomou.Result;
import com.xiaomou.dto.CommentBackDTO;
import com.xiaomou.dto.CommentDTO;
import com.xiaomou.dto.PageDTO;
import com.xiaomou.dto.ReplyDTO;
import com.xiaomou.service.CommentService;
import com.xiaomou.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@RestController
@Api(tags = "评论模块")
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;


    @ApiOperation(value = "查询评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Integer")})
    @GetMapping("/comments")
    private Result listComments(@RequestParam(value = "articleId") Integer articleId, @RequestParam(value = "current") Integer current) {

        PageDTO<CommentDTO> data = commentService.listComments(articleId, current);
        return Result.ok().data("data", data);

    }

    @ApiOperation(value = "添加评论或回复")
    @PostMapping("/comments")
    private Result saveComment(@RequestBody CommentVO commentVO) {

        commentService.saveComment(commentVO);

        return Result.ok();
    }

    @ApiOperation(value = "查看回复评论")
    @GetMapping("/comments/replies")
    private Result listRepliesByCommentId(Integer commentId, Long current) {


        List<ReplyDTO> data = commentService.listRepliesByCommentId(commentId, current);

        return Result.ok().data("data", data);
    }


    @ApiOperation(value = "分页获取用户评论列表")
    @GetMapping("/getUserCommentList")
    public Result getUserInfoList(Integer current, Integer size, String nickname) {

        List<CommentBackDTO> list = commentService.getUserReplyList(current, size, nickname);
        int total = commentService.count();
        return Result.ok().data("total", total).data("data", list);

    }

    @ApiOperation(value = "评论点赞")
    @PostMapping("/comments/like")
    public Result saveCommentList(Integer commentId) {
        commentService.saveCommentLike(commentId);
        return Result.ok().message("点赞成功！");
    }

}

