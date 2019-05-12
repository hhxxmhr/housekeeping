package com.mhr.housekeeping.entity.vo;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/05/11
 */
public class FundVO extends Query implements Serializable {
    private static final long serialVersionUID = -6758147211168128691L;

    private Integer id;

    private Integer userId;

    private Integer orderId;

    /**
     * 余额
     */
    private Integer balance;

    /**
     * 每次变化的金额
     */
    private Integer changeMoney;

    private Long createTime;

    /**
     * 0：雇主充值  1：雇主服务支出  2：雇主违时取消  3：雇主退款  4：员工收入 5：员工退款
     */
    private Integer type;

    public FundVO( Integer userId, Integer orderId, Integer balance, Integer changeMoney, Long createTime, Integer type) {
        this.userId = userId;
        this.orderId = orderId;
        this.balance = balance;
        this.changeMoney = changeMoney;
        this.createTime = createTime;
        this.type = type;
    }

    public FundVO() {
    }

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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(Integer changeMoney) {
        this.changeMoney = changeMoney;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
