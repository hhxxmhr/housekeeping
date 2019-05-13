package com.mhr.housekeeping.dao;

import com.mhr.housekeeping.entity.OrdersDO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.entity.vo.UserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br/>
 * Created by min on 2019/05/04
 */
@Repository
public interface OrdersMapper {

    Integer addOrders(OrdersVO ordersVO);

    Integer updateOrders(OrdersVO ordersVO);

    OrdersDO findDetailOrders(OrdersVO ordersVO);

    OrdersVO findDetailOrdersVo(OrdersVO ordersVO);

    List<OrdersVO> listOrders(OrdersVO ordersVO);

    List<OrdersVO> listOrdersPage(OrdersVO ordersVO);

    Integer countOrders(OrdersVO ordersVO);

    Integer deleteOrders(OrdersVO ordersVO);

    List<OrdersVO> listEmployeeOrders(Integer userId, Integer state);

    List<OrdersVO> listEmployerOrders(Integer userId, Integer state);

    Integer countOrdersByEmployeeId(UserVO it);

    Integer countOrdersByEmployerId(UserVO it);

    Integer countGoodOrders(OrdersVO orderVO);

    Integer countGoodOrders2(OrdersVO orderVO);

    Integer countOrdersByTime(Long timeItem, Long time2, Integer serviceId);

    Integer countOrdersByTime2(Long timeItem, Long time2, Integer serviceId, Integer eid);

    Integer countMoneyByTime(long l, long time, Integer serviceId);

    Integer countMoneyByTime2(long l, long time, Integer serviceId, Integer eid);

    Integer countOrdersByTime3(Long timeItem, Long time2, Integer serviceId, Integer eid);

    Integer countMoneyByTime3(long l, long time, Integer serviceId, Integer eid);

    Integer countOrdersOver(OrdersVO ordersVO);

    Integer countOrdersWithComment(OrdersVO ordersVO);

    Integer countOrdersWithGoodComment(OrdersVO ordersVO);

    Integer countOrdersWithReason(OrdersVO ordersVO);

    Integer getServiceMost(Integer id);
}
