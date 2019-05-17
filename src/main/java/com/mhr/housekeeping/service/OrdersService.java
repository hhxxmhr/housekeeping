package com.mhr.housekeeping.service;


import com.github.pagehelper.PageInfo;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.utils.Result;

import java.util.List;

/**
 * <br/>
 * Created by min on 2019/05/04
 */
public interface OrdersService {

    Result addOrders(OrdersVO ordersVO) throws Exception;

    Result updateOrders(OrdersVO ordersVO) throws Exception;

    Result findDetailOrders(OrdersVO ordersVO) throws Exception;

    PageInfo<OrdersVO> listOrders(OrdersVO ordersVO) throws Exception;

    Result listOrdersPage(OrdersVO ordersVO) throws Exception;

    Integer countOrders(OrdersVO ordersVO) throws Exception;

    Result deleteOrders(OrdersVO ordersVO) throws Exception;

    Integer countOrdersByTime(Long timeItem, Long time2, Integer serviceId);

    Integer countOrdersByTime2(Long timeItem, Long time2, Integer serviceId, Integer eid);

    Integer countOrdersByTime3(Long timeItem, Long time2, Integer serviceId, Integer eid);

    Integer countMoneyByTime(long l, long time, Integer serviceId);

    Integer countMoneyByTime2(long l, long time, Integer serviceId, Integer eid);

    Integer countMoneyByTime3(long l, long time, Integer serviceId, Integer eid);

    Integer countOrdersOver(OrdersVO ordersVO);

    Integer countOrdersWithComment(OrdersVO ordersVO);

    Integer countOrdersWithGoodComment(OrdersVO ordersVO);

    Integer countOrdersByEmployeeId(UserVO userVO );

    Integer countOrdersWithReason(OrdersVO ordersVO);

    Integer getServiceMost(Integer id);

    Integer countOrdersRefund(long l, long l1, Integer id);

    List<OrdersVO> findOrdersByReverseTime(Integer employeeId, Long startTime, Long endTime);
}