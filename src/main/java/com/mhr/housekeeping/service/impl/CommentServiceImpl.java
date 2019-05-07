package com.mhr.housekeeping.service.impl;

import com.mhr.housekeeping.dao.CommentMapper;
import com.mhr.housekeeping.entity.vo.CommentVO;
import com.mhr.housekeeping.service.CommentService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <br/>
 * Created by min on 2019/05/07
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

    private final static Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Resource
    private CommentMapper commentMapper;

    @Override
    public Result addComment(CommentVO commentVO) throws Exception {
        Integer count = commentMapper.addComment(commentVO);
        if (count > 0) {
            return Result.getSuccess("评论成功");
        }
        return Result.getFailure("评论失败");
    }

    @Override
    public Result updateComment(CommentVO commentVO) throws Exception {
        return null;
    }

    @Override
    public Result findDetailComment(CommentVO commentVO) throws Exception {
        return null;
    }

    @Override
    public Result listComment(CommentVO commentVO) throws Exception {
        return null;
    }

    @Override
    public Result listCommentPage(CommentVO commentVO) throws Exception {
        return null;
    }

    @Override
    public Result countComment(CommentVO commentVO) throws Exception {
        return null;
    }

    @Override
    public Result deleteComment(CommentVO commentVO) throws Exception {
        return null;
    }

}