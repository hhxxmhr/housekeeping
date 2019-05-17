package com.mhr.housekeeping.entity.vo;

import com.mhr.housekeeping.entity.ServiceDO;

import java.io.Serializable;
import java.util.List;

/**
 * <br/>
 * Created by min on 2019/04/24
 */
public class UserVO extends Query implements Serializable {
    private static final long serialVersionUID = -4944675620658964018L;

    //预留时间--过滤在此时间段人员的状态
    private Long reverseTime;

    public Long getReverseTime() {
        return reverseTime;
    }

    public void setReverseTime(Long reverseTime) {
        this.reverseTime = reverseTime;
    }

    //雇员完成订单有评论数
    private Integer totalComment;
    //雇员完成订单hao评论数
    private Integer goodComment;

    public Integer getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(Integer totalComment) {
        this.totalComment = totalComment;
    }

    public Integer getGoodComment() {
        return goodComment;
    }

    public void setGoodComment(Integer goodComment) {
        this.goodComment = goodComment;
    }

    //雇员完成的订单数
    private Integer orderCount;

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    //雇员的所拥有的服务
    private List<ServiceDO> services;

    public List<ServiceDO> getServices() {
        return services;
    }

    public void setServices(List<ServiceDO> services) {
        this.services = services;
    }

    //所传参数---大小类别id的集合
    private List<Integer> serviceIds;

    public List<Integer> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Integer> serviceIds) {
        this.serviceIds = serviceIds;
    }

    private Integer serviceId;
    private Integer rankId;
    private Integer money;
    private Integer price;
    private Integer parent;
    private String type;//按次结还是月结
    private String rankName;
    private String serviceName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<UserVO> getVoList() {
        return voList;
    }

    public void setVoList(List<UserVO> voList) {
        this.voList = voList;
    }

    private List<UserVO> voList;

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

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
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

    private Integer id;//user的

    private String name;

    private String username;

    private String password;

    private Integer role;

    private Integer state;

    private String sex;

    private Integer age;

    private String phone;

    private String idCard;

    private String bankCard;

    private Long createTime;

    private String photo;

    private String address;

    private String married;

    private Integer education;

    private Integer experience;

    private String introduction;

    private String city;

    private String prov;

    private Integer balance;

    public UserVO(Integer id) {
        this.id = id;
    }

    public UserVO() {
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "serviceId=" + serviceId +
                ", rankId=" + rankId +
                ", money=" + money +
                ", price=" + price +
                ", parent=" + parent +
                ", rankName='" + rankName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", voList=" + voList +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", state=" + state +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", idCard='" + idCard + '\'' +
                ", bankCard='" + bankCard + '\'' +
                ", createTime=" + createTime +
                ", photo='" + photo + '\'' +
                ", address='" + address + '\'' +
                ", married='" + married + '\'' +
                ", education=" + education +
                ", experience=" + experience +
                ", introduction='" + introduction + '\'' +
                ", city='" + city + '\'' +
                ", prov='" + prov + '\'' +
                '}';
    }
}
