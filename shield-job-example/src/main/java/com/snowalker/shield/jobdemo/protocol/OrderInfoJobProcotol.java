package com.snowalker.shield.jobdemo.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.snowalker.shield.job.BaseJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/9 9:29
 * @className OrderInfoJob
 * @desc 测试订单任务协议
 */
public class OrderInfoJobProcotol extends BaseJob implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInfoJobProcotol.class);

    private static final long serialVersionUID = -593312377579834842L;

    private String userId;
    private String userName;
    private String orderId;

    private Map<String, String> header;
    private Map<String, String> body;

    @Override
    public String encode() {
        // 组装消息协议头
        ImmutableMap.Builder headerBuilder = new ImmutableMap.Builder<String, String>()
                .put("version", this.getVersion())
                .put("jobTopic", this.getJobTopic())
                .put("jobTag", this.getJobTag())
                .put("jobBizDesc", "测试订单协议")
                .put("jobProducerGroup", this.getJobProducerGroup())
                .put("jobConsumerGroup", this.getJobConsumerGroup())
                .put("jobTraceId", this.getJobTraceId());
        header = headerBuilder.build();

        body = new ImmutableMap.Builder<String, String>()
                .put("userId", this.getUserId())
                .put("userName", this.getUserName())
                .put("orderId", this.getOrderId())
                .build();

        ImmutableMap<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("header", header)
                .put("body", body)
                .build();
        // 返回序列化消息Json串
        String ret_string = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ret_string = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            LOGGER.error("消息序列化json异常:", e);
        }
        return ret_string;
    }

    @Override
    public void decode(String msg) {
        Preconditions.checkNotNull(msg);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(msg);
            // header
            this.setVersion(root.get("header").get("version").asText());
            this.setJobTopic(root.get("header").get("jobTopic").asText());
            this.setJobTag(root.get("header").get("jobTag").asText());
            this.setJobBizDesc(root.get("header").get("jobBizDesc").asText());
            this.setJobProducerGroup(root.get("header").get("jobProducerGroup").asText());
            this.setJobConsumerGroup(root.get("header").get("jobConsumerGroup").asText());
            this.setJobTraceId(root.get("header").get("jobTraceId").asText());
            // body
            this.setUserName(root.get("body").get("userName").asText());
            this.setOrderId(root.get("body").get("orderId").asText());
            this.setUserId(root.get("body").get("userId").asText());
        } catch (IOException e) {
            LOGGER.error("反序列化消息异常:", e);
        }
    }

    public String getUserId() {
        return userId;
    }

    public OrderInfoJobProcotol setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public OrderInfoJobProcotol setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderInfoJobProcotol setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }
}
