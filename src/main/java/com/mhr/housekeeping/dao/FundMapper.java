package com.mhr.housekeeping.dao;

import com.mhr.housekeeping.entity.FundDO;
import com.mhr.housekeeping.entity.vo.FundVO;
import com.mhr.housekeeping.entity.vo.OrdersVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 
 * <br/>
 * Created by min on 2019/05/11
 */
@Repository
public interface FundMapper {

    Integer addFund(FundVO fundVO);

    Integer updateFund(FundVO fundVO);

    FundDO findDetailFund(FundVO fundVO);

    List<FundDO> listFund(FundVO fundVO);

    List<FundDO> listFundPage(FundVO fundVO);

    Integer countFund(FundVO fundVO);

    Integer deleteFund(FundVO fundVO);

    List<FundVO> listFundByManager(FundVO fundVO);

    Integer getExtra(FundVO vo);

    Integer deleteFundByOrder(OrdersVO ordersVO);
}
