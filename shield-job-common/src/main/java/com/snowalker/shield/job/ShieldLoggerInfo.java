package com.snowalker.shield.job;

import com.snowalker.shield.job.constant.CommonPropertyConst;

import java.io.Serializable;
import java.util.Date;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/18 16:12
 * @className ShieldLoggerInfo
 * @desc shield-info日志实体
 */
public class ShieldLoggerInfo implements Serializable {

    private static final long serialVersionUID = 2929722071947428073L;

    /**调度日志自然主键*/
    private int id;
    /**日志记录主键*/
    private String logId;
    /**服务id*/
    private String serviceId;
    /**执行器地址*/
    private String serviceAddress;
    /**失败重试次数，第几次重试*/
    private String retryTimes;
    /**执行结果码*/
    private String executeCode;
    /**执行结果描述*/
    private String executeDesc;
    /**任务执行日志体*/
    private String executeLoggerMsg;
    /**执行时间*/
    private String handleTime;
    /**
     * 调度结果码 {@link CommonPropertyConst}
     * 调度成功-SUCCESS 调度失败-FAIL 调度中-DEALING 调度初始化-INIT
     */
    private String scheduleCode;
    /**调度日志*/
    private String scheduleLoggerMsg;
    /**
     * 告警状态： {@link CommonPropertyConst}
     * DEFAULT-默认、NONEED-无需告警、SUCCESS-告警成功、FAIL-告警失败
     */
    private String alarmStatus;
    /**任务消息体*/
    private String taskMsgContent;
    /**入库时间*/
    private Date gmtCreate;
    /**修改时间*/
    private Date gmtModified;

    public int getId() {
        return id;
    }

    public ShieldLoggerInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getLogId() {
        return logId;
    }

    public ShieldLoggerInfo setLogId(String logId) {
        this.logId = logId;
        return this;
    }

    public String getServiceId() {
        return serviceId;
    }

    public ShieldLoggerInfo setServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public ShieldLoggerInfo setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
        return this;
    }

    public String getRetryTimes() {
        return retryTimes;
    }

    public ShieldLoggerInfo setRetryTimes(String retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public String getExecuteCode() {
        return executeCode;
    }

    public ShieldLoggerInfo setExecuteCode(String executeCode) {
        this.executeCode = executeCode;
        return this;
    }

    public String getExecuteDesc() {
        return executeDesc;
    }

    public ShieldLoggerInfo setExecuteDesc(String executeDesc) {
        this.executeDesc = executeDesc;
        return this;
    }

    public String getExecuteLoggerMsg() {
        return executeLoggerMsg;
    }

    public ShieldLoggerInfo setExecuteLoggerMsg(String executeLoggerMsg) {
        this.executeLoggerMsg = executeLoggerMsg;
        return this;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public ShieldLoggerInfo setHandleTime(String handleTime) {
        this.handleTime = handleTime;
        return this;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public ShieldLoggerInfo setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
        return this;
    }

    public String getScheduleLoggerMsg() {
        return scheduleLoggerMsg;
    }

    public ShieldLoggerInfo setScheduleLoggerMsg(String scheduleLoggerMsg) {
        this.scheduleLoggerMsg = scheduleLoggerMsg;
        return this;
    }

    public String getAlarmStatus() {
        return alarmStatus;
    }

    public ShieldLoggerInfo setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
        return this;
    }

    public String getTaskMsgContent() {
        return taskMsgContent;
    }

    public ShieldLoggerInfo setTaskMsgContent(String taskMsgContent) {
        this.taskMsgContent = taskMsgContent;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public ShieldLoggerInfo setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public ShieldLoggerInfo setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }

    @Override
    public String toString() {
        return "ShieldLoggerInfo{" +
                "id=" + id +
                ", logId='" + logId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceAddress='" + serviceAddress + '\'' +
                ", retryTimes='" + retryTimes + '\'' +
                ", executeCode='" + executeCode + '\'' +
                ", executeDesc='" + executeDesc + '\'' +
                ", executeLoggerMsg='" + executeLoggerMsg + '\'' +
                ", handleTime='" + handleTime + '\'' +
                ", scheduleCode='" + scheduleCode + '\'' +
                ", scheduleLoggerMsg='" + scheduleLoggerMsg + '\'' +
                ", alarmStatus='" + alarmStatus + '\'' +
                ", taskMsgContent='" + taskMsgContent + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
