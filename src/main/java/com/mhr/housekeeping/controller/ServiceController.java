package com.mhr.housekeeping.controller;

import com.alibaba.fastjson.JSONObject;
import com.mhr.housekeeping.entity.ServiceDO;
import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.service.ServiceService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class ServiceController {
    @Autowired
    ServiceService serviceService;

    @RequestMapping("/Service/getAll")
    public JSONObject getAll(@RequestBody ServiceVO serviceVO) throws Exception {
        JSONObject object = new JSONObject();
        Result res = serviceService.listService(serviceVO);
        object.put("serviceList", res.getData());
        return object;
    }

    @RequestMapping("/Service/getServiceByUserId")
    public Result getServiceByUserId(@RequestBody HashMap hashMap) throws Exception {
        Integer employeeId = (Integer) hashMap.get("id");
        Result res = serviceService.findServiceByUserId(employeeId);
        return res;
    }

    @RequestMapping("/Service/addService")
    public Result addService(@RequestBody ServiceVO serviceVO) throws Exception {
        return serviceService.addService(serviceVO);
    }

    @RequestMapping("/Service/editService")
    public Result editService(@RequestBody ServiceVO serviceVO) throws Exception {
        return serviceService.updateService(serviceVO);
    }

    @RequestMapping("/Service/delParService")
    public Result delParService(@RequestBody ServiceVO serviceVO) throws Exception {
        return serviceService.deleteParService(serviceVO);
    }

    @RequestMapping("/Service/deleteChild")
    public Result deleteChild(@RequestBody ServiceVO serviceVO) throws Exception {
        return serviceService.deleteService(serviceVO);
    }

    @RequestMapping("/Service/findServiceByOrder")
    public ServiceDO findServiceByOrder(@RequestBody HashMap hashMap) throws Exception {
        Integer orderId = (Integer) hashMap.get("orderId");
        return serviceService.findServiceByOrder(orderId);
    }

    @RequestMapping("/Service/getServiceByUserOrder")
    public List<ServiceDO> getServiceByUserOrder(@RequestBody HashMap hashMap) throws Exception {
        Integer employerId = (Integer) hashMap.get("employerId");
        //根据雇主id查询订单--》服务信息
        return serviceService.getServiceByUserOrder(employerId);
    }


}
