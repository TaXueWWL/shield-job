package com.snowalker.shield.job.consumer.resend;

import com.google.common.base.Preconditions;
import com.snowalker.shield.job.constant.ShieldInnerMsgResendConst;
import com.snowalker.shield.job.consumer.store.impl.MessageStoreRedisTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/12 23:24
 * @className ResendScheduleExecutorServiceConfig
 * @desc 重发线程池配置类
 */
public class MessageResendScheduleExecutorServiceConfig {

    /**最大重复消费次数，达到则提交消息并将消息存储*/
    private volatile Integer maxReconsumeTimes;

    /**nameSrv地址*/
    private String rocketMQNameSrvAddr;

    /**消息存储客户端模板接口，Redis实现*/
    private MessageStoreRedisTemplate messageStoreRedisTemplate;

    /**定时任务初始化延迟时间长度*/
    private long initialDelay = ShieldInnerMsgResendConst.MESSAGE_RESEND_EXECUTOR_SERVICE_INITDELAY;

    /**一次执行终止和下一次执行开始之间的延迟*/
    private long delay = ShieldInnerMsgResendConst.MESSAGE_RESEND_EXECUTOR_SERVICE_DELAY;

    /**执行频率时间单位*/
    private TimeUnit unit = TimeUnit.SECONDS;

    /**重发调度器*/
    private ScheduledExecutorService resendExecutorService;

    public MessageResendScheduleExecutorServiceConfig(Integer maxReconsumeTimes,
                                                String rocketMQNameSrvAddr,
                                                MessageStoreRedisTemplate messageStoreRedisTemplate,
                                                ScheduledExecutorService resendExecutorService,
                                                long initialDelay,
                                                long delay,
                                                TimeUnit unit) {

        Preconditions.checkNotNull(maxReconsumeTimes, "maxReconsumeTimes cannot be NULL!");
        Preconditions.checkNotNull(rocketMQNameSrvAddr, "rocketMQNameSrvAddr cannot be NULL!");
        Preconditions.checkNotNull(messageStoreRedisTemplate, "messageStoreRedisTemplate instance cannot be NULL");

        this.maxReconsumeTimes = maxReconsumeTimes;
        this.rocketMQNameSrvAddr = rocketMQNameSrvAddr;
        this.messageStoreRedisTemplate = messageStoreRedisTemplate;

        this.resendExecutorService = resendExecutorService;
        this.initialDelay = initialDelay;
        this.delay = delay;
        this.unit = unit;
    }

    public MessageResendScheduleExecutorServiceConfig(Integer maxReconsumeTimes,
                                                String rocketMQNameSrvAddr,
                                                MessageStoreRedisTemplate messageStoreRedisTemplate,
                                                ScheduledExecutorService resendExecutorService) {

        Preconditions.checkNotNull(maxReconsumeTimes, "maxReconsumeTimes cannot be NULL!");
        Preconditions.checkNotNull(rocketMQNameSrvAddr, "rocketMQNameSrvAddr cannot be NULL!");
        Preconditions.checkNotNull(messageStoreRedisTemplate, "messageStoreRedisTemplate instance cannot be NULL");

        this.maxReconsumeTimes = maxReconsumeTimes;
        this.rocketMQNameSrvAddr = rocketMQNameSrvAddr;
        this.messageStoreRedisTemplate = messageStoreRedisTemplate;
        this.resendExecutorService = resendExecutorService;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public long getDelay() {
        return delay;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public ScheduledExecutorService getResendExecutorService() {
        return resendExecutorService;
    }

    public Integer getMaxReconsumeTimes() {
        return maxReconsumeTimes;
    }

    public String getRocketMQNameSrvAddr() {
        return rocketMQNameSrvAddr;
    }

    public MessageStoreRedisTemplate getMessageStoreRedisTemplate() {
        return messageStoreRedisTemplate;
    }
}
