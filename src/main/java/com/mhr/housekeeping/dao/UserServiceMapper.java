package com.mhr.housekeeping.dao;

import com.mhr.housekeeping.entity.UserServiceDO;
import com.mhr.housekeeping.entity.vo.UserServiceVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 
 * <br/>
 * Created by min on 2019/04/28
 */
@Repository
public interface UserServiceMapper {

    Integer addUserService(UserServiceVO userServiceVO);

    Integer updateUserService(UserServiceVO userServiceVO);

    UserServiceDO findDetailUserService(UserServiceVO userServiceVO);

    List<UserServiceVO> listUserService(UserServiceVO userServiceVO);

    List<UserServiceDO> listUserServicePage(UserServiceVO userServiceVO);

    Integer countUserService(UserServiceVO userServiceVO);

    Integer deleteUserService(UserServiceVO userServiceVO);

    Integer deleteUserServiceByRanId(Integer id);

    Integer deleteUserServiceByServiceId(Integer id);

    Integer deleteUserServiceByUserId(Integer id);

    Integer updateUserServiceRank(UserServiceVO userServiceVO);

    UserServiceVO findUserServiceByOther(UserServiceVO userServiceVO);
}
