package com.snowalker.shield.job.consumer.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 15:54
 * @className JobConsumerListener
 * @desc 任务消费监听器接口
 */
public interface JobConsumerListener extends MessageListenerConcurrently {

    /**
     * 消息消费
     * @param msgs 消息体
     * @param context
     * @return 消费状态
     */
    @Override
    ConsumeConcurrentlyStatus consumeMessage(final List<MessageExt> msgs,
                                             final ConsumeConcurrentlyContext context);
}
