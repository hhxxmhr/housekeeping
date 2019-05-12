package com.mhr.housekeeping.entity.vo;

import com.mhr.housekeeping.entity.UserDO;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/05/11
 */
public class FundVO extends Query implements Serializable {
    private static final long serialVersionUID = -6758147211168128691L;

    private String serviceName;
    private String employeeName;
    private String employerName;
    private String employeeUsername;
    private String employerUsername;
    private String rankName;
    private Integer rankMoney;

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

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public String getEmployerUsername() {
        return employerUsername;
    }

    public void setEmployerUsername(String employerUsername) {
        this.employerUsername = employerUsername;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public Integer getRankMoney() {
        return rankMoney;
    }

    public void setRankMoney(Integer rankMoney) {
        this.rankMoney = rankMoney;
    }

    private UserDO userDO;

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }

    private Integer eid;//更多操作  跳转携带的用户id
    private Integer startTime;
    private Integer endTime;
    private Integer page;
    private Integer size;

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

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

    public FundVO(Integer userId, Integer orderId, Integer balance, Integer changeMoney, Long createTime, Integer type) {
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
