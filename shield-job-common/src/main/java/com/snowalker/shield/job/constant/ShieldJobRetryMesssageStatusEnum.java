package com.snowalker.shield.job.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/11 14:11
 * @className ShieldJobRetryMesssageStatusEnum
 * @desc 重发消息执行结果状态枚举
 */
public enum ShieldJobRetryMesssageStatusEnum {

    /**
     * 消息存储成功
     */
    RETRY_MESSSAGE_STATUS_STORE_SUCCESS,

    /**
     * 消息存储失败
     */
    RETRY_MESSSAGE_STATUS_STORE_FAILED,

    /**
     * 消息重发次数增加成功
     */
    RETRY_MESSSAGE_STATUS_INCREMENT_SUCCESS,

    /**
     * 消息重发次数增加失败
     */
    RETRY_MESSSAGE_STATUS_INCREMENT_FAILED,

    /**
     * 死信存储成功
     */
    RETRY_MESSSAGE_STATUS_DEAD_MSG_STORE_SUCCESS,

    /**
     * 当前处于死信消息
     */
    RETRY_MESSSAGE_STATUS_DEAD_MSG;
}
