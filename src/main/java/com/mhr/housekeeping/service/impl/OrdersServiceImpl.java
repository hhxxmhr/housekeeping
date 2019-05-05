package com.mhr.housekeeping.service.impl;

import com.mhr.housekeeping.dao.OrdersMapper;
import com.mhr.housekeeping.entity.OrdersDO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.service.OrdersService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <br/>
 * Created by min on 2019/05/04
 */
@Service("ordersService")
public class OrdersServiceImpl implements OrdersService {

    private final static Logger LOG = LoggerFactory.getLogger(OrdersServiceImpl.class);

    @Resource
    private OrdersMapper ordersMapper;

    @Override
    public Result addOrders(OrdersVO ordersVO) throws Exception {
        Integer count = ordersMapper.addOrders(ordersVO);
        if (count > 0) {
            return Result.getSuccess("添加成功");
        }
        return Result.getFailure("添加失败");
    }

    @Override
    public Result updateOrders(OrdersVO ordersVO) throws Exception {
        return null;
    }

    @Override
    public Result findDetailOrders(OrdersVO ordersVO) throws Exception {
        return null;
    }

    @Override
    public Result listOrders(OrdersVO ordersVO) throws Exception {
        //如果登陆的是管理员，则显示所有的订单
        if (ordersVO.getRole() == 100) {
            List<OrdersDO> orders = ordersMapper.listOrders(ordersVO);
            return new Result<>(orders);
        } else {
            //根据userId显示对应的订单
            OrdersVO vo = new OrdersVO();
            if (ordersVO.getRole() == 200) {
                vo.setEmployeeId(ordersVO.getUserId());
            } else if (ordersVO.getRole() == 300) {
                vo.setEmployerId(ordersVO.getUserId());
            }
            List<OrdersDO> list = ordersMapper.listOrders(vo);
            return new Result<>(list);
        }
        /*orders.forEach(order -> {
            //根据一条订单里的employeeId,查询人员的信息
            //根据一条订单里的employeId,查询雇主的信息

        });*/
//        return null;
    }

    @Override
    public Result listOrdersPage(OrdersVO ordersVO) throws Exception {
        return null;
    }

    @Override
    public Result countOrders(OrdersVO ordersVO) throws Exception {
        return null;
    }

    @Override
    public Result deleteOrders(OrdersVO ordersVO) throws Exception {
        return null;
    }

}