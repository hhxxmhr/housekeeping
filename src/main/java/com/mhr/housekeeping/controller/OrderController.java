package com.mhr.housekeeping.controller;

import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.service.OrdersService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    OrdersService ordersService;

    @RequestMapping("/Order/add")
    public Result add(@RequestBody OrdersVO ordersVO) throws Exception {
        ordersVO.setState(0);
        ordersVO.setCreateTime(System.currentTimeMillis() / 1000);
        return ordersService.addOrders(ordersVO);
    }
}
