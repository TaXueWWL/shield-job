package com.snowalker.shield.job.consumer.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.snowalker.shield.job.constant.ShieldJobMsgResendStatusEnum;
import com.snowalker.shield.job.exception.JobConsumeException;

import java.io.IOException;
import java.util.Map;

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

    /**消息Tag*/
    private String msgTag;

    /**重发生产者组*/
    private String msgRetryProducerGroup;

    /**nameSrv地址*/
    private String msgNameSrvAddr;

    /**消息体*/
    private String msgBody;

    /**默认重发状态为待重发*/
    private String msgResendStatusEnum = ShieldJobMsgResendStatusEnum.MSG_RESEND_STATUS_RECONSUMELATER.toString();

    private Map<String, String> jobRetryMessageMap;

    /**
     * 转换实体到Json串
     * @return
     */
    public String encode() throws JobConsumeException {
        ImmutableMap.Builder mapBuilder = new ImmutableMap.Builder<String, String>()
                .put("msgId", this.getMsgId())
                .put("msgTopic", this.getMsgTopic())
                .put("msgTag", this.getMsgTag())
                .put("msgRetryProducerGroup", this.getMsgRetryProducerGroup())
                .put("msgNameSrvAddr", this.getMsgNameSrvAddr())
                .put("msgBody", this.getMsgBody())
                .put("msgResendStatusEnum", this.getMsgResendStatusEnum());

        jobRetryMessageMap = mapBuilder.build();

        String ret_string = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ret_string = objectMapper.writeValueAsString(jobRetryMessageMap);
        } catch (JsonProcessingException e) {
            throw new JobConsumeException("encode() serialize JobRetryMessage occurred Exception, util is com.fasterxml.jackson.databind.ObjectMapper");
        }
        return ret_string;
    }

    /**
     * 转换Json串到实体
     * @return
     */
    public void decode(String msg) throws JobConsumeException {
        Preconditions.checkNotNull(msg);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(msg);
            this.setMsgId(root.get("msgId").asText());
            this.setMsgTopic(root.get("msgTopic").asText());
            this.setMsgTag(root.get("msgTag").asText());
            this.setMsgRetryProducerGroup(root.get("msgRetryProducerGroup").asText());
            this.setMsgNameSrvAddr(root.get("msgNameSrvAddr").asText());
            this.setMsgBody(root.get("msgBody").asText());
            this.setMsgResendStatusEnum(root.get("msgResendStatusEnum").asText());
        } catch (IOException e) {
            throw new JobConsumeException("decode() deserialize JobRetryMessage occurred Exception, util is com.fasterxml.jackson.databind.ObjectMapper");
        }
    }

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

    public String getMsgResendStatusEnum() {
        return msgResendStatusEnum;
    }

    public JobRetryMessage setMsgResendStatusEnum(String msgResendStatusEnum) {
        this.msgResendStatusEnum = msgResendStatusEnum;
        return this;
    }

    public String getMsgTag() {
        return msgTag;
    }

    public JobRetryMessage setMsgTag(String msgTag) {
        this.msgTag = msgTag;
        return this;
    }

    @Override
    public String toString() {
        return "JobRetryMessage{" +
                "msgId='" + msgId + '\'' +
                ", msgTopic='" + msgTopic + '\'' +
                ", msgTag='" + msgTag + '\'' +
                ", msgRetryProducerGroup='" + msgRetryProducerGroup + '\'' +
                ", msgNameSrvAddr='" + msgNameSrvAddr + '\'' +
                ", msgBody='" + msgBody + '\'' +
                ", msgResendStatusEnum='" + msgResendStatusEnum + '\'' +
                '}';
    }
}
