package com.mhr.housekeeping.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhr.housekeeping.dao.*;
import com.mhr.housekeeping.entity.CommentDO;
import com.mhr.housekeeping.entity.RankDO;
import com.mhr.housekeeping.entity.ServiceDO;
import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.OrdersService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <br/>
 * Created by min on 2019/05/04
 */
@Service("ordersService")
@Transactional
public class OrdersServiceImpl implements OrdersService {

    private final static Logger LOG = LoggerFactory.getLogger(OrdersServiceImpl.class);

    @Resource
    private OrdersMapper ordersMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ServiceMapper serviceMapper;
    @Autowired
    RankMapper rankMapper;
    @Autowired
    CommentMapper commentMapper;

    @Override
    public Result addOrders(OrdersVO ordersVO) throws Exception {
        Integer count = ordersMapper.addOrders(ordersVO);
        if (count > 0) {
            return Result.getSuccess("添加成功");
        }
        return Result.getFailure("添加失败");
    }

    @Override
    public Result updateOrders(OrdersVO ordersVO) throws Exception {
        Integer count = ordersMapper.updateOrders(ordersVO);
        if (count > 0) {
            return Result.getSuccess("操作成功");
        }
        return Result.getFailure("操作失败");
    }

    @Override
    public Result findDetailOrders(OrdersVO ordersVO) throws Exception {
        return null;
    }

    @Override
    public PageInfo<OrdersVO> listOrders(OrdersVO ordersVO) throws Exception {
        PageHelper.startPage(ordersVO.getPage(), ordersVO.getSize());
        //如果登陆的是管理员，则显示所有的订单
        if (ordersVO.getRole() == 100) {
            List<OrdersVO> orders = ordersMapper.listOrders(ordersVO);
            if (orders != null && orders.size() > 0) {
                orders.forEach(order -> {
                    UserDO detailUser = userMapper.findDetailUser(new UserVO(order.getEmployerId()));
                    order.setEmployerName(detailUser.getName());
                    order.setEmployerUsername(detailUser.getUsername());
                    UserDO detailUser1 = userMapper.findDetailUser(new UserVO(order.getEmployeeId()));
                    order.setEmployeeName(detailUser1.getName());
                    order.setEmployeeUsername(detailUser1.getUsername());
                    ServiceVO serviceVO = new ServiceVO();
                    serviceVO.setId(order.getServiceId());
                    ServiceDO detailService = serviceMapper.findDetailService(serviceVO);
                    order.setServiceName(detailService.getName());
                    //获取等级名字等信息
                    RankDO rankDO = rankMapper.findRankByOrder(order.getEmployeeId(), order.getServiceId());
                    order.setRankName(rankDO.getName());
                    order.setRankMoney(rankDO.getMoney());
                    //根据订单id获取评论信息
                    CommentDO commentDO = commentMapper.findCommentByOrder(order.getId());
                    if (commentDO != null) {
                        order.setRate(commentDO.getRate());
                    }

                });
            }
            if (ordersVO.getEid() == null) {
                return new PageInfo<>(orders);
            } else {
                //将所有的订单进行过滤，返回某个雇主或者雇员的订单信息
                List<OrdersVO> list = new ArrayList<>();
                if (orders != null && orders.size() > 0) {
                    orders.forEach(order -> {
                        if (order.getEmployeeId().equals(ordersVO.getEid()) || order.getEmployerId().equals(ordersVO.getEid())) {
                            list.add(order);
                        }
                    });
                }
                return new PageInfo<>(list);
            }

        } else {
            //根据userId显示对应的订单
            if (ordersVO.getRole() == 200) {
                //如果当前登陆的是雇员，查询订单列表，显示他个人的所有列表
                List<OrdersVO> voList = ordersMapper.listEmployeeOrders(ordersVO.getUserId(), ordersVO.getState());
                //根据雇员的id查询雇员的名字
                if (voList.size() > 0) {
                    voList.forEach(vo -> {
                        UserDO detailUser = userMapper.findDetailUser(new UserVO(vo.getEmployerId()));
                        vo.setEmployerName(detailUser.getName());
                        //根据订单id获取评论信息
                        CommentDO commentDO = commentMapper.findCommentByOrder(vo.getId());
                        if (commentDO != null) {
                            vo.setRate(commentDO.getRate());
                        }
                    });
                }
                return new PageInfo<>(voList);
            } else if (ordersVO.getRole() == 300) {
                //当前登陆的是雇主，显示雇主显示的所有的订单
                List<OrdersVO> voList = ordersMapper.listEmployerOrders(ordersVO.getUserId(), ordersVO.getState());
                if (voList.size() > 0) {
                    voList.forEach(vo -> {
                        //获取雇员信息
                        UserDO detailUser = userMapper.findDetailUser(new UserVO(vo.getEmployeeId()));
                        vo.setEmployeeName(detailUser.getName());
                        //获取等级信息
                        RankDO rankDO = rankMapper.findRankByOrder(vo.getEmployeeId(), vo.getServiceId());
                        vo.setRankName(rankDO.getName());
                        //根据订单id获取评论信息
                        CommentDO commentDO = commentMapper.findCommentByOrder(vo.getId());
                        if (commentDO != null) {
                            vo.setRate(commentDO.getRate());
                        }
                    });
                }
                return new PageInfo<>(voList);
            }
        }
        return null;
    }

    @Override
    public Result listOrdersPage(OrdersVO ordersVO) throws Exception {
        return null;
    }

    @Override
    public Result countOrders(OrdersVO ordersVO) throws Exception {
        return null;
    }

    @Override
    public Result deleteOrders(OrdersVO ordersVO) throws Exception {
        Integer count = ordersMapper.deleteOrders(ordersVO);
        if (count > 0) {
            return Result.getSuccess("删除成功");
        }
        return Result.getFailure("删除失败");
    }


}