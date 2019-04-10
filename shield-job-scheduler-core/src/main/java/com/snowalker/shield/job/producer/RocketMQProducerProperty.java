package com.snowalker.shield.job.producer;

import com.google.common.base.Preconditions;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 16:44
 * @className RocketMQProducerProperty
 * @desc RocketMQ生产者属性
 */
public class RocketMQProducerProperty {

    /**生产者组名，建议以PID开头*/
    private String producerGroup;

    /**nameServer地址*/
    private String nameSrvAddr;

    public RocketMQProducerProperty(String producerGroup, String nameSrvAddr) {
        Preconditions.checkNotNull(producerGroup, "producerGroup is NULL, please fill in a correct value");
        Preconditions.checkNotNull(nameSrvAddr, "nameSrvAddr is NULL, please fill in a correct value");
        this.producerGroup = producerGroup;
        this.nameSrvAddr = nameSrvAddr;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public String getNameSrvAddr() {
        return nameSrvAddr;
    }

}
