package com.mhr.housekeeping.service.impl;

import com.mhr.housekeeping.dao.UserMapper;
import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.UserService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public Result addUser(UserVO userVO) throws Exception {
        Integer count = userMapper.addUser(userVO);
        if (count > 0) {
            return new Result(Result.CODE_SUCCESS, "添加成功");
        }
        return null;
    }

    @Override
    public Result updateUser(UserVO userVO) throws Exception {
        Integer count = userMapper.updateUser(userVO);
        if (count > 0) {
            return new Result(Result.CODE_SUCCESS, "修改成功");
        }
        return new Result(Result.CODE_FAILURE, "修改失败");
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
        Integer count = userMapper.deleteUser(userVO);
        if (count > 0) {
            return new Result(Result.CODE_SUCCESS, "删除成功");
        }
        return new Result(Result.CODE_FAILURE, "删除失败");
    }

    @Override
    public UserVO getUserByUsername(String username) throws Exception {
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
    public UserVO getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    @Override
    public UserVO getUserByIdCard(String idCard) {
        return userMapper.getUserByIdCard(idCard);
    }

    @Override
    public UserVO getUserByBankCard(String bankCard) {
        return userMapper.getUserByBankCard(bankCard);
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