package com.mhr.housekeeping.controller;

import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.UserService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ManagerController {

    @Autowired
    private UserService userService;

    @RequestMapping("/Manager/getAdminList")
    public Result getAdminList() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setRole(100);
        List<UserVO> userVOS = userService.listUser(userVO);
        return new Result<>(userVOS);
    }

    @RequestMapping("/Manager/editAdmin")
    public Result editAdmin(@RequestBody UserVO userVO) throws Exception {
        return userService.updateUser(userVO, null);
    }

    @RequestMapping("/Manager/addAdmin")
    public Result addAdmin(@RequestBody UserVO userVO) throws Exception {
        return userService.addUser(userVO, null);
    }

    @RequestMapping("/Manager/changeState")
    public Result changeState(@RequestBody UserVO userVO) throws Exception {
        return userService.updateUserState(userVO);
    }

    @RequestMapping("/Manager/deleteAdmin")
    public Result deleteAdmin(@RequestBody UserVO userVO) throws Exception {
        return userService.deleteUser(userVO);
    }


}
