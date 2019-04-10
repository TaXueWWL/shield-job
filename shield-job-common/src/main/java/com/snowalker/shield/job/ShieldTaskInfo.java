package com.snowalker.shield.job;

import java.io.Serializable;
import java.util.Date;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/17 16:36
 * @className ShieldTaskInfo
 * @desc Shield-job任务实体
 */
public class ShieldTaskInfo implements Serializable {

    private static final long serialVersionUID = 8514779470421559624L;

    private Integer id;
    /**任务唯一标识，TASK_UUID*/
    private String taskId;
    /**任务名称*/
    private String taskName;
    /**任务详情*/
    private String taskDesc;
    /**任务MQ主题*/
    private String taskMqTopic;
    /**任务状态 INIT-初始化 RUNNING-运行中 PAUSED-已暂停 STOPPED-停止运行*/
    private String taskStatus;
    /**发布状态，INIT-发布初始化 PUBLISHING-发布中
     * PUBLISHED 发布完成 PUBFAIL 发布失败
     * CANCLEING 发布取消 CANCLED 发布已取消
     * DELETED 删除
     * 只有发布完成之后才能取消发布
     */
    private String taskPublishStatus;
    /**调度框架类型，Spring调度-SPRING_SCHEDULE JDK的调度-JDK_EXECUTOR_SCHEDULE*/
    private String taskScheduleFrameType;
    /**任务类型，定时触发/循环-FIXED 一次性任务-ONCE*/
    private String taskType;
    /**是否cron调度，TRUE-使用cron表达式，FALSE-不使用cron表达式*/
    private String isCronEnable;
    /**cron表达式*/
    private String taskCronExpression;
    /**运行模式，单机-SINGLETON 集群-CLUSTERING*/
    private String taskRunPattern;
    /**模块负责人*/
    private String taskOwner;
    /**入库时间*/
    private Date gmtCreate;
    /**更新时间*/
    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public ShieldTaskInfo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public ShieldTaskInfo setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public ShieldTaskInfo setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public ShieldTaskInfo setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
        return this;
    }

    public String getTaskMqTopic() {
        return taskMqTopic;
    }

    public ShieldTaskInfo setTaskMqTopic(String taskMqTopic) {
        this.taskMqTopic = taskMqTopic;
        return this;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public ShieldTaskInfo setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
        return this;
    }

    public String getTaskPublishStatus() {
        return taskPublishStatus;
    }

    public ShieldTaskInfo setTaskPublishStatus(String taskPublishStatus) {
        this.taskPublishStatus = taskPublishStatus;
        return this;
    }

    public String getTaskScheduleFrameType() {
        return taskScheduleFrameType;
    }

    public ShieldTaskInfo setTaskScheduleFrameType(String taskScheduleFrameType) {
        this.taskScheduleFrameType = taskScheduleFrameType;
        return this;
    }

    public String getTaskType() {
        return taskType;
    }

    public ShieldTaskInfo setTaskType(String taskType) {
        this.taskType = taskType;
        return this;
    }

    public String getIsCronEnable() {
        return isCronEnable;
    }

    public ShieldTaskInfo setIsCronEnable(String isCronEnable) {
        this.isCronEnable = isCronEnable;
        return this;
    }

    public String getTaskCronExpression() {
        return taskCronExpression;
    }

    public ShieldTaskInfo setTaskCronExpression(String taskCronExpression) {
        this.taskCronExpression = taskCronExpression;
        return this;
    }

    public String getTaskRunPattern() {
        return taskRunPattern;
    }

    public ShieldTaskInfo setTaskRunPattern(String taskRunPattern) {
        this.taskRunPattern = taskRunPattern;
        return this;
    }

    public String getTaskOwner() {
        return taskOwner;
    }

    public ShieldTaskInfo setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public ShieldTaskInfo setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public ShieldTaskInfo setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }

    @Override
    public String toString() {
        return "ShieldTaskInfo{" +
                "id=" + id +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskDesc='" + taskDesc + '\'' +
                ", taskMqTopic='" + taskMqTopic + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskPublishStatus='" + taskPublishStatus + '\'' +
                ", taskScheduleFrameType='" + taskScheduleFrameType + '\'' +
                ", taskType='" + taskType + '\'' +
                ", isCronEnable='" + isCronEnable + '\'' +
                ", taskCronExpression='" + taskCronExpression + '\'' +
                ", taskRunPattern='" + taskRunPattern + '\'' +
                ", taskOwner='" + taskOwner + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
