package com.snowalker.shield.jobdemo.consumer;

import com.snowalker.shield.job.consumer.JobConsumerExecutor;
import com.snowalker.shield.job.consumer.JobConsumerListenerAdapter;
import com.snowalker.shield.job.consumer.RocketMQConsumerProperty;
import com.snowalker.shield.job.consumer.listener.JobConsumerListener;
import com.snowalker.shield.jobdemo.protocol.OrderInfoJobProcotol;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/9 10:49
 * @className OrderInfoJobConsumer
 * @desc 测试订单
 */
@Component
public class OrderInfoJobConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInfoJobConsumer.class);

    /**测试订单任务TOPIC*/
    private static final String TOPIC = "SNOWALKER_TEST_SHIELD_JOB_TOPIC";
    /**测试订单任务TAG*/
    private static final String TAG = "SNOWALKER_TEST_SHIELD_JOB_TAG";
    /**测试订单生产者组*/
    private static final String CONSUMER_GROUP = "CID_SNOWALKER_TEST_SHIELD_JOB";

    @Value("${rocketmq.nameServer}")
    private String nameSrvAddr;

    @PostConstruct
    public void execute() throws MQClientException {
        /**
         * lambda模式
         */
        new JobConsumerExecutor().execute(
                new RocketMQConsumerProperty(TOPIC, CONSUMER_GROUP, nameSrvAddr, TAG),
                    new JobConsumerListenerAdapter((msgs, context) -> {
                        return getConsumeConcurrentlyStatus(msgs);
                    }).setMaxReconsumeTimes(1)
        ).start();
    }

    private ConsumeConcurrentlyStatus getConsumeConcurrentlyStatus(List<MessageExt> msgs) {
        try {
            Thread.sleep(3000);
            // 默认msgs只有一条消息
            for (MessageExt msg : msgs) {
                String message = new String(msg.getBody());
                OrderInfoJobProcotol protocol = new OrderInfoJobProcotol();
                protocol.decode(message);
                // 解析打印实体
                LOGGER.info("模拟订单Job消息消费成功,msgId={}, protocol={}", msg.getMsgId(), protocol.toString());
            }
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        } catch (Exception e) {
            LOGGER.error("钱包扣款消费异常,e={}", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

    /**
     * 普通监听器模式简洁写法
     * @throws MQClientException
     */
    public void consumerWithListenerOneLine() throws MQClientException {
        new JobConsumerExecutor().execute(
                new RocketMQConsumerProperty(
                        TOPIC, CONSUMER_GROUP, nameSrvAddr, TAG), new JobConsumerListenerAdapter(new JobConsumerListener() {
                    @Override
                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                        return getConsumeConcurrentlyStatus(msgs);
                    }
                })).start();
    }

    /**
     * 普通监听器模式
     */
    private void consumerWithListener() throws MQClientException {
        JobConsumerExecutor jobConsumerExecuter = new JobConsumerExecutor();
        JobConsumerListenerAdapter jobConsumerListenerAdapter =
                new JobConsumerListenerAdapter(new JobConsumerListener() {
                    @Override
                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                        return getConsumeConcurrentlyStatus(msgs);
                    }
                });
        jobConsumerExecuter.execute(
                new RocketMQConsumerProperty(
                        TOPIC, CONSUMER_GROUP, nameSrvAddr, TAG
                ), jobConsumerListenerAdapter).start();
    }

}
