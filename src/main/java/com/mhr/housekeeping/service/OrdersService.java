package com.mhr.housekeeping.service;


import com.github.pagehelper.PageInfo;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.utils.Result;

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
	
	Result countOrders(OrdersVO ordersVO) throws Exception;
	
	Result deleteOrders(OrdersVO ordersVO) throws Exception;

}