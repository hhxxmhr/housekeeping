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
    public Result editInfo(@RequestBody CommentVO commentVO ) throws Exception {
        return commentService.addComment(commentVO);
    }
}
