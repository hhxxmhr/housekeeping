package com.mhr.housekeeping.entity.vo;

import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/05/04
 */
public class OrdersVO extends Query implements Serializable {
    private static final long serialVersionUID = -5184197486620321135L;

    private Integer eid;//用来传递参数的id，表示员工的id，或者雇主的id

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    private Integer page;//当前页
    private Integer size;//页面数据数量

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

    private Integer role;
    private Integer userId;
    private String employerName;
    private String employerPhone;
    private String employerUsername;
    private String employeeName;
    private String employeePhone;
    private String employeeUsername;
    private String serviceName;
    private Integer servicePrice;
    private Integer serviceParent;
    private String realName;
    private String username;
    private String password;
    private String sex;
    private Integer age;
    private Integer education;
    private Integer experience;
    private Integer rankId;
    private String rankName;
    private Integer rankMoney;

    public String getEmployerPhone() {
        return employerPhone;
    }

    public void setEmployerPhone(String employerPhone) {
        this.employerPhone = employerPhone;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployerUsername() {
        return employerUsername;
    }

    public void setEmployerUsername(String employerUsername) {
        this.employerUsername = employerUsername;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(Integer servicePrice) {
        this.servicePrice = servicePrice;
    }

    public Integer getServiceParent() {
        return serviceParent;
    }

    public void setServiceParent(Integer serviceParent) {
        this.serviceParent = serviceParent;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getRankId() {
        return rankId;
    }

    public void setRankId(Integer rankId) {
        this.rankId = rankId;
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    private Integer id;

    private Integer serviceId;

    private Integer employeeId;

    /**
     * 预留上门时间
     */
    private Long reverseTime;

    private String address;

    private String phone;

    private String tip;

    private Integer state;

    private Integer orderPrice;

    private Integer employerId;

    private Long createTime;

    /**
     * 订单完成时间
     */
    private Long endTime;

    /**
     * 评分
     */
    private Integer rate;

    private String prov;

    private String city;

    /**
     * 超时取消费
     */
    private Integer pay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Long getReverseTime() {
        return reverseTime;
    }

    public void setReverseTime(Long reverseTime) {
        this.reverseTime = reverseTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    @Override
    public String toString() {
        return "OrdersVO{" +
                "eid=" + eid +
                ", role=" + role +
                ", userId=" + userId +
                ", employerName='" + employerName + '\'' +
                ", employerUsername='" + employerUsername + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeUsername='" + employeeUsername + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", servicePrice=" + servicePrice +
                ", serviceParent=" + serviceParent +
                ", realName='" + realName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", education=" + education +
                ", experience=" + experience +
                ", rankId=" + rankId +
                ", rankName='" + rankName + '\'' +
                ", rankMoney=" + rankMoney +
                ", id=" + id +
                ", serviceId=" + serviceId +
                ", employeeId=" + employeeId +
                ", reverseTime=" + reverseTime +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", tip='" + tip + '\'' +
                ", state=" + state +
                ", orderPrice=" + orderPrice +
                ", employerId=" + employerId +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                ", rate=" + rate +
                ", prov='" + prov + '\'' +
                ", city='" + city + '\'' +
                ", pay=" + pay +
                '}';
    }
}
