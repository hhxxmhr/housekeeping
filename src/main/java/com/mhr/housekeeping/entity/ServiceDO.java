package com.mhr.housekeeping.entity;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/04/28
 */
public class ServiceDO implements Serializable {
    private static final long serialVersionUID = -5376206537373734931L;

    private Integer id;

    private String name;

    private Integer price;

    private Integer parent;

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

    @Override
    public String toString() {
        return "ServiceDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", parent=" + parent +
                '}';
    }
}
