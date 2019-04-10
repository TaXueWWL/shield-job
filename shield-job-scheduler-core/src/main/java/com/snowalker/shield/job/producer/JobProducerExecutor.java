package com.snowalker.shield.job.producer;

import com.snowalker.shield.job.BaseJob;
import com.snowalker.shield.job.JobProducer;
import com.snowalker.shield.job.JobSendResult;
import com.snowalker.shield.job.Result;
import com.snowalker.shield.job.constant.ResultCodeEnum;
import com.snowalker.shield.job.exception.JobProduceException;
import com.snowalker.shield.job.producer.listener.JobProducerListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 10:41
 * @className JobProducerExecuter
 * @desc Job生产者执行器
 */
public class JobProducerExecutor<T extends BaseJob> implements JobProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobProducerExecutor.class);

    /**
     * RocketMQ普通消息发送引用
     */
    private DefaultMQProducer defaultMQProducer;

    private int sendTimeout = 3000;

    /**
     * 初始化执行器
     * @param rocketMQProducerProperty
     * @return
     */
    @Override
    public JobProducerExecutor init(RocketMQProducerProperty rocketMQProducerProperty) {
        String nameSrvAddr = rocketMQProducerProperty.getNameSrvAddr();
        String producerGroup = rocketMQProducerProperty.getProducerGroup();
        // 初始化生产者
        if (defaultMQProducer == null) {
            defaultMQProducer = new DefaultMQProducer(producerGroup);
        }
        defaultMQProducer.setNamesrvAddr(nameSrvAddr);
        LOGGER.info("JobProducerExecutor init successfully, jobProducerGroup={}", producerGroup);
        return this;
    }

    /**
     * 执行Job生产逻辑，投递Job消息到MQ中
     * @param jobProducerListener
     * @param arg
     * @return 发送失败的Job列表
     */
    @Override
    public Result<JobSendResult> execute(final JobProducerListener jobProducerListener,
                                         Object arg) {
        // 取出生产者作业集合
        List<T> jobList = jobProducerListener.produce(arg);
        if (jobList == null || jobList.size() < 0) {
            throw new JobProduceException("BaseJob list is empty, " +
                    "please make true you have returned job list in com.shield.job.producer.JobProducerListener.produce(final Object arg)");
        }
        String topic = jobList.get(0).getJobTopic();
        String tag = jobList.get(0).getJobTag();
        if (StringUtils.isBlank(topic)) {
            throw new JobProduceException("JobTopic is NULL, please fill in a correct value");
        }
        if (StringUtils.isBlank(tag)) {
            throw new JobProduceException("JobTag is NULL, please fill in a correct value");
        }

        /**发送成功的Job列表，返回给调用方处理*/
        final List<BaseJob> sendSuccessJobList = new ArrayList<>();
        /**发送失败的Job列表，返回给调用方处理*/
        final List<BaseJob> sendFailureJobList = new ArrayList<>();
        /**发送结果包装*/
        Result<JobSendResult> result = new Result<>();
        JobSendResult jobSendResult = new JobSendResult();
        try {
            // 遍历任务列表发送, 注意Tag使用了协议中的tag
            for (T t : jobList) {
                String messageBody = t.encode();
                Message message = new Message(t.getJobTopic(), t.getJobTag(),
                        messageBody.getBytes());
                try {
                    SendResult sendResult = defaultMQProducer.send(message, sendTimeout);
                    if (sendResult != null) {
                        LOGGER.debug("send job message successful, message sendStatus={}, msgId={}, queueOffset={}, transactionId={}, offsetMsgId={}, regionId={}, sendSuccessJobList.size()={}",
                                sendResult.getSendStatus(),
                                sendResult.getMsgId(),
                                sendResult.getQueueOffset(),
                                sendResult.getTransactionId(),
                                sendResult.getOffsetMsgId(),
                                sendResult.getRegionId(),
                                sendSuccessJobList.size());
                        // 将消息id返回
                        t.setJobMsgId(sendResult.getMsgId());
                        sendSuccessJobList.add(t);
                    }
                } catch (MQBrokerException e) {
                    sendFailureJobList.add(t);
                    LOGGER.error("send job message occurred Exception, job detail={}", t.toString(), e);
                }
            }
            // 包装返回发送结果体
            jobSendResult = jobSendResult
                    .setSendSuccessJobList(sendSuccessJobList)
                    .setSendFailureJobList(sendFailureJobList);
            LOGGER.info("send job messages finished, sendSuccessJobList messages size={}, sendFailureJobList messages size={}, topic={}, tag={}",
                    sendSuccessJobList.size(), sendFailureJobList.size(), topic, tag);
            return new Result(true,
                    ResultCodeEnum.SUCCESS_CODE.getCode(),
                    ResultCodeEnum.SUCCESS_CODE.getDesc(),
                    true,
                    jobSendResult);
        } catch (MQClientException | RemotingException | InterruptedException e) {
            LOGGER.error("send message failure, jobTopic={}", topic, e);
            return new Result(false,
                    ResultCodeEnum.FAIL_CODE.getCode(),
                    ResultCodeEnum.FAIL_CODE.getDesc(),
                    false,
                    null);
        }
    }

    public void start() throws MQClientException {
        this.defaultMQProducer.start();
    }

    public void shutdown() {
        this.defaultMQProducer.shutdown();
        LOGGER.info("com.shield.job.producer.JobProducerExecuter.DefaultMQProducer has been shutdown");
    }

    public DefaultMQProducer getProducer() {
        return defaultMQProducer;
    }

    public JobProducerExecutor<T> setDefaultMQProducer(DefaultMQProducer defaultMQProducer) {
        this.defaultMQProducer = defaultMQProducer;
        return this;
    }

    public int getSendTimeout() {
        return sendTimeout;
    }

    public JobProducerExecutor<T> setSendTimeout(int sendTimeout) {
        this.sendTimeout = sendTimeout;
        return this;
    }
}
