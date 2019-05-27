package com.mhr.housekeeping.controller;

import com.mhr.housekeeping.entity.vo.CommentVO;
import com.mhr.housekeeping.service.CommentService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;


@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @RequestMapping("/Comment/add")
    public Result add(@RequestBody CommentVO commentVO) throws Exception {
        return commentService.addComment(commentVO);
    }

    /**
     * 传过来的是一个id数组
     *
     * @param hashMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/Comment/delete")
    public Result delete(@RequestBody HashMap hashMap) {
        List<Integer> ids = (List<Integer>) hashMap.get("ids");
        return commentService.deleteCommentByIds(ids);
    }

    /**
     * 删除单个的评论记录
     * @param commentVO
     * @return
     */
    @RequestMapping("/Comment/deleteComment")
    public Result deleteComment(@RequestBody CommentVO commentVO ) throws Exception {
        return commentService.deleteComment(commentVO);
    }

    /**
     * 根据登陆者的id,查询其做出的所有评论的详细信息
     *
     * @param commentVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/Comment/getAllDetail")
    public Result getAllDetail(@RequestBody CommentVO commentVO) throws Exception {
        return commentService.listComment(commentVO);
    }


}
