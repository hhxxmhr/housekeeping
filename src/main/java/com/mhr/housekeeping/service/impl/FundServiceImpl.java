package com.mhr.housekeeping.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhr.housekeeping.dao.*;
import com.mhr.housekeeping.entity.RankDO;
import com.mhr.housekeeping.entity.ServiceDO;
import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.vo.FundVO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.entity.vo.UserVO;
import com.mhr.housekeeping.service.FundService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <br/>
 * Created by min on 2019/05/11
 */
@Service("fundService")
public class FundServiceImpl implements FundService {

    private final static Logger LOG = LoggerFactory.getLogger(FundServiceImpl.class);

    @Resource
    private FundMapper fundMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ServiceMapper serviceMapper;
    @Resource
    RankMapper rankMapper;
    @Resource
    private OrdersMapper ordersMapper;

    @Override
    public Result addFund(FundVO fundVO) throws Exception {
        return null;
    }

    @Override
    public Result updateFund(FundVO fundVO) throws Exception {
        return null;
    }

    @Override
    public Result findDetailFund(FundVO fundVO) throws Exception {
        return null;
    }

    @Override
    public PageInfo<FundVO> listFund(FundVO fundVO) throws Exception {
        PageHelper.startPage(fundVO.getPage(), fundVO.getSize());
        List<FundVO> list = fundMapper.listFundByManager(fundVO);
        if (list != null && list.size() > 0) {
            list.forEach(it -> {
                //根据userId查询关于用户的信息
                UserDO detailUser = userMapper.findDetailUser(new UserVO(it.getUserId()));
                it.setUserDO(detailUser);
                if (it.getOrderId() != null) {
                    //说明他是与订单相关的记录--根据订单id查询订单的信息
                    OrdersVO vo = ordersMapper.findDetailOrdersVo(new OrdersVO(it.getOrderId()));
                    UserDO employee = userMapper.findDetailUser(new UserVO(vo.getEmployeeId()));
                    UserDO employer = userMapper.findDetailUser(new UserVO(vo.getEmployerId()));
                    ServiceDO service = serviceMapper.findDetailService(new ServiceVO(vo.getServiceId()));
                    it.setServiceName(service.getName());
                    it.setEmployeeUsername(employee.getUsername());
                    it.setEmployerUsername(employer.getUsername());
                    it.setEmployeeName(employee.getName());
                    it.setEmployerName(employer.getName());
                    RankDO rankDO = rankMapper.findRankByOrder(vo.getEmployeeId(), vo.getServiceId());
                    it.setRankName(rankDO.getName());
                    it.setRankMoney(rankDO.getMoney());
                }
            });
        }
        return new PageInfo<>(list);
    }

    @Override
    public Result listFundPage(FundVO fundVO) throws Exception {
        return null;
    }

    @Override
    public Result countFund(FundVO fundVO) throws Exception {
        return null;
    }

    @Override
    public Result deleteFund(FundVO fundVO) throws Exception {
        return null;
    }

    @Override
    public Integer getExtra(FundVO vo) {
        return fundMapper.getExtra(vo);
    }

    @Override
    public Integer getTotalIncome(long startTime, long endTime, Integer userId) {
        return fundMapper.getTotalIncome(startTime, endTime, userId);
    }

    @Override
    public Integer getTotalRefund(long startTime, long endTime, Integer userId) {
        return fundMapper.getTotalRefund(startTime, endTime, userId);
    }

}