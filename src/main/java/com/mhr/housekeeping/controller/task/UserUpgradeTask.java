package com.mhr.housekeeping.controller.task;

import com.mhr.housekeeping.entity.UserServiceDO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.entity.vo.UserServiceVO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.OrdersService;
import com.mhr.housekeeping.service.UserService;
import com.mhr.housekeeping.service.UserServiceService;
import com.mhr.housekeeping.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserUpgradeTask {
    @Autowired
    UserService userService;
    @Autowired
    UserServiceService userServiceService;
    @Autowired
    OrdersService ordersService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void updateGrade() throws Exception {
        //查看每个用户在他擅长的服务里面，完成订单数>20就升中级
        //>50&&好评率>50%就升高级
        List<UserVO> users = userService.listUser(new UserVO());
        if (users != null && users.size() > 0) {
            users.forEach(user -> {
                try {
                    List<UserServiceVO> services = userServiceService.listUserService(new UserServiceVO());
                    if (services != null && services.size() > 0) {
                        services.forEach(service -> {
                            //根据雇员id和服务id,count 完成的订单数
                            OrdersVO ordersVO = new OrdersVO();
                            ordersVO.setEmployeeId(user.getId());
                            ordersVO.setServiceId(service.getServiceId());
                            try {
                                Integer count = ordersService.countOrdersOver(ordersVO);
                                //这个人这项服务的好评率  好评占评论
                                Integer comments = ordersService.countOrdersWithComment(ordersVO);
                                Integer goodComments = ordersService.countOrdersWithGoodComment(ordersVO);
                                float per = (float) goodComments / comments;
                                if (count > 20 && per > 0.5) {
                                    //更新人员这项服务的等级
                                    service.setRankId(2);
                                    Result result = userServiceService.updateUserService(service);
                                }else if (count > 40 && per > 0.45) {
                                    //更新人员这项服务的等级
                                    service.setRankId(3);
                                    Result result = userServiceService.updateUserService(service);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
