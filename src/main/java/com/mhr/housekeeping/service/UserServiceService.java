package com.mhr.housekeeping.service;


import com.mhr.housekeeping.entity.UserServiceDO;
import com.mhr.housekeeping.entity.vo.UserServiceVO;
import com.mhr.housekeeping.utils.Result;

import java.util.List;

/**
 * <br/>
 * Created by min on 2019/04/28
 */
public interface UserServiceService {

	Result addUserService(UserServiceVO userServiceVO) throws Exception;

	Result updateUserService(UserServiceVO userServiceVO) throws Exception;
	
	Result findDetailUserService(UserServiceVO userServiceVO) throws Exception;
	
	List<UserServiceVO> listUserService(UserServiceVO userServiceVO) throws Exception;

    Result listUserServicePage(UserServiceVO userServiceVO) throws Exception;
	
	Result countUserService(UserServiceVO userServiceVO) throws Exception;
	
	Result deleteUserService(UserServiceVO userServiceVO) throws Exception;

    Integer deleteUserServiceByRanId(Integer id);

	Integer deleteUserServiceByServiceId(Integer id);

	Integer updateUserServiceRank(UserServiceVO userServiceVO);

	UserServiceVO findUserServiceByOther(UserServiceVO userServiceVO);
}