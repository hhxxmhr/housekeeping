package com.mhr.housekeeping.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhr.housekeeping.dao.*;
import com.mhr.housekeeping.entity.*;
import com.mhr.housekeeping.entity.vo.FundVO;
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
import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    FundMapper fundMapper;
    @Autowired
    HttpServletRequest request;

    @Override
    public Result addOrders(OrdersVO ordersVO) throws Exception {
        Integer count = ordersMapper.addOrders(ordersVO);
        if (count > 0) {
            //更新用户余额、
            UserDO user = (UserDO) request.getSession().getAttribute("user");
            user.setBalance(user.getBalance() - ordersVO.getOrderPrice());
            Integer r1 = userMapper.updateUser2(user);
            //更新资金记录---雇主支出服务费
            FundVO vo = new FundVO();
            vo.setBalance(user.getBalance());//雇主的余额
            vo.setChangeMoney(ordersVO.getOrderPrice());
            vo.setCreateTime(System.currentTimeMillis() / 1000);
            vo.setType(1);
            vo.setUserId(ordersVO.getEmployerId());
            vo.setOrderId(ordersVO.getId());//自增id
            Integer r2 = fundMapper.addFund(vo);
            if (r1 > 0 && r2 > 0) return Result.getSuccess("预定成功");
            else return Result.getFailure("预定失败");
        }
        return Result.getFailure("预定失败");
    }

    /**
     * 更新操作  改变状态  雇员确认订单  雇员完成订单 雇主取消订单和超时取消订单
     * 1、雇主超时取消订单的时候余额更新、资金记录更新
     * 2、雇员完成订单的时候，工资更新、资金记录更新
     *
     * @param ordersVO
     * @return
     * @throws Exception
     */
    @Override
    public Result updateOrders(OrdersVO ordersVO) throws Exception {
        Integer count = ordersMapper.updateOrders(ordersVO);
        //根据id获取到此订单的详细信息
        OrdersDO detailOrders = ordersMapper.findDetailOrders(new OrdersVO(ordersVO.getId()));
        if (count > 0) {
            //此操作是---超时取消的费用
            if (ordersVO.getPay() != null) {
                //更新当前登陆用户余额---减去超时费用
                UserDO user = (UserDO) request.getSession().getAttribute("user");
                user.setBalance(user.getBalance() - ordersVO.getPay());
                Integer r1 = userMapper.updateUser2(user);
                //更新资金记录--减去超时费用
                Integer r2 = fundMapper.addFund(new FundVO(user.getId(), ordersVO.getId(), user.getBalance(), ordersVO.getPay(), System.currentTimeMillis() / 1000, 2));
                //更新雇员工资
                UserDO employee = userMapper.findDetailUser(new UserVO(detailOrders.getEmployeeId()));
                employee.setBalance(employee.getBalance() + ordersVO.getPay());
                Integer r3 = userMapper.updateUser2(employee);
                //更新资金记录---雇员加上超时费用
                Integer r4 = fundMapper.addFund(new FundVO(employee.getId(), ordersVO.getId(), employee.getBalance(), ordersVO.getPay(), System.currentTimeMillis() / 1000, 4));
                if (r1 > 0 && r2 > 0 && r3 > 0 && r4 > 0) {
                    return Result.getSuccess("操作成功");
                } else return Result.getFailure("更新金额失败");
            }

            //此操作是雇员完成订单，更新工资、更新资金记录
            if (ordersVO.getEndTime() != null) {
                //更新当前登陆用户工资---加上订单的费用
                UserDO user = (UserDO) request.getSession().getAttribute("user");
                user.setBalance(user.getBalance() + detailOrders.getOrderPrice());
                Integer res1 = userMapper.updateUser2(user);
                //更新记录
                Integer res2 = fundMapper.addFund(new FundVO(user.getId(), ordersVO.getId(), user.getBalance(), detailOrders.getOrderPrice(), System.currentTimeMillis() / 1000, 5));
                if (res1 > 0 && res2 > 0) {
                    return Result.getSuccess("操作成功");
                } else return Result.getFailure("更新金额失败");
            }
            //此操作是管理员同意 退款申请 ，更新员工的工资、雇主的余额 以及 更新资金记录
            if (ordersVO.getState() == 6) {
                //根据订单更新雇员
                UserDO employee = userMapper.findDetailUser(new UserVO(detailOrders.getEmployeeId()));
                employee.setBalance(employee.getBalance() - detailOrders.getOrderPrice());
                Integer count1 = userMapper.updateUser2(employee);
                //更新资金
                Integer count2 = fundMapper.addFund(new FundVO(employee.getId(), ordersVO.getId(), employee.getBalance(), detailOrders.getOrderPrice(), System.currentTimeMillis() / 1000, 6));
                //根据订单更新雇主
                UserDO employer = userMapper.findDetailUser(new UserVO(detailOrders.getEmployerId()));
                employer.setBalance(employer.getBalance() + detailOrders.getOrderPrice());
                Integer count3 = userMapper.updateUser2(employer);
                //更新资金
                Integer count4 = fundMapper.addFund(new FundVO(employer.getId(), ordersVO.getId(), employer.getBalance(), detailOrders.getOrderPrice(), System.currentTimeMillis() / 1000, 3));
                if (count3 > 0 && count4 > 0 && count1 > 0 && count2 > 0) {
                    return Result.getSuccess("操作成功");
                } else return Result.getFailure("更新金额失败");

            }
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
                        vo.setEmployerPhone(detailUser.getPhone());
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
                        vo.setEmployeePhone(detailUser.getPhone());
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
    public Integer countOrders(OrdersVO ordersVO) throws Exception {
        return ordersMapper.countOrders(ordersVO);
    }

    @Override
    public Result deleteOrders(OrdersVO ordersVO) throws Exception {
        //删除订单之前，先删除与订单有关的记录
        Integer co = fundMapper.deleteFundByOrder(ordersVO);
        if (co > 0) {
            Integer count = ordersMapper.deleteOrders(ordersVO);
            if (count > 0) {
                return Result.getSuccess("删除成功");
            }
            return Result.getFailure("删除失败");
        } else return Result.getFailure("根据订单先删除与之相关的资金记录失败");
    }

    @Override
    public Integer countOrdersByTime(Long timeItem, Long time2, Integer serviceId) {
        return ordersMapper.countOrdersByTime(timeItem, time2, serviceId);
    }

    @Override
    public Integer countOrdersByTime2(Long timeItem, Long time2, Integer serviceId, Integer eid) {
        return ordersMapper.countOrdersByTime2(timeItem, time2, serviceId, eid);
    }

    @Override
    public Integer countOrdersByTime3(Long timeItem, Long time2, Integer serviceId, Integer eid) {
        return ordersMapper.countOrdersByTime3(timeItem, time2, serviceId, eid);
    }

    /**
     * 累计每天的销售额
     *
     * @param l
     * @return
     */
    @Override
    public Integer countMoneyByTime(long l, long time, Integer serviceId) {
        return ordersMapper.countMoneyByTime(l, time, serviceId);
    }

    @Override
    public Integer countMoneyByTime2(long l, long time, Integer serviceId, Integer eid) {
        return ordersMapper.countMoneyByTime2(l, time, serviceId, eid);
    }

    @Override
    public Integer countMoneyByTime3(long l, long time, Integer serviceId, Integer eid) {
        return ordersMapper.countMoneyByTime3(l, time, serviceId, eid);
    }

    @Override
    public Integer countOrdersOver(OrdersVO ordersVO) {

        return ordersMapper.countOrdersOver(ordersVO);
    }

    @Override
    public Integer countOrdersWithComment(OrdersVO ordersVO) {

        return ordersMapper.countOrdersWithComment(ordersVO);
    }

    @Override
    public Integer countOrdersWithGoodComment(OrdersVO ordersVO) {
        return ordersMapper.countOrdersWithGoodComment(ordersVO);
    }

    @Override
    public Integer countOrdersByEmployeeId(UserVO userVO) {
        return ordersMapper.countOrdersByEmployeeId(userVO);
    }

    @Override
    public Integer countOrdersWithReason(OrdersVO ordersVO) {
        return ordersMapper.countOrdersWithReason(ordersVO);
    }

    @Override
    public Integer getServiceMost(Integer id) {
        return ordersMapper.getServiceMost(id);
    }


}