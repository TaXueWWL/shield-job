package com.snowalker.shield.job.consumer.resend.impl;

import com.snowalker.shield.job.constant.ShieldInnerMsgResendConst;
import com.snowalker.shield.job.consumer.resend.MessageResendRedisStoreContext;
import com.snowalker.shield.job.consumer.resend.MessageResendScheduler;
import com.snowalker.shield.job.consumer.store.JobRetryMessage;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandler;
import com.snowalker.shield.job.exception.JobConsumeException;
import com.snowalker.shield.job.producer.RocketMQProducerProperty;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/11 17:30
 * @className MessageResendRedisExecutor
 * @desc 消息重投递Redis执行器
 * TODO 消息投递定时调度 判断存储的当前消息的后台线程重提交次数，如果大于最大重发阈值，则不再重发，放入死信队列，直接返回
 *
 * 基于Redis的重发：
 *      1. 不需要对重发的数据做清理操作，因为一旦弹出队列就不存在了，只需要在达到重发的最大阈值后入死信即可
 *      2. 不需要标记死信，原因同上，入死信的就是死信
 * 基于MySQL的重发：
 *      1. 需要清理重发的数据，起线程将重发表中的数据清理，同时转移到死信表中（同一本地事务）
 *      2. 不需要标记死信，原因同上，入死信表的就是死信
 */
