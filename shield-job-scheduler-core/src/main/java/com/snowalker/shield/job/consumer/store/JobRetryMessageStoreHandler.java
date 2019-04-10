package com.snowalker.shield.job.consumer.store;

import com.snowalker.shield.job.Result;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 14:24
 * @className JobRetryMessageStore
 * @desc TODO 消息重试存储接口
 */
public interface JobRetryMessageStoreHandler {


    /**
     * 重发消息存储
     * @param jobRetryMessage
     */
    Result<Boolean> storeRetryJobMsg(JobRetryMessage jobRetryMessage);

    /**
     * 重发计数
     * @param messageId 消息id，重发保持不变
     * @param msgTopic  消息主题，与消息id一同作为消息的唯一标识
     */
    Result<Boolean> countRetryJobMsgResendTimes(String messageId, String msgTopic);

    /**
     * 清理重发后消费成功的消息
     * @param jobRetryMessages
     * @return
     */
    Result<Boolean> removeRetrySuccessJobMsg(List<JobRetryMessage> jobRetryMessages);

    /**
     * 标记达到最大重发次数的死信
     * @param messageId 消息id，重发保持不变
     * @param msgTopic  消息主题，与消息id一同作为消息的唯一标识
     */
    Result<Boolean> markAfterRetryDeadJobMsg(String messageId, String msgTopic);
}
