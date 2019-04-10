package com.snowalker.shield.job;

import com.snowalker.shield.job.producer.JobProducerExecutor;
import com.snowalker.shield.job.producer.RocketMQProducerProperty;
import com.snowalker.shield.job.producer.listener.JobProducerListener;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 15:47
 * @className JobProducer
 * @desc Job生产者接口
 */
public interface JobProducer {

    /**
     * 初始化Executer
     * @param rocketMQProducerProperty
     * @return
     */
    JobProducerExecutor init(RocketMQProducerProperty rocketMQProducerProperty);


    /**
     * 执行Job生产逻辑，投递Job消息到MQ中
     * @param jobProducerListener
     * @param arg
     * @return 发送的Job列表
     */
    Result<JobSendResult> execute(final JobProducerListener jobProducerListener,
                                  Object arg);
}