public class MessageResendRedisSchedulerImpl implements MessageResendScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageResendRedisSchedulerImpl.class);

    /**重发调度器*/
    private ScheduledExecutorService resendExecutorService;

    /**RedisTemplate引用*/
    private RedisTemplate redisTemplate;

    /**定时任务初始化延迟时间长度*/
    private long initialDelay = ShieldInnerMsgResendConst.MESSAGE_RESEND_EXECUTOR_SERVICE_INITDELAY;;

    /**一次执行终止和下一次执行开始之间的延迟*/
    private long delay = ShieldInnerMsgResendConst.MESSAGE_RESEND_EXECUTOR_SERVICE_DELAY;

    /**执行频率时间单位*/
    private TimeUnit unit = TimeUnit.SECONDS;

    /**重发消息处理器*/
    private JobRetryMessageHandler jobRetryMessageHandler;

    /**
     * RocketMQ普通消息发送引用
     */
    private DefaultMQProducer defaultMQProducer;

    public MessageResendRedisSchedulerImpl(RocketMQProducerProperty rocketMQProducerProperty,
                                      RedisTemplate redisTemplate,
                                      ScheduledExecutorService resendExecutorService,
                                      long initialDelay,
                                      long delay,
                                      TimeUnit unit) {
        if (resendExecutorService == null) {
            this.resendExecutorService =
                    Executors.newScheduledThreadPool(ShieldInnerMsgResendConst.REDIS_MESSAGE_RESEND_CORE_POOLSIZE);
        } else {
            this.resendExecutorService = resendExecutorService;
            this.delay = delay;
            this.initialDelay = initialDelay;
            this.unit = unit;
        }
        // 设置RedisTemplate实例
        this.redisTemplate = redisTemplate;
        // 初始化重发器JobResendProducerExecutor
        this.init(rocketMQProducerProperty);
        // 开启生产
        try {
            this.start();
        } catch (MQClientException e) {
            LOGGER.error("Start RocketMQ defaultMQProducer occurred Exception!", e);
            throw new JobConsumeException("Start RocketMQ defaultMQProducer occurred Exception!");
        }
        LOGGER.info("[MessageResendRedisSchedulerImpl] start successfully, resendExecutorService={}", resendExecutorService.getClass());
    }

    /**
     * 初始化执行器
     * @param rocketMQProducerProperty
     * @return
     */
    public MessageResendRedisSchedulerImpl init(RocketMQProducerProperty rocketMQProducerProperty) {
        String nameSrvAddr = rocketMQProducerProperty.getNameSrvAddr();
        String producerGroup = rocketMQProducerProperty.getProducerGroup();
        // 初始化生产者
        if (this.defaultMQProducer == null) {
            this.defaultMQProducer = new DefaultMQProducer(producerGroup);
        }
        this.defaultMQProducer.setNamesrvAddr(nameSrvAddr);
        LOGGER.info("[MessageResendRedisSchedulerImpl] init successfully, jobProducerGroup={}", producerGroup);
        return this;
    }

    /**
     * 执行重发逻辑
     * 同一时间只有一个线程执行取队列重发
     */
    @Override
    public void doResend() {
        LOGGER.debug("Starting retry message resend sequence......");
        this.resendExecutorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        execute();
                    }
                }, initialDelay, delay, unit
        );
    }

    /**
     * 核心重发业务逻辑
     * 使用线程同步
     */
    private void execute() {
        // 获取重发队列名
        List<String> resendRedisQueueNames = MessageResendRedisStoreContext.getRedisRetryMsgQueueList();
        synchronized (Object.class) {
            resendRedisQueueNames.stream().forEach((String queueName) -> {
                String retryMessageId = null;
                String retryMessageTopic = null;
                String retryMesssageBody = null;
                String retryMessageTag = null;
                try {// 迭代每个重发队列，按照放入的顺序进行重发。队列：左入右出
                    RedisTemplate redisTemplate = this.getRedisTemplate();

                    // 取队列中的元素, 超时默认10s, 转换为重发实体
                    String retryMessageVal = (String) redisTemplate.opsForList().rightPop(queueName,
                            ShieldInnerMsgResendConst.MESSAGE_RESEND_REDIS_POP_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    JobRetryMessage jobRetryMessage = new JobRetryMessage();
                    jobRetryMessage.decode(retryMessageVal);
                    // 获取重发消息体组装Message实体
                    retryMessageId = jobRetryMessage.getMsgId();
                    retryMessageTopic = jobRetryMessage.getMsgTopic();
                    retryMesssageBody = jobRetryMessage.getMsgBody();
                    retryMessageTag = jobRetryMessage.getMsgTag();

                    // 比较当前重发次数和最大重发次数
                    int currentResendTimes = (Integer) redisTemplate.opsForValue()
                            .get(ShieldInnerMsgResendConst.getResendTimesRedisKey(retryMessageId));
                    if (currentResendTimes >= ShieldInnerMsgResendConst.MAX_RESEND_TIMES) {
                        // 超过最大重发次数，直接入死信队列
                        this.jobRetryMessageHandler.storeRetryJobMsgIntoDeadQueue(jobRetryMessage);
                        LOGGER.warn("message has been grater than maxResendTimes,[currentResendTimes]={}, store to Dead-Message-Queue, msgId={}, topic={}", currentResendTimes, retryMessageId, retryMessageTopic);
                        return;
                    }
                    // 否则进行重发
                    Message message = new Message(retryMessageTopic, retryMessageTag, retryMesssageBody.getBytes());
                    this.getProducer().send(message, ShieldInnerMsgResendConst.MESSAGE_RESEND_MQ_TIMEOUT_SECONDS);
                    // 重发次数加1
                    redisTemplate.opsForValue()
                            .increment(ShieldInnerMsgResendConst.getResendTimesRedisKey(retryMessageId));
                } catch (Exception e) {

                    LOGGER.error("Resend message occurred Exception, topic={}, msgId={}, messageBody={}",
                            retryMessageTopic, retryMessageId, retryMesssageBody);
                    e.printStackTrace();
                }
            });
        }
    }


    /**
     * 启动生产者客户端
     * @throws MQClientException
     */
    public void start() throws MQClientException {
        this.defaultMQProducer.start();
    }

    /**
     * 关闭客户端
     */
    public void shutdown() {
        this.defaultMQProducer.shutdown();
        LOGGER.info("com.snowalker.shield.job.consumer.resend.JobResendProducerExecutor.defaultMQProducer has been shutdown");
    }

    public DefaultMQProducer getProducer() {
        return defaultMQProducer;
    }

    @Override
    public ScheduledExecutorService getResendExecutorService() {
        return resendExecutorService;
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

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public MessageResendRedisSchedulerImpl setJobRetryMessageHandler(JobRetryMessageHandler jobRetryMessageHandler) {
        this.jobRetryMessageHandler = jobRetryMessageHandler;
        return this;
    }

    public JobRetryMessageHandler getJobRetryMessageHandler() {
        return jobRetryMessageHandler;
    }
}
