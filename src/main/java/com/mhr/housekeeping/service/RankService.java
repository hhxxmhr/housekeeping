package com.mhr.housekeeping.service;

import com.mhr.housekeeping.entity.RankDO;
import com.mhr.housekeeping.entity.vo.RankVO;
import com.mhr.housekeeping.utils.Result;

/**
 * <br/>
 * Created by min on 2019/04/26
 */
public interface RankService {

	Result addRank(RankVO rankVO) throws Exception;

	Result updateRank(RankVO rankVO) throws Exception;
	
	Result findDetailRank(RankVO rankVO) throws Exception;
	
	Result listRank(RankVO rankVO) throws Exception;

    Result listRankPage(RankVO rankVO) throws Exception;
	
	Result countRank(RankVO rankVO) throws Exception;
	
	Result deleteRank(RankVO rankVO) throws Exception;

    Result findRankByUserId(Integer id);

	Result findRankByUidAndSid(Integer userId, Integer serviceId);

    RankDO getMostRank(Integer id);
}