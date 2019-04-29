package com.mhr.housekeeping.service.impl;

import com.mhr.housekeeping.dao.ServiceMapper;
import com.mhr.housekeeping.entity.ServiceDO;
import com.mhr.housekeeping.entity.vo.ServiceVO;
import com.mhr.housekeeping.entity.vo.ServiceVO2;
import com.mhr.housekeeping.service.ServiceService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <br/>
 * Created by min on 2019/04/28
 */
@Service("serviceService")
public class ServiceServiceImpl implements ServiceService {

    private final static Logger LOG = LoggerFactory.getLogger(ServiceServiceImpl.class);

    @Resource
    private ServiceMapper serviceMapper;

    @Override
    public Result addService(ServiceVO serviceVO) throws Exception {
        ServiceVO vo = serviceMapper.findServiceByName(serviceVO);
        if (vo != null) {
            return Result.getFailure("该服务名称已存在");
        } else {
            Integer count = serviceMapper.addService(serviceVO);
            if (count > 0) {
                return Result.getSuccess("操作成功");
            }
            return Result.getFailure("操作失败");
        }
    }

    @Override
    public Result updateService(ServiceVO serviceVO) throws Exception {
        Integer count = serviceMapper.updateService(serviceVO);
        if (count > 0) {
            return Result.getSuccess("操作成功");
        }
        return Result.getFailure("操作失败");
    }

    @Override
    public Result findDetailService(ServiceVO serviceVO) throws Exception {
        return null;
    }

    @Override
    public Result listService(ServiceVO serviceVO) throws Exception {
        List<ServiceDO> listService = serviceMapper.listService(serviceVO);
        return new Result<>(listService);
    }

    @Override
    public Result listServicePage(ServiceVO serviceVO) throws Exception {
        return null;
    }

    @Override
    public Result countService(ServiceVO serviceVO) throws Exception {
        return null;
    }

    /**
     * 删除服务的父类   删除的同时还需要删除数据库中parent是此父类的记录
     *
     * @param serviceVO
     * @return
     * @throws Exception
     */
    @Override
    public Result deleteParService(ServiceVO serviceVO) throws Exception {
        Integer count = serviceMapper.deleteParService(serviceVO);
        if (count > 0) {
            return Result.getSuccess("删除成功");
        }
        return Result.getFailure("删除失败");
    }

    @Override
    public Result deleteService(ServiceVO serviceVO) {
        Integer count = serviceMapper.deleteService(serviceVO);
        if (count > 0) {
            return Result.getSuccess("删除成功");
        }
        return Result.getFailure("删除失败");
    }


}