package com.mhr.housekeeping.controller.user;

import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.UserService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ManagerController {

    @Autowired
    private UserService userService;

    @RequestMapping("/Manager/getAdminList")
    public Result getAdminList() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setRole(100);
        return userService.listUser(userVO);
    }

    @RequestMapping("/Manager/editAdmin")
    public Result editAdmin(@RequestParam int id,@RequestParam String username,@RequestParam boolean state) throws Exception {
        UserVO userVO = new UserVO();
        userVO.setUsername(username);
        userVO.setId(id);
        if (state){
            userVO.setState(0);
        }else {
            userVO.setState(1);
        }
        return userService.updateUser(userVO);
    }

    @RequestMapping("/Manager/addAdmin")
    public Result addAdmin(@RequestParam String username, @RequestParam String password, @RequestParam boolean state) throws Exception {
        UserVO userVO = new UserVO();
        userVO.setRole(100);
        userVO.setPassword(password);
        userVO.setUsername(username);
        if (state){
            userVO.setState(0);
        }else {
            userVO.setState(1);
        }
        return userService.addUser(userVO);
    }

    @RequestMapping("/Manager/changeState")
    public Result changeState(@RequestParam int id, @RequestParam int state) throws Exception {
        UserVO userVO = new UserVO();
        userVO.setId(id);
        userVO.setState(state);
        return userService.updateUserState(userVO);
    }

    @RequestMapping("/Manager/deleteAdmin")
    public Result deleteAdmin(@RequestParam int id) throws Exception {
        return userService.deleteUser(new UserVO(id));
    }
    @RequestMapping("/User/editPassword")
    public Result edit(@RequestParam String new_pwd,@RequestParam String old_pwd ) throws Exception {
        return userService.editPassword(new_pwd,old_pwd);
    }
}
