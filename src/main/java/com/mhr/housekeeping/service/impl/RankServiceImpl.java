package com.mhr.housekeeping.service.impl;

import com.mhr.housekeeping.dao.RankMapper;
import com.mhr.housekeeping.entity.RankDO;
import com.mhr.housekeeping.entity.vo.RankVO;
import com.mhr.housekeeping.service.RankService;
import com.mhr.housekeeping.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <br/>
 * Created by min on 2019/04/26
 */
@Service("rankService")
public class RankServiceImpl implements RankService {

    private final static Logger LOG = LoggerFactory.getLogger(RankServiceImpl.class);

    @Resource
    private RankMapper rankMapper;

    @Override
    public Result addRank(RankVO rankVO) throws Exception {
        Integer count = rankMapper.addRank(rankVO);
        if (count > 0) {
            return Result.getSuccess("增添成功");
        }
        return Result.getFailure("增添失败");
    }

    @Override
    public Result updateRank(RankVO rankVO) throws Exception {
        Integer count = rankMapper.updateRank(rankVO);
        if (count > 0) {
            return Result.getSuccess("修改成功");
        }
        return Result.getFailure("修改失败");
    }

    @Override
    public Result findDetailRank(RankVO rankVO) throws Exception {
        return null;
    }

    @Override
    public Result listRank(RankVO rankVO) throws Exception {
        List<RankDO> rankList = rankMapper.listRank(rankVO);
        return new Result<>(rankList);
    }

    @Override
    public Result listRankPage(RankVO rankVO) throws Exception {
        return null;
    }

    @Override
    public Result countRank(RankVO rankVO) throws Exception {
        return null;
    }

    @Override
    public Result deleteRank(RankVO rankVO) throws Exception {
        Integer count = rankMapper.deleteRank(rankVO);
        if (count > 0) {
            return Result.getSuccess("删除成功");
        }
        return Result.getFailure("删除失败");
    }

}