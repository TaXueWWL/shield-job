package com.snowalker.shield.job.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/1/17 17:09
 * @className CommonConstant
 * @desc 内部消息重发常量定义
 */
public class ShieldInnerMsgResendConst {

    private ShieldInnerMsgResendConst() {}

    /**最大重复投递次数3次，超过三次后不再重发*/
    private static final int MAX_RESEND_TIMES = 3;

    /**消息重投递生产者组前缀*/
    private static final String INNER_MSG_RESEND_PRODUCER_GROUP_PREFIX = "PID_MSG_RESEND_";

    /**Redis重发消息队列前缀*/
    public static final String REDIS_RETRY_MESSAGE_PREFIX = "shield_job:";


    /**
     * 获取内部消息重发次数计数器key
     * @param suffix
     * @return
     */
    public static String getResendTimesRedisKey(String suffix) {
        StringBuilder keyBuilder = new StringBuilder(REDIS_RETRY_MESSAGE_PREFIX);
        return keyBuilder.append("msg_resend_counter:").append(suffix).toString();
    }

    /**
     * 获取内部消息重发次数计数器key
     * @param suffix
     * @return
     */
    public static String getStoreRetryJobMsgQueueKey(String suffix) {
        StringBuilder keyBuilder = new StringBuilder(REDIS_RETRY_MESSAGE_PREFIX);
        return keyBuilder.append("retry_message_queue:").append(suffix).toString();
    }

    /**
     * 获取内部消息重发生产者组producerGroup值
     * @param topic
     * @return
     */
    public static String getResendPGVal(String topic) {
        StringBuilder valBuilder = new StringBuilder();
        return valBuilder.append(INNER_MSG_RESEND_PRODUCER_GROUP_PREFIX).append(topic).toString();
    }

    public static int getMaxResendTimes() {
        return MAX_RESEND_TIMES;
    }
}
