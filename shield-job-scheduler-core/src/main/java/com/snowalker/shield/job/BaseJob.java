package com.snowalker.shield.job;

import java.util.Map;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 10:30
 * @className Job
 * @desc 作业实体
 */
public abstract class BaseJob {

    /**版本号，默认1.0*/
    private String version = "1.0";
    /**主题名*/
    private String jobTopic;
    /**标签名，标识当前的业务类型*/
    private String jobTag;
    /**业务描述*/
    private String jobBizDesc;
    /**生产者组*/
    private String jobProducerGroup;
    /**消费者组*/
    private String jobConsumerGroup;
    /**上下文TraceId*/
    private String jobTraceId;
    /**消息id*/
    private String jobMsgId;

    private Map<String, Object> params;

    public abstract String encode();

    public abstract void decode(String msg);

    public String getVersion() {
        return version;
    }

    public BaseJob setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getJobTopic() {
        return jobTopic;
    }

    public BaseJob setJobTopic(String jobTopic) {
        this.jobTopic = jobTopic;
        return this;
    }

    public String getJobTag() {
        return jobTag;
    }

    public BaseJob setJobTag(String jobTag) {
        this.jobTag = jobTag;
        return this;
    }

    public String getJobBizDesc() {
        return jobBizDesc;
    }

    public BaseJob setJobBizDesc(String jobBizDesc) {
        this.jobBizDesc = jobBizDesc;
        return this;
    }

    public String getJobProducerGroup() {
        return jobProducerGroup;
    }

    public BaseJob setJobProducerGroup(String jobProducerGroup) {
        this.jobProducerGroup = jobProducerGroup;
        return this;
    }

    public String getJobConsumerGroup() {
        return jobConsumerGroup;
    }

    public BaseJob setJobConsumerGroup(String jobConsumerGroup) {
        this.jobConsumerGroup = jobConsumerGroup;
        return this;
    }

    public String getJobTraceId() {
        return jobTraceId;
    }

    public BaseJob setJobTraceId(String jobTraceId) {
        this.jobTraceId = jobTraceId;
        return this;
    }

    public String getJobMsgId() {
        return jobMsgId;
    }

    public BaseJob setJobMsgId(String jobMsgId) {
        this.jobMsgId = jobMsgId;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public BaseJob setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    @Override
    public String toString() {
        return "BaseJob{" +
                "version='" + version + '\'' +
                ", jobTopic='" + jobTopic + '\'' +
                ", jobTag='" + jobTag + '\'' +
                ", jobBizDesc='" + jobBizDesc + '\'' +
                ", jobProducerGroup='" + jobProducerGroup + '\'' +
                ", jobConsumerGroup='" + jobConsumerGroup + '\'' +
                ", jobTraceId='" + jobTraceId + '\'' +
                ", jobMsgId='" + jobMsgId + '\'' +
                ", params=" + params +
                '}';
    }
}
