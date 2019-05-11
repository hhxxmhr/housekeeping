package com.mhr.housekeeping.service.impl;

import com.mhr.housekeeping.dao.FundMapper;
import com.mhr.housekeeping.entity.vo.FundVO;
import com.mhr.housekeeping.service.FundService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/** 
 * <br/>
 * Created by min on 2019/05/11
 */
@Service("fundService")
public class FundServiceImpl implements FundService {

	private final static Logger LOG = LoggerFactory.getLogger(FundServiceImpl.class);

	@Resource
	private FundMapper fundMapper;

	@Override
	public Result addFund(FundVO fundVO) throws Exception {
		return null;
	}

	@Override
	public Result updateFund(FundVO fundVO) throws Exception {
		return null;
	}
	
	@Override
	public Result findDetailFund(FundVO fundVO) throws Exception{
		return null;
	}

	@Override
	public Result listFund(FundVO fundVO) throws Exception{
		return null;
	}
	
	@Override
	public Result listFundPage(FundVO fundVO) throws Exception{
		return null;
	}
	
	@Override
	public Result countFund(FundVO fundVO) throws Exception{
		return null;
	}
	
	@Override
	public Result deleteFund(FundVO fundVO) throws Exception{
		return null;
	}

}