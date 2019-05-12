package com.mhr.housekeeping.controller;

import com.github.pagehelper.PageInfo;
import com.mhr.housekeeping.entity.vo.FundVO;
import com.mhr.housekeeping.service.FundService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class FundController {
    @Autowired
    FundService fundService;

    @RequestMapping("/Fund/getAll")
    public PageInfo<FundVO> getAll(@RequestBody HashMap hashMap) throws Exception {
        JSONObject object = JSONObject.fromObject(hashMap);
        FundVO vo = (FundVO) JSONObject.toBean(object, FundVO.class);
        return fundService.listFund(vo);
    }

}
