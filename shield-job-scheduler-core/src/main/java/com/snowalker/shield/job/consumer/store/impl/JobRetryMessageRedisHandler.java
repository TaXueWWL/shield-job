package com.snowalker.shield.job.consumer.store.impl;

import com.snowalker.shield.job.Result;
import com.snowalker.shield.job.constant.ResultCodeEnum;
import com.snowalker.shield.job.constant.ShieldInnerMsgResendConst;
import com.snowalker.shield.job.constant.ShieldJobRetryMesssageStatusEnum;
import com.snowalker.shield.job.consumer.resend.MessageResendRedisStoreContext;
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
 * @desc 重发消息处理器Redis实现
 */
public class JobRetryMessageRedisHandler implements JobRetryMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRetryMessageRedisHandler.class);

    private MessageStoreRedisTemplate messageStoreRedisTemplate;

    public JobRetryMessageRedisHandler(MessageStoreRedisTemplate messageStoreRedisTemplate) {
        this.messageStoreRedisTemplate = messageStoreRedisTemplate;
    }

    /**
     * 存储重试消息到Redis
     * @param jobRetryMessage
     * @return
     */
    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> storeRetryJobMsg(JobRetryMessage jobRetryMessage) {
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
                    .toString();
            String queueName = ShieldInnerMsgResendConst.getStoreRetryJobMsgQueueKey(queueNameSuffix.toLowerCase());
            // result 列表插入后的长度
            long result = messageStoreRedisTemplate.getRedisTemplate().opsForList().leftPush(queueName, storeRetryJobMsgVal);
            // 设置重发队列名到JobRetryMessageResendContext
            MessageResendRedisStoreContext.setRetryQueueNameToList(queueName);
            // 设置当前消息重发次数
            messageStoreRedisTemplate.getRedisTemplate().opsForValue()
                .increment(ShieldInnerMsgResendConst.getResendTimesRedisKey(msgId));

            LOGGER.debug("Storing Retry Job message into Redis has finished, msgId={}, topic={}, tag={}",
                    msgId, msgTopic, msgTag);
            return result > 0 ?
                    new Result<>(ResultCodeEnum.SUCCESS_CODE.getCode(), ResultCodeEnum.SUCCESS_CODE.getDesc(), ShieldJobRetryMesssageStatusEnum.RETRY_MESSSAGE_STATUS_STORE_SUCCESS) :
                    new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), ShieldJobRetryMesssageStatusEnum.RETRY_MESSSAGE_STATUS_STORE_FAILED);
        } catch (Exception e) {
            LOGGER.error("Storing Retry Job message into Redis occurred Exception, msgId={}, topic={}, tag={}, messageBody={}",
                    msgId, msgTopic, msgTag, msgBody, e);
            return new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), ShieldJobRetryMesssageStatusEnum.RETRY_MESSSAGE_STATUS_STORE_FAILED);
        }
    }

    /**
     * 消息重投递计数
     * 拼装全局重发次数key，每一轮调用时增1
     * @param jobRetryMessage
     * @return
     */
    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> countRetryJobMsgResendTimes(JobRetryMessage jobRetryMessage) {
        String messageId = jobRetryMessage.getMsgId();
        String msgTopic = jobRetryMessage.getMsgTopic();
        try {
            String suffix = new StringBuilder(msgTopic).append(":").append(messageId).toString().toLowerCase();
            String jobMsgResendTimesKey = ShieldInnerMsgResendConst.getResendTimesRedisKey(suffix);

            long result = messageStoreRedisTemplate.getRedisTemplate().opsForValue().increment(jobMsgResendTimesKey);
            LOGGER.debug("Retry Job message resend times increment finished, result={}, messageId={}, msgTopic={}",
                    result, messageId, msgTopic);

            // 读取当前已重投递次数，如果超过最大限制投递次数则消息入死信，不再重投递, 当前消息需要弹出队列
            Integer resendTimes = (Integer) messageStoreRedisTemplate.getRedisTemplate().opsForValue().get(jobMsgResendTimesKey);
            if (resendTimes != null && resendTimes > ShieldInnerMsgResendConst.MAX_RESEND_TIMES) {
                LOGGER.warn("The message has come to MaxResendTimes limit, put it into [dead-Msg-Queue], msgId={}, msgTopic={}, currentResendTimes={}",
                        messageId, msgTopic, resendTimes);
                this.storeRetryJobMsgIntoDeadQueue(jobRetryMessage);
                return new Result<>(ResultCodeEnum.SUCCESS_CODE.getCode(), ResultCodeEnum.SUCCESS_CODE.getDesc(), ShieldJobRetryMesssageStatusEnum.RETRY_MESSSAGE_STATUS_DEAD_MSG);
            }

            return result > 0 ?
                    new Result<>(ResultCodeEnum.SUCCESS_CODE.getCode(), ResultCodeEnum.SUCCESS_CODE.getDesc(), ShieldJobRetryMesssageStatusEnum.RETRY_MESSSAGE_STATUS_INCREMENT_SUCCESS) :
                    new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), ShieldJobRetryMesssageStatusEnum.RETRY_MESSSAGE_STATUS_INCREMENT_FAILED);
        } catch (Exception e) {
            LOGGER.error("Retry Job message resend times increment occurred Exception, messageId={}, msgTopic={}", messageId, msgTopic, e);
            return new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), ShieldJobRetryMesssageStatusEnum.RETRY_MESSSAGE_STATUS_INCREMENT_FAILED);
        }
    }

    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> removeRetrySuccessJobMsg(List<JobRetryMessage> jobRetryMessages) {
        return null;
    }

    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> transferDeadJobMsg(String messageId, String msgTopic) {
        return null;
    }

    /**
     * 消息入死信队列
     * @param jobRetryMessage
     * @return
     */
    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> storeRetryJobMsgIntoDeadQueue(JobRetryMessage jobRetryMessage) {
        String msgId = jobRetryMessage.getMsgId();
        String msgTopic = jobRetryMessage.getMsgTopic();
        String msgTag = jobRetryMessage.getMsgTag();
        String deadMsgQueueName = ShieldInnerMsgResendConst.getRetryDeadMsgQueueKey(msgTopic);
        // 将该消息从原队列弹出入死信
        messageStoreRedisTemplate.getRedisTemplate()
                .opsForList()
                    .leftPush(deadMsgQueueName, jobRetryMessage.encode());
        LOGGER.debug("Storing dead Job message into [DEAD-MSG-QUEUE] has finished, msgId={}, topic={}, tag={}, queueName={}",
                msgId, msgTopic, msgTag, deadMsgQueueName);
        return new Result<>(ResultCodeEnum.SUCCESS_CODE.getCode(),
                ResultCodeEnum.SUCCESS_CODE.getDesc(),
                ShieldJobRetryMesssageStatusEnum.RETRY_MESSSAGE_STATUS_DEAD_MSG_STORE_SUCCESS);
    }
}
