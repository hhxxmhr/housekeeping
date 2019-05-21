package com.mhr.housekeeping.controller.task;

import com.mhr.housekeeping.entity.RankDO;
import com.mhr.housekeeping.entity.UserServiceDO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.entity.vo.RankVO;
import com.mhr.housekeeping.entity.vo.UserServiceVO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.OrdersService;
import com.mhr.housekeeping.service.RankService;
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
    RankService rankService;
    @Autowired
    OrdersService ordersService;

        @Scheduled(cron = "0 0 8 * * ?")
//    @Scheduled(cron = "0 0/1 * * * ?")
    //每隔2秒执行一次
//    @Scheduled(fixedRate = 2000)
    public void updateGrade() throws Exception {
        //查看每个用户在他擅长的服务里面，完成订单数>20就升中级
        //>50&&好评率>50%就升高级

        //员工与服务的记录(返回的一个用户 对  多个 服务)
        List<UserVO> list = userService.findUserAndService();
        if (list.size() > 0) {
            list.forEach(it -> {
                OrdersVO ordersVO = new OrdersVO();
                ordersVO.setEmployeeId(it.getId());
                ordersVO.setServiceId(it.getServiceId());
                //查询每个员工每一项服务的订单数
                Integer count = ordersService.countOrdersOver(ordersVO);
                it.setServiceOrderCount(count);
                //根据员工和服务查询订单的好评率
                Integer goodComment = ordersService.countOrdersWithGoodComment(ordersVO);
                Integer comment = ordersService.countOrdersWithComment(ordersVO);
                float per;
                if (comment > 0) {
                    per = (float) goodComment / comment;
                } else {
                    per = 0;
                }
                it.setGoodPer(per);
            });
            list.forEach(item -> {
                if (item.getServiceOrderCount() > 10 && item.getGoodPer() > 0.5) {
                    UserServiceVO userServiceVO = new UserServiceVO();
                    userServiceVO.setUserId(item.getId());
                    userServiceVO.setServiceId(item.getServiceId());
                    //获得这个人这项服务的等级信息
                    UserServiceVO vo = userServiceService.findUserServiceByOther(userServiceVO);
                    //遍历所有的等级
                    try {
                        List<RankDO> ranks = rankService.listRankOrderByMoney(new RankVO());
                        for (int i = 0; i < ranks.size(); i++) {
                            if (ranks.get(i).getId().equals(item.getRankId())) {
                                userServiceVO.setRankId(ranks.get(i + 1).getId());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Integer count = userServiceService.updateUserServiceRank(userServiceVO);
                    if (count > 0) {
                        System.out.println("用户id" + item.getId() + "服务id" + item.getServiceId() + "等级提升完毕");
                    }
                }
                if (item.getServiceOrderCount() > 20 && item.getGoodPer() > 0.45) {
                    UserServiceVO userServiceVO = new UserServiceVO();
                    userServiceVO.setUserId(item.getId());
                    userServiceVO.setServiceId(item.getServiceId());
                    //获得这个人这项服务的等级信息
                    UserServiceVO vo = userServiceService.findUserServiceByOther(userServiceVO);
                    //遍历所有的等级
                    try {
                        List<RankDO> ranks = rankService.listRankOrderByMoney(new RankVO());
                        for (int i = 0; i < ranks.size(); i++) {
                            if (ranks.get(i).getId().equals(item.getRankId())) {
                                userServiceVO.setRankId(ranks.get(i + 1).getId());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Integer count = userServiceService.updateUserServiceRank(userServiceVO);
                    if (count > 0) {
                        System.out.println("用户id" + item.getId() + "服务id" + item.getServiceId() + "等级提升完毕");
                    }
                }
            });
        }
    }

}
