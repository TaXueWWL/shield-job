package com.snowalker.shield.job.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 11:53
 * @className ShieldJobMsgStoreTypeEnum
 * @desc 任务重发后消费状态枚举，该状态为每轮重试达到最大设定重试次数时候的消息状态，
 * 一旦成功则更改状态为MSG_RESEND_STATUS_COMMIT
 * 否则直到达到最大次数时候仍旧为MSG_RESEND_STATUS_RECONSUMELATER，则状态为MSG_RESEND_STATUS_RECONSUMELATER
 */
public enum ShieldJobMsgResendStatusEnum {

    /**
     * 重试消费成功
     */
    MSG_RESEND_STATUS_COMMIT,
    /**
     * 重试消费失败，仍需继续消费
     */
    MSG_RESEND_STATUS_RECONSUMELATER;
}
