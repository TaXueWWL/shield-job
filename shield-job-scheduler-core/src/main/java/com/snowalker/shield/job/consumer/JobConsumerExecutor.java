package com.snowalker.shield.job.consumer;

import com.google.common.base.Preconditions;
import com.snowalker.shield.job.JobConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 15:47
 * @className JobConsumerExecuter
 * @desc Job消费者执行器
 */
public class JobConsumerExecutor implements JobConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobConsumerExecutor.class);

    private DefaultMQPushConsumer defaultMQPushConsumer;

    /**
     * 初始化DefaultMQPushConsumer, 调用JobConsumerListener进行任务的消费及调度
     * @param rocketMQConsumerProperty
     * @param jobConsumerListenerAdapter
     * @throws MQClientException
     */
    @Override
    public JobConsumerExecutor execute(RocketMQConsumerProperty rocketMQConsumerProperty,
                                       JobConsumerListenerAdapter jobConsumerListenerAdapter) throws MQClientException {
        // 1. 初始化消费端
        Preconditions.checkNotNull(rocketMQConsumerProperty,
                "rocketMQConsumerProperty is NULL, please build a correct value");
        if (defaultMQPushConsumer == null) {
            defaultMQPushConsumer = new DefaultMQPushConsumer(rocketMQConsumerProperty.getConsumerGroup());
        }
        defaultMQPushConsumer.setMessageModel(rocketMQConsumerProperty.getMessageModel());
        defaultMQPushConsumer.setNamesrvAddr(rocketMQConsumerProperty.getNameSrvAddr());
        defaultMQPushConsumer.setConsumeFromWhere(rocketMQConsumerProperty.getConsumeFromWhere());
        // 2. 设置消息消费回调
        defaultMQPushConsumer.registerMessageListener(jobConsumerListenerAdapter);
        // 3. 订阅消息主题
        defaultMQPushConsumer.subscribe(rocketMQConsumerProperty.getTopic(), rocketMQConsumerProperty.getSubExpression());
        LOGGER.info("JobConsumerExecuter init successfully, topic={}, consumerGroup={}",
                rocketMQConsumerProperty.getTopic(), rocketMQConsumerProperty.getConsumerGroup());
        return this;
    }

    public DefaultMQPushConsumer getDefaultMQPushConsumer() {
        return defaultMQPushConsumer;
    }

    public JobConsumerExecutor setDefaultMQPushConsumer(DefaultMQPushConsumer defaultMQPushConsumer) {
        this.defaultMQPushConsumer = defaultMQPushConsumer;
        return this;
    }

    public void start() throws MQClientException {
        this.defaultMQPushConsumer.start();
    }

    public void shutdown() {
        defaultMQPushConsumer.shutdown();
        LOGGER.info("com.shield.job.consumer.JobConsumerExecuter.DefaultMQPushConsumer has been shutdown");
    }
}
