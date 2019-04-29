package com.mhr.housekeeping.controller;

import com.alibaba.fastjson.JSONObject;
import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.service.ServiceService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
