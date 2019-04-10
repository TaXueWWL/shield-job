package com.snowalker.shield.job.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 11:53
 * @className ShieldJobMsgStoreTypeEnum
 * @desc 消费失败任务存储方式枚举
 */
public enum ShieldJobMsgResendStoreTypeEnum {

    /**
     * 使用Redis存储
     */
    MSG_STORE_TYPE_REDIS,
    /**
     * 使用MySQL存储
     */
    MSG_STORE_TYPE_MYSQL;

}
