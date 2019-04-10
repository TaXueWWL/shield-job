package com.snowalker.shield.job.consumer;

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
 */
public class JobConsumerListenerAdapter implements JobConsumerListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobConsumerListenerAdapter.class);

    private JobConsumerListener jobConsumerListener;

    public JobConsumerListenerAdapter(JobConsumerListener jobConsumerListener) {
        this.jobConsumerListener = jobConsumerListener;
    }

    /**
     * TODO 完善重发逻辑
     * 消费代理
     * @param msgs 消息体
     * @param context
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        ConsumeConcurrentlyStatus consumeConcurrentlyStatus =
                this.jobConsumerListener.consumeMessage(msgs, context);
        if (ConsumeConcurrentlyStatus.RECONSUME_LATER.equals(consumeConcurrentlyStatus)) {
            LOGGER.info("-------------此处应该有重发逻辑---------------");
        }
        MessageExt messageExt = msgs.get(0);
        int reconsumeTimes = messageExt.getReconsumeTimes();
        LOGGER.info("-----------发送次数--{}, msgId={}, msgBody={}",
                reconsumeTimes, messageExt.getMsgId(), new String(messageExt.getBody()));
        return consumeConcurrentlyStatus;
    }
}
