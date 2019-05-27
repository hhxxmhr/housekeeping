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
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    OrdersService ordersService;

    @RequestMapping("/Order/add")
    public Result add(@RequestBody OrdersVO ordersVO) throws Exception {
        ordersVO.setState(0);
        ordersVO.setCreateTime(System.currentTimeMillis() / 1000);
        ordersVO.setPay(0);
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

    /**
     * 查询检验的这个时间  是不是在员工上门时间到预计任务完成时间的  区间内，
     * 如果在的话，就把此人给过滤掉，不在的话就显示符合条件的雇员
     *
     * @param hashMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/Orders/findOrdersByReverseTime")
    public Boolean findOrdersByReverseTime(@RequestBody HashMap hashMap) throws Exception {
        Integer startTime = (Integer) hashMap.get("startTime");
        Long endTime = (Long) hashMap.get("endTime");
        Integer employeeId = (Integer) hashMap.get("employeeId");
        //遍历这个员工的所有的已经确认或者待完成的订单，查看是否有区间满足
        List<OrdersVO> voList = ordersService.findOrdersByEmployeeIdWithState(employeeId);
        if (voList != null && voList.size() > 0) {
            for (OrdersVO order : voList) {
                if (order.getServiceType().equals("月")) {
                    if (startTime >= order.getReverseTime() && startTime <= order.getReverseTime() + 31 * 24 * 3600) {
                        return false;
                    }
                } else if (order.getServiceType().equals("次")) {
                    if (startTime >= order.getReverseTime() && startTime <= order.getReverseTime() + 6 * 3600) {
                        return false;
                    }
                }
            }
        }
        return true;
        /*List<OrdersVO> ordersByReverseTime = ordersService.findOrdersByReverseTime(employeeId, startTime / 1000, endTime / 1000);
        System.out.println(ordersByReverseTime);
        return ordersByReverseTime;*/
    }


}
