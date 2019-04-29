package com.mhr.housekeeping.entity.vo;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/04/28
 */
public class UserServiceVO extends Query implements Serializable {
    private static final long serialVersionUID = -8905089075254025653L;

    private Integer id;

    private Integer userId;

    private Integer serviceId;

    private Integer rankId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getRankId() {
        return rankId;
    }

    public void setRankId(Integer rankId) {
        this.rankId = rankId;
    }
}
