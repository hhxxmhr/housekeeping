package com.mhr.housekeeping.controller;

import com.mhr.housekeeping.entity.vo.CommentVO;
import com.mhr.housekeeping.service.CommentService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @RequestMapping("/Comment/add")
    public Result add(@RequestBody CommentVO commentVO) throws Exception {
        return commentService.addComment(commentVO);
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
