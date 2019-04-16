package com.snowalker.shield.job.mapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/2/15 16:46
 * @className OrderEntity
 * @desc 模拟订单数据库实体
 */
public class OrderEntity {

    private int id;
    private String orderId;
    /**支付状态 1 初始化 2 处理中 3 失败 0 成功*/
    private int orderStatus;
    /**支付状态 1 初始化 2 处理中 3 失败 0 成功*/
    private int payStatus;
    /**退款状态 -1 不需要退款 1 退款失败 0 退款成功*/
    private int refundStaus;
    private String userName;
    private BigDecimal chargeMoney;
    /**钱包id*/
    private String purseId;
    private Date gmtCreate;
    private Date gmtUpdate;

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public OrderEntity setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
        return this;
    }

    public String getPurseId() {
        return purseId;
    }

    public OrderEntity setPurseId(String purseId) {
        this.purseId = purseId;
        return this;
    }

    public int getId() {
        return id;
    }

    public OrderEntity setId(int id) {
        this.id = id;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderEntity setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public OrderEntity setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public OrderEntity setPayStatus(int payStatus) {
        this.payStatus = payStatus;
        return this;
    }

    public int getRefundStaus() {
        return refundStaus;
    }

    public OrderEntity setRefundStaus(int refundStaus) {
        this.refundStaus = refundStaus;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public OrderEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public OrderEntity setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public OrderEntity setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
        return this;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", orderStatus=" + orderStatus +
                ", payStatus=" + payStatus +
                ", refundStaus=" + refundStaus +
                ", userName='" + userName + '\'' +
                ", chargeMoney='" + chargeMoney + '\'' +
                ", purseId='" + purseId + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtUpdate=" + gmtUpdate +
                '}';
    }
}
