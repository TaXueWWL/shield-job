package com.snowalker.shield.job;

import com.snowalker.shield.job.consumer.RocketMQConsumerProperty;
import com.snowalker.shield.job.consumer.JobConsumerListenerAdapter;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 15:49
 * @className JobConsumer
 * @desc Job消费者接口
 */
public interface JobConsumer {

    /**
     * 消费任务消息
     * @param rocketMQConsumerProperty
     * @param jobConsumerListenerAdapter
     */
    JobConsumer execute(RocketMQConsumerProperty rocketMQConsumerProperty, JobConsumerListenerAdapter jobConsumerListenerAdapter) throws MQClientException;
}
