package com.mhr.housekeeping.entity;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/04/28
 */
public class UserServiceDO implements Serializable {
    private static final long serialVersionUID = -5486159274454910379L;

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
