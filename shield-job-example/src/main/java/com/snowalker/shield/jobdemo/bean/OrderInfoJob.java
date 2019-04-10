package com.snowalker.shield.jobdemo.bean;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/9 9:47
 * @className OrderInfoJob
 * @desc 测试订单任务实体
 */
public class OrderInfoJob {

    private String userId;
    private String userName;
    private String orderId;
    private String msgId;
    private String traceId;

    public OrderInfoJob() {
    }

    public OrderInfoJob(String userId, String userName, String orderId, String msgId, String traceId) {
        this.userId = userId;
        this.userName = userName;
        this.orderId = orderId;
        this.msgId = msgId;
        this.traceId = traceId;
    }

    public String getUserId() {
        return userId;
    }

    public OrderInfoJob setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public OrderInfoJob setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderInfoJob setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getMsgId() {
        return msgId;
    }

    public OrderInfoJob setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public OrderInfoJob setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
