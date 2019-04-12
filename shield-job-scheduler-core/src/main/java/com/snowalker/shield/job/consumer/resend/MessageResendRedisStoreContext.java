package com.snowalker.shield.job.consumer.resend;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/11 17:14
 * @className JobRetryMessageResendContext
 * @desc 需重复消息重发布Borker上下文
 */
public class MessageResendRedisStoreContext {

    private static final List<String> redisRetryMsgQueueList =
            new CopyOnWriteArrayList<>();

    public static List<String> getRedisRetryMsgQueueList() {
        return redisRetryMsgQueueList;
    }

    public static void setRetryQueueNameToList(String queueName) {
        redisRetryMsgQueueList.add(queueName);
    }
}
