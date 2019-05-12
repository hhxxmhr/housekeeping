package com.mhr.housekeeping.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mhr.housekeeping.dao.*;
import com.mhr.housekeeping.entity.ServiceDO;
import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.UserServiceDO;
import com.mhr.housekeeping.entity.vo.FundVO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.entity.vo.UserServiceVO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.UserService;
import com.mhr.housekeeping.service.UserServiceService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <br/>
 * Created by mhr on 2019/04/07
 */
@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    HttpServletRequest request;
    @Resource
    private UserMapper userMapper;
    @Resource
    private FundMapper fundMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private ServiceMapper serviceMapper;
    @Resource
    private UserServiceMapper userServiceMapper;
    @Autowired
    UserServiceService userServiceService;

    @Override
    public Result addUser(UserVO userVO, List<Integer> serviceList) throws Exception {
        Integer count = userMapper.addUser(userVO);
        if (count > 0) {
            if (serviceList.size() > 0) {
                //更新UserService关联表
                Integer userId = userVO.getId();//获取数据库里面自增的id
                UserServiceVO userServiceVO = new UserServiceVO();
                userServiceVO.setUserId(userId);
                userServiceVO.setRankId(1);
                for (int i = 0; i < serviceList.size(); i++) {
                    userServiceVO.setServiceId(serviceList.get(i));
                    userServiceService.addUserService(userServiceVO);
                }
            }
            return Result.getSuccess("用户添加成功");
        }
        return Result.getFailure("用户添加失败");
    }

    @Override
    public Result updateUser(UserVO userVO, Integer service) throws Exception {
        Integer count = userMapper.updateUser(userVO);
        if (count > 0) {
            //更新user_service
            if (service != null) {
                //添加服务
                UserServiceVO userServiceVO = new UserServiceVO();
                userServiceVO.setUserId(userVO.getId());
                userServiceVO.setServiceId(service);
                userServiceVO.setRankId(1);
                userServiceService.addUserService(userServiceVO);
            }
            return Result.getSuccess("修改成功");
        }
        return Result.getFailure("修改失败");
    }

    @Override
    public Result findDetailUser(UserVO userVO) throws Exception {
        UserDO detailUser = userMapper.findDetailUser(userVO);
        return new Result<>(detailUser);
    }

    @Override
    public List<UserVO> listUser(UserVO userVO) throws Exception {
        List<UserVO> users = userMapper.listUser(userVO);
        moreInfo(users);
        return users;
    }

    @Override
    public Result listUserByServiceId(UserVO userVO) {
        List<UserVO> userVOS = userMapper.listUserByServiceId(userVO);
        moreInfo(userVOS);
        return new Result<>(userVOS);
    }

    @Override
    public Result listUserPage(UserVO userVO) throws Exception {
        return null;
    }

    @Override
    public Result countUser(UserVO userVO) throws Exception {
        return null;
    }

    @Override
    public Result deleteUser(UserVO userVO) throws Exception {
        UserServiceVO userServiceVO = new UserServiceVO();
        userServiceVO.setUserId(userVO.getId());
        List<UserServiceDO> list = userServiceMapper.listUserService(userServiceVO);
        if (list.size() > 0) {
            Integer r = userServiceMapper.deleteUserServiceByUserId(userVO.getId());
            if (r > 0) {
                Integer count = userMapper.deleteUser(userVO);
                if (count > 0) {
                    return new Result(Result.CODE_SUCCESS, "删除成功");
                }
                return new Result(Result.CODE_FAILURE, "删除失败");
            }
        }
        Integer count = userMapper.deleteUser(userVO);
        if (count > 0) {
            return new Result(Result.CODE_SUCCESS, "删除成功");
        }
        return new Result(Result.CODE_FAILURE, "删除失败");
    }

    @Override
    public UserDO getUserByUsername(UserVO username) throws Exception {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public Result updateUserState(UserVO userVO) {
        Integer count = userMapper.updateUser(userVO);
        if (count > 0) {
            return new Result(Result.CODE_SUCCESS, "更新成功");
        }
        return null;
    }

    @Override
    public Result editPassword(String new_pwd, String old_pwd) {
        //更新密码
//        userMapper.findDetailUser();
        return null;
    }

    @Override
    public UserDO getUserByPhone(UserVO userVO) {
        return userMapper.getUserByPhone(userVO);
    }

    @Override
    public UserDO getUserByIdCard(UserVO userVO) {
        return userMapper.getUserByIdCard(userVO);
    }

    @Override
    public UserDO getUserByBankCard(UserVO userVO) {
        return userMapper.getUserByBankCard(userVO);
    }

    @Override
    public Result updateUserByUsername(UserVO userVO) {
        Integer count = userMapper.updateUserByUsername(userVO);
        if (count > 0) {
            return Result.getSuccess("修改成功");
        }
        return Result.getSuccess("修改失败");
    }

    @Override
    public JSONObject findUserInfos(UserVO userVO) {
        List<UserVO> userInfos = userMapper.findUserInfos(userVO);
        JSONObject object = new JSONObject();
        object.put("data", userInfos);
        return object;
    }

    @Override
    public UserDO findUserByOrder(Integer orderId) {
        return userMapper.findUserByOrder(orderId);
    }

    /**
     * 充值
     *
     * @param money   充值金额
     * @return
     */
    @Override
    public Result updateUserBalance(UserDO userDO ,Integer money) {
        Integer count = userMapper.updateUser2(userDO);
        if (count > 0) {
            //更新资金记录表
            FundVO vo = new FundVO();
            vo.setBalance(userDO.getBalance());
            vo.setChangeMoney(money);
            vo.setCreateTime(System.currentTimeMillis() / 1000);
            vo.setType(0);
            vo.setUserId(userDO.getId());
            Integer res = fundMapper.addFund(vo);
            if (res > 0) return Result.getSuccess("充值成功");
            else return Result.getFailure("充值失败");
        } else return Result.getFailure("充值失败");
    }

    public void moreInfo(List<UserVO> userVOS) {
        if (userVOS != null && userVOS.size() > 0) {
            userVOS.forEach(it -> {
                //根据员工id查询所拥有的服务
                List<ServiceDO> serviceByUserId = serviceMapper.findServiceByUserId(it.getId());
                it.setServices(serviceByUserId);
                //根据员工id查询完成的订单数
                if (it.getRole() == 200) {
                    Integer orderCount = ordersMapper.countOrdersByEmployeeId(it);
                    it.setOrderCount(orderCount);
                    //计算此员工的好评率
                    // 1、这个员工一共有几条带评论的订单数 2、 这总数里面有几条是好评的  3、计算
                    OrdersVO orderVO = new OrdersVO();
                    orderVO.setState(4);
                    orderVO.setEmployeeId(it.getId());
                    Integer totalComment = ordersMapper.countOrders(orderVO);
                    Integer goodComment = ordersMapper.countGoodOrders(orderVO);
                    it.setTotalComment(totalComment);
                    it.setGoodComment(goodComment);
                } else if (it.getRole() == 300) {
                    Integer orderCount = ordersMapper.countOrdersByEmployerId(it);
                    it.setOrderCount(orderCount);//有效的订单数  状态3/4
                    //计算此员工的好评率
                    // 1、这个员工一共有几条带评论的订单数 2、 这总数里面有几条是好评的  3、计算
                    OrdersVO orderVO = new OrdersVO();
                    orderVO.setState(4);
                    orderVO.setEmployerId(it.getId());
                    Integer totalComment = ordersMapper.countOrders(orderVO);//评论过的订单数 状态4
                    Integer goodComment = ordersMapper.countGoodOrders2(orderVO);
                    it.setTotalComment(totalComment);
                    it.setGoodComment(goodComment);
                }

            });
        }
    }

}