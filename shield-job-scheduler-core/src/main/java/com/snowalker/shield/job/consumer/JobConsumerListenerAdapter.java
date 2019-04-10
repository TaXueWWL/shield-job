package com.snowalker.shield.job.consumer;

import com.snowalker.shield.job.constant.ShieldJobMsgResendStoreTypeEnum;
import com.snowalker.shield.job.consumer.listener.JobConsumerListener;
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
 * TODO JDBCTemplate及RedisTemplate引用注入
 */
public class JobConsumerListenerAdapter implements JobConsumerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobConsumerListenerAdapter.class);

    private JobConsumerListener jobConsumerListener;

    /**消息存储枚举*/
    private ShieldJobMsgResendStoreTypeEnum msgStoreTypeEnum;

    /**最大重复消费次数，达到则提交消息并将消息存储*/
    private volatile Integer maxReconsumeTimes;

    /**TODO nameSrv地址，需要摄入*/
    private String nameSrvAddr;

    /**
     * 默认构造，只消费不重发
     * @param jobConsumerListener
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener) {
        this.jobConsumerListener = jobConsumerListener;
    }

    /**
     * 使用Redis作为重发存储机制，设置最大重试次数，超过后入Redis重发
     * @param jobConsumerListener
     * @param maxReconsumeTimes
     * @param msgStoreTypeEnum
     */
    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener,
                                      Integer maxReconsumeTimes,
                                      ShieldJobMsgResendStoreTypeEnum msgStoreTypeEnum) {
        this.jobConsumerListener = jobConsumerListener;
        this.maxReconsumeTimes = maxReconsumeTimes;
        this.msgStoreTypeEnum  = msgStoreTypeEnum;
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
                // 比较当前重发次数与最大重发次数,超过最大重发阈值则提交消息并持久化后重发,幂等最终由消费端控制
                if (currentReconsumeTimes >= maxReconsumeTimes.intValue()) {
                    LOGGER.info("message reconsumeTimes has been grater than maxReconsumeTimes={},commit message and store it. currentReconsumeTimes={}, msgId={}, msgTopic={}",
                            this.maxReconsumeTimes, currentReconsumeTimes, msgId, msgTopic);
                    // todo 转储消息
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
}
