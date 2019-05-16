package com.mhr.housekeeping.controller;

import com.alibaba.fastjson.JSONObject;
import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.service.OrdersService;
import com.mhr.housekeeping.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class StaticController {
    @Autowired
    ServiceService serviceService;
    @Autowired
    OrdersService ordersService;

    /**
     * serviceId服务id
     * startTime 开始时间
     * endTime 结束时间
     * 根据完成的订单进行服务的统计
     *
     * @param hashMap
     */
    @RequestMapping("/Static/serviceDetail")
    public List<ServiceVO> serviceDetail(@RequestBody HashMap hashMap) throws Exception {
        Integer serviceId = (Integer) hashMap.get("serviceId");
        Integer startTime = (Integer) hashMap.get("startTime");
        Integer endTime = (Integer) hashMap.get("endTime");
        //根据服务的id、订单的完结时间区间 查询订单表，进行统计
        return serviceService.serviceStatic(serviceId, startTime, endTime);
    }

    /**
     * 雇员和雇主的统计页面
     *
     * @param hashMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/Static/getServiceDetailByEid")
    public JSONObject getServiceDetailByEid(@RequestBody HashMap hashMap) throws Exception {
        Integer serviceId = (Integer) hashMap.get("serviceId");
        Integer eid = (Integer) hashMap.get("eid");
        Integer role = (Integer) hashMap.get("role");
        Integer startTime = (Integer) hashMap.get("startTime");
        Integer endTime = (Integer) hashMap.get("endTime");
        //根据服务的id、订单的完结时间区间 查询订单表，进行统计
        List<ServiceVO> vos = serviceService.serviceStaticByEid(serviceId, startTime, endTime, eid, role);
        JSONObject object = new JSONObject();
        object.put("data",vos);
        return object;
    }

    /**
     * startTime : 当天的零点
     * endTime: 当天的23：59
     *
     * @param hashMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/Static/serviceChart")
    public JSONObject serviceChart(@RequestBody HashMap hashMap) throws Exception {
        //如果接收到了serviceId，则查询这一项服务的销量、营业额
        Integer serviceId = (Integer) hashMap.get("serviceId");
        Long startTime = (Long) hashMap.get("startTime");
        Long endTime = (Long) hashMap.get("endTime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //时间数组
        List<String> timeList = new ArrayList<>();
        //每天所有服务总单量
        List<Integer> serviceList = new ArrayList<>();
        //每天所有服务总营业额
        List<Integer> moneyList = new ArrayList<>();
        Long timeItem = startTime;
        while (timeItem <= endTime) {
            System.out.println(sdf.format(new Date(timeItem)));
            timeList.add(sdf.format(new Date(timeItem)));
            //查询每天的销量
            System.out.println("一天的endTime是" + (timeItem / 1000 + 3600 * 24 - 1));
            Integer count = ordersService.countOrdersByTime(timeItem / 1000, (timeItem / 1000 + 3600 * 24 - 1), serviceId);
            serviceList.add(count);
            //查询每天的营业额
            Integer money = ordersService.countMoneyByTime(timeItem / 1000, (timeItem / 1000 + 3600 * 24 - 1), serviceId);
            if (money == null) {
                money = 0;
            }
            moneyList.add(money);
            timeItem += 60 * 60 * 24 * 1000;
        }
        JSONObject object = new JSONObject();
        object.put("timeList", timeList);
        object.put("serviceList", serviceList);
        object.put("moneyList", moneyList);
        return object;
    }

    @RequestMapping("/Static/serviceChartByEid")
    public JSONObject serviceChartByEid(@RequestBody HashMap hashMap) throws Exception {
        //如果接收到了serviceId，则查询这一项服务的销量、营业额
        Integer serviceId = (Integer) hashMap.get("serviceId");
        Integer role = (Integer) hashMap.get("role");
        Long startTime = (Long) hashMap.get("startTime");
        Integer eid = (Integer) hashMap.get("eid");
        Long endTime = (Long) hashMap.get("endTime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> timeList = new ArrayList<>();
        List<Integer> serviceList = new ArrayList<>();
        List<Integer> moneyList = new ArrayList<>();
        Long timeItem = startTime;
        while (timeItem <= endTime) {
            timeList.add(sdf.format(new Date(timeItem)));
            //查询每天的销量
            if (role == 200) {
                Integer count = ordersService.countOrdersByTime2(timeItem / 1000, (timeItem / 1000 + 3600 * 24 - 1), serviceId, eid);
                serviceList.add(count);
                //查询每天的营业额
                Integer money = ordersService.countMoneyByTime2(timeItem / 1000, (timeItem / 1000 + 3600 * 24 - 1), serviceId, eid);
                if (money == null) {
                    money = 0;
                }
                moneyList.add(money);
                timeItem += 60 * 60 * 24 * 1000;
            } else if (role == 300) {
                Integer count = ordersService.countOrdersByTime3(timeItem / 1000, (timeItem / 1000 + 3600 * 24 - 1), serviceId, eid);
                serviceList.add(count);
                //查询每天的营业额
                Integer money = ordersService.countMoneyByTime3(timeItem / 1000, (timeItem / 1000 + 3600 * 24 - 1), serviceId, eid);
                if (money == null) {
                    money = 0;
                }
                moneyList.add(money);
                timeItem += 60 * 60 * 24 * 1000;
            }
        }
        JSONObject object = new JSONObject();
        object.put("timeList", timeList);
        object.put("serviceList", serviceList);
        object.put("moneyList", moneyList);
        return object;
    }
}
