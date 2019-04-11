package com.snowalker.shield.job.consumer;

import com.google.common.base.Preconditions;
import com.snowalker.shield.job.constant.ShieldInnerMsgResendConst;
import com.snowalker.shield.job.constant.ShieldJobMsgResendStatusEnum;
import com.snowalker.shield.job.constant.ShieldJobMsgResendStoreTypeEnum;
import com.snowalker.shield.job.consumer.listener.JobConsumerListener;
import com.snowalker.shield.job.consumer.store.JobRetryMessage;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandler;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandlerFactory;
import com.snowalker.shield.job.consumer.store.impl.MessageStoreMySQLTemplate;
import com.snowalker.shield.job.consumer.store.impl.MessageStoreRedisTemplate;
import com.snowalker.shield.job.exception.JobConsumeException;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/9 22:33
 * @className JobConsumerListenerAdapter
 * @desc JobConsumerListener适配器
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
     * 消息存储客户端模板接口，Redis实现
     */
    private MessageStoreRedisTemplate messageStoreRedisTemplate;

    /**
     * 消息存储客户端模板接口，MySQL实现
     */
    private MessageStoreMySQLTemplate messageStoreMySQLTemplate;

    /**
     * 默认构造，只消费不重发
     * @param jobConsumerListener
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener) {
        Preconditions.checkNotNull(jobConsumerListener, "jobConsumerListener cannot be NULL!");
        this.jobConsumerListener = jobConsumerListener;
    }

    /**
     * 使用MessageStoreRedisTemplate的实例作为重发存储机制，设置最大重试次数，超过后入Redis重发
     * @param jobConsumerListener
     * @param maxReconsumeTimes
     * @param rocketMQNameSrvAddr
     * @param messageStoreRedisTemplate
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener,
                                      Integer maxReconsumeTimes,
                                      String rocketMQNameSrvAddr,
                                      MessageStoreRedisTemplate messageStoreRedisTemplate) {
        Preconditions.checkNotNull(jobConsumerListener, "jobConsumerListener cannot be NULL!");
        Preconditions.checkNotNull(maxReconsumeTimes, "maxReconsumeTimes cannot be NULL!");
        Preconditions.checkNotNull(rocketMQNameSrvAddr, "rocketMQNameSrvAddr cannot be NULL!");
        Preconditions.checkNotNull(messageStoreRedisTemplate, "messageStoreRedisTemplate instance cannot be NULL");

        this.jobConsumerListener = jobConsumerListener;
        this.maxReconsumeTimes = maxReconsumeTimes;
        this.rocketMQNameSrvAddr = rocketMQNameSrvAddr;
        this.messageStoreRedisTemplate = messageStoreRedisTemplate;
    }

    /**
     * 使用MessageStoreMySQLTemplate的实例作为重发存储机制，设置最大重试次数，超过后入MySQL重发
     * @param jobConsumerListener
     * @param maxReconsumeTimes
     * @param rocketMQNameSrvAddr
     * @param messageStoreMySQLTemplate
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener,
                                      Integer maxReconsumeTimes,
                                      String rocketMQNameSrvAddr,
                                      MessageStoreMySQLTemplate messageStoreMySQLTemplate) {
        Preconditions.checkNotNull(jobConsumerListener, "jobConsumerListener cannot be NULL!");
        Preconditions.checkNotNull(maxReconsumeTimes, "maxReconsumeTimes cannot be NULL!");
        Preconditions.checkNotNull(rocketMQNameSrvAddr, "rocketMQNameSrvAddr cannot be NULL!");
        Preconditions.checkNotNull(messageStoreMySQLTemplate, "messageStoreMySQLTemplate instance cannot be NULL");

        this.jobConsumerListener = jobConsumerListener;
        this.maxReconsumeTimes = maxReconsumeTimes;
        this.rocketMQNameSrvAddr = rocketMQNameSrvAddr;
        this.messageStoreMySQLTemplate = messageStoreMySQLTemplate;
    }
    /**
     * 消费代理,根据调用方传参决定是否进行重发
     * @param msgs 消息体
     * @param context
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                    ConsumeConcurrentlyContext context) {
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus =
                this.jobConsumerListener.consumeMessage(msgs, context);
        // 默认不传重试次数则不做消息存储重发，只消费
        if (this.maxReconsumeTimes == null) {
            return consumeConcurrentlyStatus;
        }
        // TODO 判断数据库中存储的当前消息的后台线程重提交次数，如果大于最大重发阈值，则不再重发，放入死信队列，直接返回



        // 只要重试阈值不为空则认为要进行消费失败重发，入重发设施
        if (ConsumeConcurrentlyStatus.RECONSUME_LATER == consumeConcurrentlyStatus) {

            for (MessageExt msg : msgs) {
                int currentReconsumeTimes = msg.getReconsumeTimes();
                String msgId = msg.getMsgId();
                String msgTopic = msg.getTopic();
                String msgTag = msg.getTags();
                String msgBody = new String(msg.getBody());
                // 比较当前重发次数与最大重发次数,超过最大重发阈值则提交消息并持久化后重发,幂等最终由消费端控制
                if (currentReconsumeTimes >= maxReconsumeTimes.intValue()) {

                    // 通过调用方传递的messageStoreClientTemplate具体的类型选取对应的JobRetryMessageHandler
                    JobRetryMessageHandlerFactory jobRetryMessageHandlerFactory = new JobRetryMessageHandlerFactory();

                    LOGGER.info("message reconsumeTimes has been grater than maxReconsumeTimes={},commit message and store it. currentReconsumeTimes={}, msgId={}, msgTopic={}",
                            this.maxReconsumeTimes, currentReconsumeTimes, msgId, msgTopic);

                    // todo 转储消息,需要在重发任务调度器中完成标记死信以及定时删除消费成功的消息的功能

                    // 根据构造传入的messageStoreClientTemplate具体实例，选取对应的消息持久化策略
                    if (messageStoreRedisTemplate != null) {
                        LOGGER.info("Using MessageStoreRedisTemplate to store retry message,repository is Redis");
                        msgStoreTypeEnum = ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_REDIS;
                        // 传入messageStoreRedisTemplate引用
                        jobRetryMessageHandlerFactory.setMessageStoreRedisTemplate(messageStoreRedisTemplate);
                    } else if (messageStoreMySQLTemplate != null) {
                        LOGGER.info("Using MessageStoreMySQLTemplate to store retry message,repository is MySQL");
                        msgStoreTypeEnum = ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_MYSQL;
                        // 传入messageStoreMySQLTemplate引用
                        jobRetryMessageHandlerFactory.setMessageStoreMySQLTemplate(messageStoreMySQLTemplate);
                    } else {
                        throw new JobConsumeException("Please input either MessageStoreRedisTemplate or MessageStoreMySQLTemplate instance, for that you can store retry message!");
                    }

                    JobRetryMessageHandler jobRetryMessageHandler =
                            jobRetryMessageHandlerFactory.createRetryMessageHandler(msgStoreTypeEnum);
                    // 构造重发消息持久化实体
                    JobRetryMessage jobRetryMessage = new JobRetryMessage();
                    jobRetryMessage.setMsgId(msgId)
                            .setMsgTopic(msgTopic)
                            .setMsgBody(msgBody)
                            .setMsgTag(msgTag)
                            .setMsgResendStatusEnum(ShieldJobMsgResendStatusEnum.MSG_RESEND_STATUS_RECONSUMELATER.toString())
                            .setMsgRetryProducerGroup(ShieldInnerMsgResendConst.getResendPGVal(msgTopic))
                            .setMsgNameSrvAddr(this.rocketMQNameSrvAddr);
                    jobRetryMessageHandler.storeRetryJobMsg(jobRetryMessage);

                    // 超过最大重复消费次数直接提交消息并持久化消息
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

    public MessageStoreRedisTemplate getMessageStoreRedisTemplate() {
        return messageStoreRedisTemplate;
    }

    public MessageStoreMySQLTemplate getMessageStoreMySQLTemplate() {
        return messageStoreMySQLTemplate;
    }
}
