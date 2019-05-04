package com.mhr.housekeeping.dao;
import com.mhr.housekeeping.entity.OrdersDO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
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

    List<OrdersDO> listOrders(OrdersVO ordersVO);

    List<OrdersDO> listOrdersPage(OrdersVO ordersVO);

    Integer countOrders(OrdersVO ordersVO);

    Integer deleteOrders(OrdersVO ordersVO);

}
