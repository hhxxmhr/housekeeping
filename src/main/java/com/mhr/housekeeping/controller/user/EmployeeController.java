package com.mhr.housekeeping.controller.user;

import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.UserService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class EmployeeController {
    @Autowired
    UserService userService;

    @RequestMapping("/Employee/editInfo")
    public Result editInfo(@RequestBody UserVO userVO, HttpServletRequest request) throws Exception {
        System.out.println(userVO);
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        BeanUtils.copyProperties(userVO, user);
        return userService.updateUser(user);
    }


}
