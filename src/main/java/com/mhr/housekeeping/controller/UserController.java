package com.mhr.housekeeping.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mhr.housekeeping.entity.RankDO;
import com.mhr.housekeeping.entity.ServiceDO;
import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.vo.FundVO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.*;
import com.mhr.housekeeping.utils.EnumType;
import com.mhr.housekeeping.utils.Result;
import com.mhr.housekeeping.utils.SmsUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    OrdersService ordersService;
    @Autowired
    FundService fundService;
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
     * 根据userId查询所有相关service  rank 的数据并返回集合
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

    //employer登陆使用
    @RequestMapping("/User/findUserInfos2")
    public JSONObject findUserInfos2(@RequestBody HashMap hashMap) {
        Integer employeeId = (Integer) hashMap.get("employeeId");
        JSONObject userInfos = userService.findUserInfos(new UserVO(employeeId));
        return userInfos;
    }

    @RequestMapping("/User/logout")
    public Result Logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return new Result(200);
    }

    @RequestMapping("/User/getUserById")
    public Result getUserById(@RequestBody UserVO userVO) throws Exception {
        return userService.findDetailUser(userVO);
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
        hashMap.remove("service");
        //hashMap转json字符串
        String jsonString = JSON.toJSONString(hashMap, true);
//        JSONObject object = JSONObject.fromObject(hashMap);
        //json字符串转实体类对象
        UserVO userVO = JSONObject.parseObject(jsonString, UserVO.class);
//        UserVO userVO = (UserVO) JSONObject.toBean(object, UserVO.class);
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
        userVO.setBalance(0);
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
            return userService.updateUser(userVO, null);
        }
    }

    /**
     * 1.0获得所有用户的信息
     * 1.1返回值里面再添加该用户拥有的服务等级信息
     * 1.2完成订单数量
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAll")
    public JSONObject getAll(@RequestBody UserVO userVO) throws Exception {
        JSONObject object = new JSONObject();
        List<UserVO> list = userService.listUser(userVO);
        object.put("list", list);
        return object;
    }

    @RequestMapping("/User/findUserById")
    public Result findUserById(@RequestBody UserVO userVO) throws Exception {
        return userService.findDetailUser(userVO);
    }

    /**
     * 服务预定---根据条件过滤人员  条件有：省、市、服务id、过滤待岗的人员
     *
     * @param userVO
     * @return
     * @throws Exception
     */
    @RequestMapping("/User/listUserByServiceId")
    public JSONObject listUserByServiceId(@RequestBody UserVO userVO) throws Exception {
        Result result = userService.listUserByServiceId(userVO);
        JSONObject object = new JSONObject();
        object.put("list", result.getData());
        return object;
    }

    @RequestMapping("/User/listUserByServiceIds")
    public JSONObject listUserByServiceIds(@RequestBody UserVO userVO) throws Exception {
        List<UserVO> userVOS = new ArrayList<>();
        List<Integer> serviceIds = userVO.getServiceIds();
        if (serviceIds != null && serviceIds.size() > 0) {
            serviceIds.forEach(serviceId -> {
                userVO.setServiceId(serviceId);
                Result result = userService.listUserByServiceId(userVO);
                List<UserVO> data = (List<UserVO>) result.getData();
                userVOS.addAll(data);
            });
        }
        //去除id相同的对象
        for (int i = 0; i < userVOS.size() - 1; i++) {
            for (int j = userVOS.size() - 1; j > i; j--) {
                if (userVOS.get(j).getId() == userVOS.get(i).getId()) {
                    userVOS.remove(j);
                }
            }
        }
        JSONObject object = new JSONObject();
        object.put("list", userVOS);
        return object;
    }

    @RequestMapping("/User/changeEmployeeState")
    public Result changeState(@RequestBody UserVO userVO) throws Exception {
        return userService.updateUser(userVO, null);

    }

    @RequestMapping("/User/findUserByOrder")
    public UserDO findUserByOrder(@RequestBody HashMap hashMap) throws Exception {
        Integer orderId = (Integer) hashMap.get("orderId");
        return userService.findUserByOrder(orderId);

    }

    @RequestMapping("/User/deleteEmployee")
    public Result deleteEmployee(@RequestBody UserVO userVO) throws Exception {
        return userService.deleteUser(userVO);
    }


    @RequestMapping("/User/recharge")
    public Result recharge(@RequestBody HashMap hashMap, HttpServletRequest request) throws Exception {
        UserDO user = (UserDO) request.getSession().getAttribute("user");
        Integer balance = user.getBalance();
        Integer money = (Integer) hashMap.get("money");
        user.setBalance(balance + money);
        return userService.updateUserBalance(user, money);
    }

    @RequestMapping("/User/getBalance")
    public UserDO getBalance(@RequestBody UserVO userVO) throws Exception {
        Result detailUser = userService.findDetailUser(userVO);
        UserDO data = (UserDO) detailUser.getData();
        return data;
    }

    /**
     * 为雇员首页 收益、订单数、好评数、好评率、退款率、额外收益、最优服务、最高等级、每月的收益统计
     *
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping("/User/statistics")
    public JSONObject statistics(HttpServletRequest request) throws Exception {
        JSONObject object = new JSONObject();
        UserDO user = (UserDO) request.getSession().getAttribute("user");
        OrdersVO ordersVO = new OrdersVO();
        ordersVO.setEmployeeId(user.getId());
        object.put("balance", user.getBalance());
        //根据userId查询完成的订单数
        Integer orderCount = ordersService.countOrdersByEmployeeId(new UserVO(user.getId()));
        object.put("orderCount", orderCount);
        //好评量
        Integer goodComment = ordersService.countOrdersWithGoodComment(ordersVO);
        object.put("goodComment", goodComment);
        //好评率
        Integer comment = ordersService.countOrdersWithComment(ordersVO);
        float goodPercent = (float) goodComment / comment;
        object.put("goodPercent", goodPercent);
        //退款率
        Integer countOrders = ordersService.countOrders(ordersVO);
        Integer refundCount = ordersService.countOrdersWithReason(ordersVO);
        float refundPercent = (float) refundCount / countOrders;
        object.put("refundPercent", refundPercent);
        //额外收益
        FundVO vo = new FundVO();
        vo.setUserId(user.getId());
        vo.setType(4);
        Integer extra = fundService.getExtra(vo);
        object.put("extra", extra);
        //最优服务--雇员被点最多的服务
        Integer serviceId = ordersService.getServiceMost(user.getId());
        ServiceDO detailService = serviceService.findDetailService(new ServiceVO(serviceId));
        object.put("goodService", detailService.getName());
        //最高等级
        RankDO rankDO = rankService.getMostRank(user.getId());
        object.put("rank", rankDO.getName());
        return object;
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
