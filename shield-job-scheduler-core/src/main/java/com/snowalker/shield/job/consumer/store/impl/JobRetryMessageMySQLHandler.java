package com.snowalker.shield.job.consumer.store.impl;

import com.snowalker.shield.job.Result;
import com.snowalker.shield.job.constant.ShieldJobRetryMesssageStatusEnum;
import com.snowalker.shield.job.consumer.store.JobRetryMessage;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandler;
import com.snowalker.shield.job.consumer.store.MessageStoreClientTemplate;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 16:06
 * @className JobRetryMessageMySQLHandler
 * @desc TODO 作业消息存储MySQL处理器实现
 * TODO messageStoreClientTemplate强转为JdbcTemplate
 */
public class JobRetryMessageMySQLHandler implements JobRetryMessageHandler {

    private MessageStoreClientTemplate messageStoreClientTemplate;

    public JobRetryMessageMySQLHandler(MessageStoreClientTemplate messageStoreClientTemplate) {
        this.messageStoreClientTemplate = messageStoreClientTemplate;
    }

    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> storeRetryJobMsg(JobRetryMessage jobRetryMessage) {
        System.out.println("作业消息存储MySQL处理器实现--模拟存储消息");
        return null;
    }

    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> countRetryJobMsgResendTimes(JobRetryMessage jobRetryMessage) {
        System.out.println("作业消息存储MySQL处理器实现--模拟增加重试次数");
        return null;
    }

    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> removeRetrySuccessJobMsg(List<JobRetryMessage> jobRetryMessages) {
        return null;
    }

    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> markAfterRetryDeadJobMsg(String messageId, String msgTopic) {
        return null;
    }

    @Override
    public Result<ShieldJobRetryMesssageStatusEnum> storeRetryJobMsgIntoDeadQueue(JobRetryMessage jobRetryMessage) {
        return null;
    }
}
