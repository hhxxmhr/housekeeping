package com.mhr.housekeeping.service.impl;

import com.mhr.housekeeping.dao.*;
import com.mhr.housekeeping.entity.*;
import com.mhr.housekeeping.entity.vo.CommentVO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.CommentService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <br/>
 * Created by min on 2019/05/07
 */
@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {

    private final static Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ServiceMapper serviceMapper;
    @Resource
    private RankMapper rankMapper;

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

    /**
     * 所有的comment以及其订单的详细信息
     *
     * @param commentVO
     * @return
     * @throws Exception
     */
    @Override
    public Result listComment(CommentVO commentVO) throws Exception {
        List<CommentVO> comments = new ArrayList<>();
        if (commentVO.getRole() == 200) {
            comments = commentMapper.listCommentWithCommentEmployee(commentVO);
        } else if (commentVO.getRole() == 300) {
            comments = commentMapper.listCommentWithCommentEmployer(commentVO);
        }
        if (comments != null && comments.size() > 0) {
            comments.forEach(comment -> {
                //根据评论的orderId查询订单的信息
                OrdersVO ordersVO = new OrdersVO();
                ordersVO.setId(comment.getOrderId());
                OrdersDO detailOrders = ordersMapper.findDetailOrders(ordersVO);
                comment.setOrder(detailOrders);
                //根据订单查到雇员和雇主的信息
                UserVO vo = new UserVO();
                vo.setId(detailOrders.getEmployeeId());
                UserDO employee = userMapper.findDetailUser(vo);
                UserVO vo2 = new UserVO();
                vo2.setId(detailOrders.getEmployerId());
                UserDO employer = userMapper.findDetailUser(vo2);
                comment.setEmployeeName(employee.getName());
                comment.setEmployerName(employer.getName());
                //根据订单查询服务
                ServiceVO serviceVO = new ServiceVO();
                serviceVO.setId(detailOrders.getServiceId());
                ServiceDO detailService = serviceMapper.findDetailService(serviceVO);
                comment.setServiceName(detailService.getName());
                //根据订单查询服务等级
                RankDO rankByOrder = rankMapper.findRankByOrder(employee.getId(), detailService.getId());
                comment.setRankName(rankByOrder.getName());
            });
        }
        return new Result<>(comments);
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