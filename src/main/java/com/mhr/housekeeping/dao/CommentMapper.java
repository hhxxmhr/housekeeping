package com.mhr.housekeeping.dao;

import com.mhr.housekeeping.entity.CommentDO;
import com.mhr.housekeeping.entity.vo.CommentVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 
 * <br/>
 * Created by min on 2019/05/07
 */
@Repository
public interface CommentMapper {

    Integer addComment(CommentVO commentVO);

    Integer updateComment(CommentVO commentVO);

    CommentDO findDetailComment(CommentVO commentVO);

    List<CommentVO> listComment(CommentVO commentVO);

    List<CommentDO> listCommentPage(CommentVO commentVO);

    Integer countComment(CommentVO commentVO);

    Integer deleteComment(CommentVO commentVO);

    CommentDO findCommentByOrder(Integer id);

    List<CommentVO> listCommentWithCommentEmployee(CommentVO commentVO);

    List<CommentVO> listCommentWithCommentEmployer(CommentVO commentVO);

    List<CommentVO> listCommentWithComment(CommentVO commentVO);
}
