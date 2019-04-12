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

    /**最大重复投递次数1次，超过三次后不再投递消息到MQ的broker*/
    public static final int MAX_RESEND_TIMES = 3;

    /**消息重投递生产者组前缀*/
    public static final String INNER_MSG_RESEND_PRODUCER_GROUP = "PID_MSG_RESEND_GROUP";

    /**Redis重发消息队列前缀*/
    public static final String REDIS_RETRY_MESSAGE_PREFIX = "shield_job:";

    /**消息重发线程池初始化大小*/
    public static final int REDIS_MESSAGE_RESEND_CORE_POOLSIZE = 10;

    /**消息重发MQ超时时间,10000ms*/
    public static final int MESSAGE_RESEND_MQ_TIMEOUT_SECONDS = 10000;

    /**消息重发Redis取队列超时时间*/
    public static final int MESSAGE_RESEND_REDIS_POP_TIMEOUT_SECONDS = 10;

    /**消息重发调度初始间隔*/
    public static final long MESSAGE_RESEND_EXECUTOR_SERVICE_INITDELAY = 10L;

    /**消息重发调度间隔*/
    public static final long MESSAGE_RESEND_EXECUTOR_SERVICE_DELAY = 10L;


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
     * 获取内部消息重发队列名key
     * @param suffix
     * @return
     */
    public static String getStoreRetryJobMsgQueueKey(String suffix) {
        StringBuilder keyBuilder = new StringBuilder(REDIS_RETRY_MESSAGE_PREFIX);
        return keyBuilder.append("retry_message_queue:").append(suffix).toString();
    }

    /**
     * 获取重发死信队列名key
     * @param suffix
     * @return
     */
    public static String getRetryDeadMsgQueueKey(String suffix) {
        StringBuilder keyBuilder = new StringBuilder(REDIS_RETRY_MESSAGE_PREFIX);
        return keyBuilder.append("retry_dead_message_queue:").append(suffix).toString();
    }

}
