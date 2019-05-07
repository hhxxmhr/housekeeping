package com.mhr.housekeeping.service;


import com.mhr.housekeeping.entity.vo.CommentVO;
import com.mhr.housekeeping.utils.Result;

/**
 * <br/>
 * Created by min on 2019/05/07
 */
public interface CommentService {

	Result addComment(CommentVO commentVO) throws Exception;

	Result updateComment(CommentVO commentVO) throws Exception;
	
	Result findDetailComment(CommentVO commentVO) throws Exception;
	
	Result listComment(CommentVO commentVO) throws Exception;

    Result listCommentPage(CommentVO commentVO) throws Exception;
	
	Result countComment(CommentVO commentVO) throws Exception;
	
	Result deleteComment(CommentVO commentVO) throws Exception;
}