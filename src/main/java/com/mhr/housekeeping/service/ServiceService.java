package com.mhr.housekeeping.service;


import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.utils.Result;

/**
 * <br/>
 * Created by min on 2019/04/28
 */
public interface ServiceService {

	Result addService(ServiceVO serviceVO) throws Exception;

	Result updateService(ServiceVO serviceVO) throws Exception;
	
	Result findDetailService(ServiceVO serviceVO) throws Exception;
	
	Result listService(ServiceVO serviceVO) throws Exception;

    Result listServicePage(ServiceVO serviceVO) throws Exception;
	
	Result countService(ServiceVO serviceVO) throws Exception;
	
	Result deleteParService(ServiceVO serviceVO) throws Exception;

	Result deleteService(ServiceVO serviceVO) throws Exception;

    Result findServiceByUserId(Integer id);
}