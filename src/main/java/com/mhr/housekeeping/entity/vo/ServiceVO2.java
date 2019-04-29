package com.mhr.housekeeping.entity.vo;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/04/28
 */
public class ServiceVO2 extends Query implements Serializable {
    private static final long serialVersionUID = -8722208901994957538L;

    private Integer id;

    private String name;

    private Integer price;

    private Integer parent;

    private ServiceVO serviceVO;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public ServiceVO getServiceVO() {
        return serviceVO;
    }

    public void setServiceVO(ServiceVO serviceVO) {
        this.serviceVO = serviceVO;
    }
}
