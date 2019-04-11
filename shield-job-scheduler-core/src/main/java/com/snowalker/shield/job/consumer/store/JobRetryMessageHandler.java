package com.snowalker.shield.job.consumer.store;

import com.snowalker.shield.job.Result;
import com.snowalker.shield.job.constant.ShieldJobRetryMesssageStatusEnum;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 14:24
 * @className JobRetryMessageStore
 * @desc 消息重试存储接口
 * TODO 清除达到最大消费次数的普通队列中的值并入死信
 */
public interface JobRetryMessageHandler {


    /**
     * 重发消息存储
     * @param jobRetryMessage
     */
    Result<ShieldJobRetryMesssageStatusEnum> storeRetryJobMsg(JobRetryMessage jobRetryMessage);

    /**
     * 消息重投递计数
     * 消息主题，与消息id一同作为消息的唯一标识
     * @param jobRetryMessage
     */
    Result<ShieldJobRetryMesssageStatusEnum> countRetryJobMsgResendTimes(JobRetryMessage jobRetryMessage);

    /**
     * 清理重发后消费成功的消息
     * @param jobRetryMessages
     * @return
     */
    Result<ShieldJobRetryMesssageStatusEnum> removeRetrySuccessJobMsg(List<JobRetryMessage> jobRetryMessages);

    /**
     * 标记达到最大重发次数的死信
     * @param messageId 消息id，重发保持不变
     * @param msgTopic  消息主题，与消息id一同作为消息的唯一标识
     */
    Result<ShieldJobRetryMesssageStatusEnum> markAfterRetryDeadJobMsg(String messageId, String msgTopic);

    /**
     * 存储重试消息到死信队列
     * TODO 对死信做操作
     * @param jobRetryMessage
     * @return
     */
    Result<ShieldJobRetryMesssageStatusEnum> storeRetryJobMsgIntoDeadQueue(JobRetryMessage jobRetryMessage);

}
