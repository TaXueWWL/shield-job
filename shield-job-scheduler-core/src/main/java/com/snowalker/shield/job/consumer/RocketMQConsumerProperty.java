package com.snowalker.shield.job.consumer;

import com.google.common.base.Preconditions;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 16:10
 * @className RocketMQConsumerProperty
 * @desc RocketMQ消费者初始化参数
 */
public class RocketMQConsumerProperty {

    /**待订阅主题*/
    private String topic;

    /**消费者组名，建议以CID开头*/
    private String consumerGroup;

    /**nameServer地址*/
    private String nameSrvAddr;

    /**消息消费表达式，子表达式，如："tag1 || tag2 || tag3" <br> null 或者 * 表示订阅所有*/
    private String subExpression = "*";

    /**消息模式，默认集群模式*/
    private MessageModel messageModel = MessageModel.CLUSTERING;

    /**从何处开始消费，默认从头开始*/
    private ConsumeFromWhere consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;

    public RocketMQConsumerProperty(String topic,
                                    String consumerGroup,
                                    String nameSrvAddr,
                                    String subExpression,
                                    MessageModel messageModel,
                                    ConsumeFromWhere consumeFromWhere) {
        Preconditions.checkNotNull(topic, "topic is NULL, please fill in correct value");
        Preconditions.checkNotNull(consumerGroup, "consumerGroup is NULL, please fill in a correct value");
        Preconditions.checkNotNull(nameSrvAddr, "nameSrvAddr is NULL, please fill in a correct value");
        Preconditions.checkNotNull(subExpression, "subExpression is NULL, please fill in a correct value");
        this.topic = topic;
        this.messageModel = messageModel;
        this.consumerGroup = consumerGroup;
        this.subExpression = subExpression;
        this.nameSrvAddr = nameSrvAddr;
        this.consumeFromWhere = consumeFromWhere;
    }

    public RocketMQConsumerProperty(String topic,
                                    String consumerGroup,
                                    String nameSrvAddr,
                                    String subExpression) {
        Preconditions.checkNotNull(topic, "topic is NULL, please fill in correct value");
        Preconditions.checkNotNull(consumerGroup, "consumerGroup is NULL, please fill in correct value");
        Preconditions.checkNotNull(nameSrvAddr, "nameSrvAddr is NULL, please fill in correct value");
        Preconditions.checkNotNull(subExpression, "subExpression is NULL, please fill in correct value");
        this.topic = topic;
        this.consumerGroup = consumerGroup;
        this.nameSrvAddr = nameSrvAddr;
        this.subExpression = subExpression;
    }

    public RocketMQConsumerProperty() {
    }

    public String getTopic() {
        return topic;
    }

    public RocketMQConsumerProperty setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public RocketMQConsumerProperty setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
        return this;
    }

    public String getNameSrvAddr() {
        return nameSrvAddr;
    }

    public RocketMQConsumerProperty setNameSrvAddr(String nameSrvAddr) {
        this.nameSrvAddr = nameSrvAddr;
        return this;
    }

    public String getSubExpression() {
        return subExpression;
    }

    public RocketMQConsumerProperty setSubExpression(String subExpression) {
        this.subExpression = subExpression;
        return this;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public RocketMQConsumerProperty setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
        return this;
    }

    public ConsumeFromWhere getConsumeFromWhere() {
        return consumeFromWhere;
    }

    public RocketMQConsumerProperty setConsumeFromWhere(ConsumeFromWhere consumeFromWhere) {
        this.consumeFromWhere = consumeFromWhere;
        return this;
    }

    @Override
    public String toString() {
        return "RocketMQConsumerProperty{" +
                "topic='" + topic + '\'' +
                ", consumerGroup='" + consumerGroup + '\'' +
                ", nameSrvAddr='" + nameSrvAddr + '\'' +
                ", subExpression='" + subExpression + '\'' +
                ", messageModel=" + messageModel +
                ", consumeFromWhere=" + consumeFromWhere +
                '}';
    }
}
