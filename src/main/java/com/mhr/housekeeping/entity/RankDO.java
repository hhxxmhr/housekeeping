package com.mhr.housekeeping.entity;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/04/26
 */
public class RankDO implements Serializable {
    private static final long serialVersionUID = -8647190691212741974L;

    private Integer id;

    private String name;

    private Integer money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}
