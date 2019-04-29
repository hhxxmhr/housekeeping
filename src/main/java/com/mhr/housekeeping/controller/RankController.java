package com.mhr.housekeeping.controller;

import com.alibaba.fastjson.JSONObject;
import com.mhr.housekeeping.entity.vo.RankVO;
import com.mhr.housekeeping.service.RankService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankController {
    @Autowired
    RankService rankService;

    @RequestMapping("/Rank/getAll")
    public JSONObject getAll(@RequestBody RankVO rankVO ) throws Exception {
        JSONObject object = new JSONObject();
        Result res = rankService.listRank(rankVO);
        object.put("rankList",res.getData());
        return object ;
    }

    @RequestMapping("/Rank/delRank")
    public Result delRank(@RequestBody RankVO rankVO) throws Exception {
        return rankService.deleteRank(rankVO);
    }

    @RequestMapping("/Rank/add")
    public Result add(@RequestBody RankVO rankVO) throws Exception {
        return rankService.addRank(rankVO);
    }

    @RequestMapping("/Rank/edit")
    public Result edit(@RequestBody RankVO rankVO) throws Exception {
        return rankService.updateRank(rankVO);
    }
}
