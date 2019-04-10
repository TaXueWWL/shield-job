package com.snowalker.shield.job.consumer.store.impl;

import com.snowalker.shield.job.Result;
import com.snowalker.shield.job.consumer.store.JobRetryMessage;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandler;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 16:05
 * @className JobRetryMessageRedisHandler
 * @desc
 */
public class JobRetryMessageRedisHandler implements JobRetryMessageHandler {



    @Override
    public Result<Boolean> storeRetryJobMsg(JobRetryMessage jobRetryMessage) {
        return null;
    }

    @Override
    public Result<Boolean> countRetryJobMsgResendTimes(String messageId, String msgTopic) {
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
