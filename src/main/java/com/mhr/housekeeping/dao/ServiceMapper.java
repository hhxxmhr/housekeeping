package com.mhr.housekeeping.dao;

import com.mhr.housekeeping.entity.ServiceDO;
import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.utils.Result;
import org.springframework.stereotype.Repository;

import java.util.List;

/** 
 * <br/>
 * Created by min on 2019/04/28
 */
@Repository
public interface ServiceMapper {

    Integer addService(ServiceVO serviceVO);

    Integer updateService(ServiceVO serviceVO);

    ServiceDO findDetailService(ServiceVO serviceVO);

    List<ServiceDO> listService(ServiceVO serviceVO);

    List<ServiceDO> listServicePage(ServiceVO serviceVO);

    Integer countService(ServiceVO serviceVO);

    Integer deleteService(ServiceVO serviceVO);

    ServiceVO findServiceByName(ServiceVO serviceVO);

    Integer deleteParService(ServiceVO serviceVO);

    List<ServiceVO> getIds(Integer id);

    List<ServiceDO> listServiceByName(ServiceVO serviceVO);

    List<ServiceDO> findServiceByUserId(Integer id);

    ServiceDO findServiceByOrder(Integer orderId);

    List<ServiceVO> serviceStatic(Integer serviceId, Integer startTime, Integer endTime);
}
