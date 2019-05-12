package com.mhr.housekeeping.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhr.housekeeping.dao.FundMapper;
import com.mhr.housekeeping.dao.UserMapper;
import com.mhr.housekeeping.entity.UserDO;
import com.mhr.housekeeping.entity.vo.FundVO;
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
    private UserMapper userMapper;

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

}