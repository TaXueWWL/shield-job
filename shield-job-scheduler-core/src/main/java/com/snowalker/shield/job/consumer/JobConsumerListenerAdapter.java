package com.snowalker.shield.job.consumer;

import com.google.common.base.Preconditions;
import com.snowalker.shield.job.constant.ShieldInnerMsgResendConst;
import com.snowalker.shield.job.constant.ShieldJobMsgResendStatusEnum;
import com.snowalker.shield.job.constant.ShieldJobMsgResendStoreTypeEnum;
import com.snowalker.shield.job.consumer.listener.JobConsumerListener;
import com.snowalker.shield.job.consumer.store.JobRetryMessage;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandler;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandlerFactory;
import com.snowalker.shield.job.consumer.store.MessageStoreClientTemplate;
import com.snowalker.shield.job.exception.JobProduceException;
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
    private String rocketMQNameSrvAddr;

    /**
     * 消息存储客户端模板接口，实现类：
     * @see com.snowalker.shield.job.consumer.store.impl.MessageStoreMySQLTemplate
     * @see com.snowalker.shield.job.consumer.store.impl.MessageStoreRedisTemplate
     */
    private MessageStoreClientTemplate messageStoreClientTemplate;

    /**
     * 默认构造，只消费不重发
     * @param jobConsumerListener
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener) {
        Preconditions.checkNotNull(jobConsumerListener, "jobConsumerListener cannot be NULL!");
        this.jobConsumerListener = jobConsumerListener;
    }

    /**
     * 使用messageStoreClientTemplate的实例作为重发存储机制，设置最大重试次数，超过后入对应的持久化设施重发
     * @param jobConsumerListener
     * @param maxReconsumeTimes
     * @param rocketMQNameSrvAddr
     * @param messageStoreClientTemplate
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener,
                                      Integer maxReconsumeTimes,
                                      String rocketMQNameSrvAddr,
                                      MessageStoreClientTemplate messageStoreClientTemplate) {
        Preconditions.checkNotNull(jobConsumerListener, "jobConsumerListener cannot be NULL!");
        Preconditions.checkNotNull(maxReconsumeTimes, "maxReconsumeTimes cannot be NULL!");
        Preconditions.checkNotNull(rocketMQNameSrvAddr, "rocketMQNameSrvAddr cannot be NULL!");
        Preconditions.checkNotNull(messageStoreClientTemplate, "messageStoreClientTemplate instance cannot be NULL");

        this.jobConsumerListener = jobConsumerListener;
        this.maxReconsumeTimes = maxReconsumeTimes;
        this.rocketMQNameSrvAddr = rocketMQNameSrvAddr;
        this.messageStoreClientTemplate = messageStoreClientTemplate;
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
        Preconditions.checkNotNull(this.jobConsumerListener, "jobConsumerListener cannot be NULL!");
        /**
         * 默认不传重试次数则表明
         */
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus =
                this.jobConsumerListener.consumeMessage(msgs, context);
        if (this.maxReconsumeTimes == null) {
            return consumeConcurrentlyStatus;
        }
        // 只要重试阈值不为空则认为要进行消费失败重发，入重发设施
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
                    if (messageStoreClientTemplate instanceof RedisTemplate) {
                        msgStoreTypeEnum = ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_REDIS;
                    } else if (messageStoreClientTemplate instanceof JdbcTemplate) {
                        msgStoreTypeEnum = ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_MYSQL;
                    } else {
                        throw new JobProduceException("messageStoreClientTemplate can only be instance of RedisTemplate or JdbcTemplate, please choose a right one!");
                    }

                    JobRetryMessageHandler jobRetryMessageHandler =
                            JobRetryMessageHandlerFactory.createRetryMessageHandler(msgStoreTypeEnum);
                    JobRetryMessage jobRetryMessage = new JobRetryMessage();
                    jobRetryMessage.setMsgId(msgId)
                            .setMsgTopic(msgTopic)
                            .setMsgBody(msgBody)
                            .setMsgResendStatusEnum(ShieldJobMsgResendStatusEnum.MSG_RESEND_STATUS_RECONSUMELATER)
                            .setMsgRetryProducerGroup(ShieldInnerMsgResendConst.getResendPGVal(msgTopic))
                            .setMsgNameSrvAddr(this.rocketMQNameSrvAddr);
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

    public ShieldJobMsgResendStoreTypeEnum getMsgStoreTypeEnum() {
        return msgStoreTypeEnum;
    }

    public String getRocketMQNameSrvAddr() {
        return rocketMQNameSrvAddr;
    }

    public MessageStoreClientTemplate getMessageStoreClientTemplate() {
        return messageStoreClientTemplate;
    }

}
