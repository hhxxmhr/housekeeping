package com.mhr.housekeeping.entity.vo;

import com.mhr.housekeeping.entity.OrdersDO;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/05/07
 */
public class CommentVO extends Query implements Serializable {
    private static final long serialVersionUID = -6353998539130808163L;
    //根据当前登陆者的id查询其个人拥有的所有的评论
    private Integer eid;

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    private Integer role;

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    //为了封装数据添加的字段
    //评论的订单信息
    private OrdersDO order;
    //评论的雇员、雇主、服务、等级名字
    private String employeeName;
    private String employerName;
    private String serviceName;
    private String rankName;

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public OrdersDO getOrder() {
        return order;
    }

    public void setOrder(OrdersDO order) {
        this.order = order;
    }


    //原本数据库里对应的字段
    private Integer id;

    private Integer orderId;

    private String comment;

    /**
     * 几星
     */
    private Integer rate;

    private String photo;

    private Long createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CommentVO{" +
                "order=" + order +
                ", employeeName='" + employeeName + '\'' +
                ", employerName='" + employerName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", rankName='" + rankName + '\'' +
                ", id=" + id +
                ", orderId=" + orderId +
                ", comment='" + comment + '\'' +
                ", rate=" + rate +
                ", photo='" + photo + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public CommentVO() {
    }

    public CommentVO(Integer id) {
        this.id = id;
    }
}
