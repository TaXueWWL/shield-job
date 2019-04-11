package com.snowalker.shield.job.consumer.store.impl;

import com.snowalker.shield.job.Result;
import com.snowalker.shield.job.constant.ResultCodeEnum;
import com.snowalker.shield.job.constant.ShieldInnerMsgResendConst;
import com.snowalker.shield.job.consumer.store.JobRetryMessage;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 16:05
 * @className JobRetryMessageRedisHandler
 * @desc todo 队列列表记录
 */
public class JobRetryMessageRedisHandler implements JobRetryMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRetryMessageRedisHandler.class);

    private MessageStoreRedisTemplate messageStoreRedisTemplate;

    public JobRetryMessageRedisHandler(MessageStoreRedisTemplate messageStoreRedisTemplate) {
        this.messageStoreRedisTemplate = messageStoreRedisTemplate;
    }

    @Override
    public Result<Boolean> storeRetryJobMsg(JobRetryMessage jobRetryMessage) {
        String msgId = jobRetryMessage.getMsgId();
        String msgTopic = jobRetryMessage.getMsgTopic();
        String msgTag = jobRetryMessage.getMsgTag();
        String msgBody = jobRetryMessage.getMsgBody();
        try {
            // 序列化重发消息实体到字符串
            String storeRetryJobMsgVal = jobRetryMessage.encode();
            // 放入Redis的队列中，队列名：TOPIC_TAG
            String queueNameSuffix = new StringBuilder()
                    .append(jobRetryMessage.getMsgTopic())
                    .append(":")
                    .append(jobRetryMessage.getMsgTag())
                    .toString();
            String queueName = ShieldInnerMsgResendConst.getStoreRetryJobMsgQueueKey(queueNameSuffix.toLowerCase());
//            Object snowalker = listOperations.rightPop("snowalker", 10, TimeUnit.SECONDS);
            long result = messageStoreRedisTemplate.getRedisTemplate().opsForList().leftPush(queueName, storeRetryJobMsgVal);
            LOGGER.debug("Storing Retry Job message into Redis has finished, result={}, msgId={}, topic={}, tag={}",
                    result, msgId, msgTopic, msgTag);
            return result > 0 ?
                    new Result<>(ResultCodeEnum.SUCCESS_CODE.getCode(), ResultCodeEnum.SUCCESS_CODE.getDesc(), true) :
                    new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), false);
        } catch (Exception e) {
            LOGGER.error("Storing Retry Job message into Redis occurred Exception, msgId={}, topic={}, tag={}, messageBody={}",
                    msgId, msgTopic, msgTag, msgBody, e);
            return new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), false);
        }
    }

    @Override
    public Result<Boolean> countRetryJobMsgResendTimes(String messageId, String msgTopic) {
        try {
            // 拼装全局重发次数key，每一轮调用时增1
            String suffix = new StringBuilder(msgTopic).append(":").append(messageId).toString().toLowerCase();
            String jobMsgResendTimesKey = ShieldInnerMsgResendConst.getResendTimesRedisKey(suffix);
            long result = messageStoreRedisTemplate.getRedisTemplate().opsForValue().increment(jobMsgResendTimesKey);
            LOGGER.debug("Retry Job message resend times increment finished, result={}, messageId={}, msgTopic={}",
                    result, messageId, msgTopic);
            return result > 0 ?
                    new Result<>(ResultCodeEnum.SUCCESS_CODE.getCode(), ResultCodeEnum.SUCCESS_CODE.getDesc(), true) :
                    new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), false);
        } catch (Exception e) {
            LOGGER.error("Retry Job message resend times increment occurred Exception, messageId={}, msgTopic={}", messageId, msgTopic, e);
            return new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), false);
        }
    }

    @Override
    public Result<Boolean> removeRetrySuccessJobMsg(List<JobRetryMessage> jobRetryMessages) {
        return null;
    }

    @Override
    public Result<Boolean> markAfterRetryDeadJobMsg(String messageId, String msgTopic) {
        return null;
    }
}
