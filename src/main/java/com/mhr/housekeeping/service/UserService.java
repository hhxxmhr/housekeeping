package com.mhr.housekeeping.service;


import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.utils.Result;

/**
 * <br/>
 * Created by min on 2019/04/24
 */
public interface UserService {

	Result addUser(UserVO userVO) throws Exception;

	Result updateUser(UserVO userVO) throws Exception;
	
	Result findDetailUser(UserVO userVO) throws Exception;
	
	Result listUser(UserVO userVO) throws Exception;

    Result listUserPage(UserVO userVO) throws Exception;
	
	Result countUser(UserVO userVO) throws Exception;
	
	Result deleteUser(UserVO userVO) throws Exception;

	UserDO getUserByUsername(UserVO username) throws Exception;

	Result updateUserState(UserVO userVO);

	Result editPassword(String new_pwd, String old_pwd);

	UserDO getUserByPhone(UserVO userVO);

	UserDO getUserByIdCard(UserVO userVO);

	UserDO getUserByBankCard(UserVO userVO);

    Result updateUserByUsername(UserVO userVO);

}