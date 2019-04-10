package com.snowalker.shield.job.consumer;

import com.google.common.base.Preconditions;
import com.snowalker.shield.job.constant.ShieldInnerMsgResendConst;
import com.snowalker.shield.job.constant.ShieldJobMsgResendStatusEnum;
import com.snowalker.shield.job.constant.ShieldJobMsgResendStoreTypeEnum;
import com.snowalker.shield.job.consumer.listener.JobConsumerListener;
import com.snowalker.shield.job.consumer.store.JobRetryMessage;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandler;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandlerFactory;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/9 22:33
 * @className JobConsumerListenerAdapter
 * @desc JobConsumerListener适配器
 * TODO JDBCTemplate及RedisTemplate引用注入
 */
public class JobConsumerListenerAdapter implements JobConsumerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobConsumerListenerAdapter.class);

    private JobConsumerListener jobConsumerListener;

    /**消息存储枚举*/
    private ShieldJobMsgResendStoreTypeEnum msgStoreTypeEnum;

    /**最大重复消费次数，达到则提交消息并将消息存储*/
    private volatile Integer maxReconsumeTimes;

    /**nameSrv地址*/
    private String nameSrvAddr;

    /**RedisTemplate*/
    private RedisTemplate redisTemplate;

    /**JdbcTemplate*/
    private JdbcTemplate jdbcTemplate;

    /**
     * 默认构造，只消费不重发
     * @param jobConsumerListener
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener) {
        Preconditions.checkNotNull(jobConsumerListener, "jobConsumerListener cannot be NULL!");
        this.jobConsumerListener = jobConsumerListener;
    }

    /**
     * 使用Redis重发存储机制，设置最大重试次数，超过后入Redis重发
     * @param jobConsumerListener
     * @param maxReconsumeTimes
     * @param msgStoreTypeEnum
     * @param rocketMQNameSrvAddr
     * @param redisTemplate
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener,
                                      Integer maxReconsumeTimes,
                                      ShieldJobMsgResendStoreTypeEnum msgStoreTypeEnum,
                                      String rocketMQNameSrvAddr,
                                      RedisTemplate redisTemplate) {
        Preconditions.checkNotNull(jobConsumerListener, "jobConsumerListener cannot be NULL!");
        Preconditions.checkNotNull(maxReconsumeTimes, "maxReconsumeTimes cannot be NULL!");
        Preconditions.checkNotNull(msgStoreTypeEnum, "msgStoreTypeEnum cannot be NULL!");
        Preconditions.checkNotNull(rocketMQNameSrvAddr, "rocketMQNameSrvAddr cannot be NULL!");
        Preconditions.checkNotNull(redisTemplate, "redisTemplate cannot be NULL");
        this.jobConsumerListener = jobConsumerListener;
        this.maxReconsumeTimes = maxReconsumeTimes;
        this.msgStoreTypeEnum  = msgStoreTypeEnum;
        this.nameSrvAddr = rocketMQNameSrvAddr;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 使用MySQL重发存储机制，设置最大重试次数，超过后入MySQL重发
     * @param jobConsumerListener
     * @param maxReconsumeTimes
     * @param msgStoreTypeEnum
     * @param rocketMQNameSrvAddr
     * @param jdbcTemplate
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener,
                                      Integer maxReconsumeTimes,
                                      ShieldJobMsgResendStoreTypeEnum msgStoreTypeEnum,
                                      String rocketMQNameSrvAddr,
                                      JdbcTemplate jdbcTemplate) {
        Preconditions.checkNotNull(jobConsumerListener, "jobConsumerListener cannot be NULL!");
        Preconditions.checkNotNull(maxReconsumeTimes, "maxReconsumeTimes cannot be NULL!");
        Preconditions.checkNotNull(msgStoreTypeEnum, "msgStoreTypeEnum cannot be NULL!");
        Preconditions.checkNotNull(rocketMQNameSrvAddr, "rocketMQNameSrvAddr cannot be NULL!");
        Preconditions.checkNotNull(jdbcTemplate, "jdbcTemplate cannot be NULL");
        this.jobConsumerListener = jobConsumerListener;
        this.maxReconsumeTimes = maxReconsumeTimes;
        this.msgStoreTypeEnum  = msgStoreTypeEnum;
        this.nameSrvAddr = rocketMQNameSrvAddr;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * TODO 完善重发逻辑
     * 消费代理, 不需要重发
     * @param msgs 消息体
     * @param context
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                    ConsumeConcurrentlyContext context) {
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus =
                this.jobConsumerListener.consumeMessage(msgs, context);
        if (this.maxReconsumeTimes == null) {
            return consumeConcurrentlyStatus;
        }
        if (ConsumeConcurrentlyStatus.RECONSUME_LATER == consumeConcurrentlyStatus) {
            for (MessageExt msg : msgs) {
                int currentReconsumeTimes = msg.getReconsumeTimes();
                String msgId = msg.getMsgId();
                String msgTopic = msg.getTopic();
                String msgBody = new String(msg.getBody());
                // 比较当前重发次数与最大重发次数,超过最大重发阈值则提交消息并持久化后重发,幂等最终由消费端控制
                if (currentReconsumeTimes >= maxReconsumeTimes.intValue()) {
                    LOGGER.info("message reconsumeTimes has been grater than maxReconsumeTimes={},commit message and store it. currentReconsumeTimes={}, msgId={}, msgTopic={}",
                            this.maxReconsumeTimes, currentReconsumeTimes, msgId, msgTopic);

                    // todo 转储消息,需要在重发任务调度器中完成标记死信以及定时删除消费成功的消息的功能
                    Preconditions.checkNotNull(msgStoreTypeEnum, "msgStoreTypeEnum cannot be NULL!");
                    JobRetryMessageHandler jobRetryMessageHandler =
                            JobRetryMessageHandlerFactory.createRetryMessageHandler(msgStoreTypeEnum);
                    JobRetryMessage jobRetryMessage = new JobRetryMessage();
                    jobRetryMessage.setMsgId(msgId)
                            .setMsgTopic(msgTopic)
                            .setMsgBody(msgBody)
                            .setMsgResendStatusEnum(ShieldJobMsgResendStatusEnum.MSG_RESEND_STATUS_RECONSUMELATER)
                            .setMsgRetryProducerGroup(ShieldInnerMsgResendConst.getResendPGVal(msgTopic))
                            .setMsgNameSrvAddr(this.nameSrvAddr);
                    jobRetryMessageHandler.storeRetryJobMsg(jobRetryMessage);

                    // 转储次数增1
                    jobRetryMessageHandler.countRetryJobMsgResendTimes(msgId, msgTopic);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
        }
        return consumeConcurrentlyStatus;
    }

    public int getMaxReconsumeTimes() {
        return maxReconsumeTimes;
    }

    public JobConsumerListenerAdapter setMaxReconsumeTimes(int maxReconsumeTimes) {
        this.maxReconsumeTimes = maxReconsumeTimes;
        return this;
    }

    public ShieldJobMsgResendStoreTypeEnum getMsgStoreTypeEnum() {
        return msgStoreTypeEnum;
    }

    public JobConsumerListenerAdapter setMsgStoreTypeEnum(ShieldJobMsgResendStoreTypeEnum msgStoreTypeEnum) {
        this.msgStoreTypeEnum = msgStoreTypeEnum;
        return this;
    }

    public String getNameSrvAddr() {
        return nameSrvAddr;
    }

    public JobConsumerListenerAdapter setNameSrvAddr(String nameSrvAddr) {
        this.nameSrvAddr = nameSrvAddr;
        return this;
    }

    public JobConsumerListenerAdapter setMaxReconsumeTimes(Integer maxReconsumeTimes) {
        this.maxReconsumeTimes = maxReconsumeTimes;
        return this;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public JobConsumerListenerAdapter setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        return this;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public JobConsumerListenerAdapter setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        return this;
    }
}
