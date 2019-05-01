package com.mhr.housekeeping.service.impl;

import com.mhr.housekeeping.dao.UserMapper;
import com.mhr.housekeeping.dao.UserServiceMapper;
import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.UserServiceDO;
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
import javax.print.attribute.standard.NumberUp;
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
    public Result updateUser(UserVO userVO) throws Exception {
        List<UserDO> getUserForUpdate = userMapper.getUserForUpdate(userVO);

        /*if (getUserForUpdate != null && getUserForUpdate.size() > 0  ) {
            return Result.getFailure("账号、手机号已被注册");
        } else {*/
        Integer count = userMapper.updateUser(userVO);
        if (count > 0) {
            return Result.getSuccess("修改成功");
        }
//        }
        return Result.getFailure("修改失败");
    }

    @Override
    public Result findDetailUser(UserVO userVO) throws Exception {
        return null;
    }

    @Override
    public Result listUser(UserVO userVO) throws Exception {
        List<UserDO> users = userMapper.listUser(userVO);
        return new Result<>(Result.CODE_SUCCESS, users);
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

}