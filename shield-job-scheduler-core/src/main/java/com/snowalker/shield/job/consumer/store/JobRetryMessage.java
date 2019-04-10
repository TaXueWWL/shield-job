package com.snowalker.shield.job.consumer.store;

import com.snowalker.shield.job.constant.ShieldJobMsgResendStatusEnum;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 15:08
 * @className JobRetryMessage
 * @desc 任务重发消息实体
 */
public class JobRetryMessage {

    /**消息id*/
    private String msgId;

    /**消息主题*/
    private String msgTopic;

    /**重发生产者组*/
    private String msgRetryProducerGroup;

    /**nameSrv地址*/
    private String msgNameSrvAddr;

    /**消息体*/
    private String msgBody;

    /**默认重发状态为待重发*/
    private ShieldJobMsgResendStatusEnum msgResendStatusEnum = ShieldJobMsgResendStatusEnum.MSG_RESEND_STATUS_RECONSUMELATER;

    public String getMsgTopic() {
        return msgTopic;
    }

    public JobRetryMessage setMsgTopic(String msgTopic) {
        this.msgTopic = msgTopic;
        return this;
    }

    public String getMsgId() {
        return msgId;
    }

    public JobRetryMessage setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public String getMsgRetryProducerGroup() {
        return msgRetryProducerGroup;
    }

    public JobRetryMessage setMsgRetryProducerGroup(String msgRetryProducerGroup) {
        this.msgRetryProducerGroup = msgRetryProducerGroup;
        return this;
    }

    public String getMsgNameSrvAddr() {
        return msgNameSrvAddr;
    }

    public JobRetryMessage setMsgNameSrvAddr(String msgNameSrvAddr) {
        this.msgNameSrvAddr = msgNameSrvAddr;
        return this;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public JobRetryMessage setMsgBody(String msgBody) {
        this.msgBody = msgBody;
        return this;
    }

    public ShieldJobMsgResendStatusEnum getMsgResendStatusEnum() {
        return msgResendStatusEnum;
    }

    public JobRetryMessage setMsgResendStatusEnum(ShieldJobMsgResendStatusEnum msgResendStatusEnum) {
        this.msgResendStatusEnum = msgResendStatusEnum;
        return this;
    }

    @Override
    public String toString() {
        return "JobRetryMessage{" +
                "msgId='" + msgId + '\'' +
                ", msgTopic='" + msgTopic + '\'' +
                ", msgRetryProducerGroup='" + msgRetryProducerGroup + '\'' +
                ", msgNameSrvAddr='" + msgNameSrvAddr + '\'' +
                ", msgBody='" + msgBody + '\'' +
                ", msgResendStatusEnum=" + msgResendStatusEnum +
                '}';
    }
}
