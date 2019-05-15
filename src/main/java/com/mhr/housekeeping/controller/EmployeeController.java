package com.mhr.housekeeping.controller;

import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.UserService;
import com.mhr.housekeeping.utils.Result;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;


@RestController
public class EmployeeController {
    @Autowired
    UserService userService;

    @RequestMapping("/Employee/editInfo")
    public Result editInfo(@RequestBody HashMap hashMap, HttpServletRequest request) throws Exception {
        Integer service = (Integer) hashMap.get("service");
        hashMap.remove("service");
        hashMap.remove("serviceRank");
        JSONObject object = JSONObject.fromObject(hashMap);
        UserVO userVO = (UserVO) JSONObject.toBean(object, UserVO.class);
        UserDO user = (UserDO) request.getSession().getAttribute("user");
        BeanUtils.copyProperties(userVO, user);
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return userService.updateUser(vo,service);
    }


}
