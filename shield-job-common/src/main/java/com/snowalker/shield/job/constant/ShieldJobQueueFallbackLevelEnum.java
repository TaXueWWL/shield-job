package com.snowalker.shield.job.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 15:30
 * @className ShieldJobQueueFallbackLevelEnum
 * @desc 任务队列降级级别
 */
public enum ShieldJobQueueFallbackLevelEnum {

    QUEUE_FALLBACK_LEVEL_0(0, "队列降级等级0：MQ中间件异常后不进行队列降级"),
    QUEUE_FALLBACK_LEVEL_1(1, "队列降级等级1：MQ中间件异常后只降级到Redis"),
    QUEUE_FALLBACK_LEVEL_2(2, "队列降级等级2：MQ中间件异常后降级到Redis，Redis异常则降级到本地队列");

    private int code;
    private String desc;

    ShieldJobQueueFallbackLevelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
