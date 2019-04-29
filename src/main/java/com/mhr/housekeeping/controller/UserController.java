package com.mhr.housekeeping.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.UserService;
import com.mhr.housekeeping.utils.EnumType;
import com.mhr.housekeeping.utils.Result;
import com.mhr.housekeeping.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 管理登陆的操作
 */

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest request;
    static Integer code;

    @RequestMapping("/User/login")
    public Result login(@RequestBody UserVO userVO) throws Exception {
        UserVO user = userService.getUserByUsername(userVO.getUsername());
        if (user != null) {
            if (user.getPassword().equals(userVO.getPassword())) {
                if (user.getState() == EnumType.Normal||user.getState() == EnumType.Free||user.getState() == EnumType.busy) {
                    request.getSession().setAttribute("user", user);
                    return new Result<>(200, "登录成功", user);
                } else if (user.getState() == EnumType.Disable) {
                    return Result.getFailure("账号被封停");
                } else if (user.getState() == EnumType.check) {
                    return Result.getFailure("账号正在审核中");
                }
            } else return Result.getFailure("密码错误，请重新输入");
        }
        return Result.getFailure("用户不存在");
    }

    @RequestMapping("/User/getMineInfo")
    public Result getMineInfo() {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            return new Result<>(true);
        }
        return new Result<>(user);
    }

    @RequestMapping("/User/logout")
    public Result Logout() {
        request.getSession().removeAttribute("user");
        return new Result(200);
    }

    @RequestMapping("/User/getCode")
    public Result getCode(@RequestBody UserVO userVO) throws Exception {
        UserVO user = userService.getUserByPhone(userVO.getPhone());
        if (user != null) {
            return Result.getFailure("手机号已注册");
        }
        return message(userVO);
    }

    @RequestMapping("/User/getForgetCode")
    public Result getForgetCode(@RequestBody UserVO userVO) throws Exception {
        UserVO userByUsername = userService.getUserByUsername(userVO.getUsername());
        if (userByUsername == null) {
            return Result.getFailure("用户不存在");
        } else {
            if (!userByUsername.getPhone().equals(userVO.getPhone())) {
                return Result.getFailure("手机号不正确");
            }
        }
        return message(userVO);
    }

    @RequestMapping("/User/register")
    public Result register(@RequestBody UserVO userVO) throws Exception {
        UserVO user = userService.getUserByUsername(userVO.getUsername());
        if (user != null) {
            return Result.getFailure("此账号已存在");
        }
        UserVO user2 = userService.getUserByPhone(userVO.getPhone());
        if (user2 != null) {
            return Result.getFailure("手机号已注册");
        }
        UserVO user3 = userService.getUserByIdCard(userVO.getIdCard());
        if (user3 != null) {
            return Result.getFailure("请检查身份证号");
        }
        UserVO user4 = userService.getUserByBankCard(userVO.getBankCard());
        if (user4 != null) {
            return Result.getFailure("请检查银行卡号");
        }
        userVO.setCreateTime(System.currentTimeMillis()/1000);
        userVO.setState(EnumType.check);
        return userService.addUser(userVO);
    }

    @RequestMapping("/User/forgetPwd")
    public Result forgetPwd(@RequestBody UserVO userVO) throws Exception {
        UserVO userByUsername = userService.getUserByUsername(userVO.getUsername());
        if (userByUsername == null) {
            return Result.getFailure("用户不存在");
        } else {
            if (!userByUsername.getPhone().equals(userVO.getPhone())) {
                return Result.getFailure("手机号不正确");
            }
        }
        return userService.updateUserByUsername(userVO);
    }

    @RequestMapping("/User/editPwd")
    public Result editPwd(@RequestBody HashMap hashMap) throws Exception {
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        if (!user.getPassword().equals(hashMap.get("old_pwd"))) {
            return Result.getFailure("原密码错误");
        } else {
            user.setPassword((String) hashMap.get("new_pwd"));
            return userService.updateUser(user);
        }
    }

    @RequestMapping("/getAll")
    public JSONObject getAll(@RequestBody UserVO userVO) throws Exception {
        JSONObject object = new JSONObject();
        Result result = userService.listUser(userVO);
        object.put("list", result.getData());
        return object;
    }

    @RequestMapping("/User/changeEmployeeState")
    public Result changeState(@RequestBody UserVO userVO) throws Exception {
        return userService.updateUser(userVO);

    }

    @RequestMapping("/User/deleteEmployee")
    public Result deleteEmployee(@RequestBody UserVO userVO) throws Exception {
        return userService.deleteUser(userVO);

    }
    public Result message(UserVO userVO) throws ClientException, InterruptedException {
        code = 0;
        for (int j = 0; j < 100; j++) {
            code = ((int) ((Math.random() * 9 + 1) * 100000));
        }
        SendSmsResponse sendMessage = SmsUtils.sendmessage(userVO.getPhone(), code);
        if (sendMessage.getBizId() != null) {
            return new Result<>(200, "获取成功", code);//返回验证码
        }
        return null;
    }
}
