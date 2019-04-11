package com.snowalker.shield.job.consumer.store.impl;

import com.snowalker.shield.job.Result;
import com.snowalker.shield.job.constant.ResultCodeEnum;
import com.snowalker.shield.job.constant.ShieldInnerMsgResendConst;
import com.snowalker.shield.job.consumer.store.JobRetryMessage;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 16:05
 * @className JobRetryMessageRedisHandler
 * @desc todo
 */
public class JobRetryMessageRedisHandler implements JobRetryMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRetryMessageRedisHandler.class);

    private MessageStoreRedisTemplate messageStoreRedisTemplate;

    public JobRetryMessageRedisHandler(MessageStoreRedisTemplate messageStoreRedisTemplate) {
        this.messageStoreRedisTemplate = messageStoreRedisTemplate;
    }

    @Override
    public Result<Boolean> storeRetryJobMsg(JobRetryMessage jobRetryMessage) {
        try {
            // 序列化重发消息实体到字符串
//            String suffix = new StringBuilder(jobRetryMessage.getMsgId()).append("_")
//                    .append(jobRetryMessage.getMsgTopic())
//                    .toString().toLowerCase();
//            System.out.println("suffix: " + suffix);
//            String storeRetryJobMsgKey = ShieldInnerMsgResendConst.getStoreRetryJobMsgRedisKey(suffix);
            String storeRetryJobMsgVal = jobRetryMessage.encode();
            // 放入Redis的队列中，队列名：TOPIC_TAG
            String queueName = new StringBuilder(ShieldInnerMsgResendConst.REDIS_RETRY_MESSAGE_QUEUE_PREFIX)
                    .append(jobRetryMessage.getMsgTopic())
                    .append(":")
                    .append(jobRetryMessage.getMsgTag())
                    .toString();
            // TODO 完善队列操作，topic+tag 唯一确定一个队列
            System.out.println("----messageStoreRedisTemplate instance of RedisTemplate-----" + (messageStoreRedisTemplate instanceof RedisTemplate));
            System.out.println("----messageStoreRedisTemplate----" + messageStoreRedisTemplate);

            ListOperations listOperations = messageStoreRedisTemplate.getRedisTemplate().opsForList();
//            Object snowalker = listOperations.rightPop("snowalker", 10, TimeUnit.SECONDS);
            System.out.println("----listOperations!=null----" + listOperations != null);
            long result = messageStoreRedisTemplate.getRedisTemplate().opsForList().leftPush(queueName, storeRetryJobMsgVal);
            System.out.println("----messageStoreClientTemplate instance of RedisTemplate-----" + (messageStoreRedisTemplate instanceof RedisTemplate));
            return result > 0 ?
                    new Result<>(ResultCodeEnum.SUCCESS_CODE.getCode(), ResultCodeEnum.SUCCESS_CODE.getDesc(), true) :
                    new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), false);
        } catch (Exception e) {
            LOGGER.error("store retry message into Redis occurred Exception, messageBody={}", jobRetryMessage.getMsgBody(), e);
            return new Result<>(ResultCodeEnum.FAIL_CODE.getCode(), ResultCodeEnum.FAIL_CODE.getDesc(), false);
        }
    }

    @Override
    public Result<Boolean> countRetryJobMsgResendTimes(String messageId, String msgTopic) {
        System.out.println("作业消息存储Redis处理器实现--模拟增加重试次数");
        String suffix = new StringBuilder(messageId).append("_").append(msgTopic).toString().toLowerCase();
        String jobMsgResendTimesKey = ShieldInnerMsgResendConst.getResendTimesRedisKey(suffix);
        return null;
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
