package com.mhr.housekeeping.entity.vo;


import java.io.Serializable;

/**
 * <br/>
 * Created by min on 2019/04/28
 */
public class ServiceVO extends Query implements Serializable {
    private static final long serialVersionUID = -8722208901994957538L;

    private Integer employeeId;
    private Integer employerId;
    private Integer orderPrice;
    private Integer rate;
    private String employerName;
    private String employerUsername;
    private String employeeName;
    private String employeeUsername;
    private Long createTime;//订单的创建时间

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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
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

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    private Integer id;

    private String name;

    private Integer price;

    private Integer parent;

    /**
     * 月结还是次结
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public ServiceVO(Integer id) {
        this.id = id;
    }

    public ServiceVO() {
    }

    @Override
    public String toString() {
        return "ServiceVO{" +
                "employeeId=" + employeeId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", parent=" + parent +
                '}';
    }
}
