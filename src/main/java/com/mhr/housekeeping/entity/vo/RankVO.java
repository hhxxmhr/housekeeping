package com.mhr.housekeeping.entity.vo;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/04/26
 */
public class RankVO extends Query implements Serializable {
    private static final long serialVersionUID = -8092369425016442791L;

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
