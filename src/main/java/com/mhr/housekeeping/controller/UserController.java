package com.mhr.housekeeping.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.RankService;
import com.mhr.housekeeping.service.ServiceService;
import com.mhr.housekeeping.service.UserService;
import com.mhr.housekeeping.service.UserServiceService;
import com.mhr.housekeeping.utils.EnumType;
import com.mhr.housekeeping.utils.Result;
import com.mhr.housekeeping.utils.SmsUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 管理登陆的操作
 */

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserServiceService userServiceService;
    @Autowired
    ServiceService serviceService;
    @Autowired
    RankService rankService;
    /*@Autowired
    HttpServletRequest request;*/
    static Integer code;

    @RequestMapping("/User/login")
    public Result login(@RequestBody UserVO uservO, HttpServletRequest request) throws Exception {
        UserDO user = userService.getUserByUsername(uservO);
        if (user != null) {
            if (user.getPassword().equals(uservO.getPassword())) {
                if (user.getState() == EnumType.Normal || user.getState() == EnumType.Free || user.getState() == EnumType.busy) {
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
    public Result getMineInfo(HttpServletRequest request) {
        UserDO user = (UserDO) request.getSession().getAttribute("user");
        if (user == null) {
            return new Result<>(true);
        }
        return new Result<>(user);
    }

    /**
     * 根据userId查询所有相关的数据并返回
     *
     * @param request
     * @return
     */
    @RequestMapping("/User/findUserInfos")
    public JSONObject findUserInfos(HttpServletRequest request) {
        UserDO user = (UserDO) request.getSession().getAttribute("user");
        JSONObject userInfos = userService.findUserInfos(new UserVO(user.getId()));
        return userInfos;
    }

    @RequestMapping("/User/logout")
    public Result Logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return new Result(200);
    }

    @RequestMapping("/User/getCode")
    public Result getCode(@RequestBody UserVO userVO) throws Exception {
        UserDO userByPhone = userService.getUserByPhone(userVO);
        if (userByPhone != null) {
            return Result.getFailure("手机号已注册");
        }
        return message(userVO);
    }

    @RequestMapping("/User/getForgetCode")
    public Result getForgetCode(@RequestBody UserVO userVO) throws Exception {
        UserDO userByUsername = userService.getUserByUsername(userVO);
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
    public Result register(@RequestBody HashMap hashMap) throws Exception {
        List<Integer> serviceList = (List<Integer>) hashMap.get("service");
        System.out.println(serviceList);
        hashMap.remove("service");
        JSONObject object = JSONObject.fromObject(hashMap);
        UserVO userVO = (UserVO) JSONObject.toBean(object, UserVO.class);
        //更新user表
//        UserDO user = userService.getUserByUsername(userVO);
        /*if (user != null) {
            return Result.getFailure("此账号已存在");
        }
        UserDO user2 = userService.getUserByPhone(userVO);
        if (user2 != null) {
            return Result.getFailure("手机号已注册");
        }
        UserDO user3 = userService.getUserByIdCard(userVO);
        if (user3 != null) {
            return Result.getFailure("请检查身份证号");
        }
        UserDO user4 = userService.getUserByBankCard(userVO);
        if (user4 != null) {
            return Result.getFailure("请检查银行卡号");
        }*/
        userVO.setCreateTime(System.currentTimeMillis() / 1000);
        userVO.setState(EnumType.check);
        return userService.addUser(userVO, serviceList);
    }

    @RequestMapping("/User/forgetPwd")
    public Result forgetPwd(@RequestBody UserVO userVO) throws Exception {
        UserDO userByUsername = userService.getUserByUsername(userVO);
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
    public Result editPwd(@RequestBody HashMap hashMap, HttpServletRequest request) throws Exception {
        UserDO user = (UserDO) request.getSession().getAttribute("user");
        if (!user.getPassword().equals(hashMap.get("old_pwd"))) {
            return Result.getFailure("原密码错误");
        } else {
            user.setPassword((String) hashMap.get("new_pwd"));
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userService.updateUser(userVO,null);
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
        return userService.updateUser(userVO,null);

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
