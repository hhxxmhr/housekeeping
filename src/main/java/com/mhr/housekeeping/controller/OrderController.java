package com.mhr.housekeeping.controller;

import com.github.pagehelper.PageInfo;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.service.OrdersService;
import com.mhr.housekeeping.utils.Result;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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

    /**
     * 取消订单---更改状态
     *
     * @param ordersVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/Order/edit")
    public Result edit(@RequestBody OrdersVO ordersVO) throws Exception {
        return ordersService.updateOrders(ordersVO);
    }

    @RequestMapping("/Order/delete")
    public Result delete(@RequestBody OrdersVO ordersVO) throws Exception {
        return ordersService.deleteOrders(ordersVO);
    }

    /**
     * 查询所有的订单
     *
     * @param hashMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/Order/getAll")
    public PageInfo<OrdersVO> getAll(@RequestBody HashMap hashMap) throws Exception {
        JSONObject object = JSONObject.fromObject(hashMap);
        OrdersVO ordersVO = (OrdersVO) JSONObject.toBean(object, OrdersVO.class);
        return ordersService.listOrders(ordersVO);
    }


}
