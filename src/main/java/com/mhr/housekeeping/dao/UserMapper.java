package com.mhr.housekeeping.dao;

import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.vo.UserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br/>
 * Created by mhr on 2019/04/07
 */
@Repository
public interface UserMapper {

    Integer addUser(UserVO userVO);

    Integer updateUser(UserVO userVO);

    UserDO findDetailUser(UserVO userVO);

    List<UserVO> listUser(UserVO userVO);

    List<UserDO> listUserPage(UserVO userVO);

    Integer countUser(UserVO userVO);

    Integer deleteUser(UserVO userVO);

    UserDO getUserByUsername(UserVO username);

    UserDO getUserByPhone(UserVO userVO);

    UserDO getUserByIdCard(UserVO userVO);

    UserDO getUserByBankCard(UserVO userVO);

    Integer updateUserByUsername(UserVO userVO);

    List<UserDO> getUserForUpdate(UserVO userVO);

    List<UserVO> findUserInfos(UserVO userVO);

    List<UserVO> listUserByServiceId(UserVO userVO);

    UserDO findUserByOrder(Integer orderId);

    Integer updateUserBalance(Integer id, Integer balance);

    Integer updateUser2(UserDO userDO);
}
