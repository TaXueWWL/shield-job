package com.snowalker.shield.jobdemo.producer;

import com.snowalker.shield.job.JobSendResult;
import com.snowalker.shield.job.Result;
import com.snowalker.shield.job.producer.JobProducerExecutor;
import com.snowalker.shield.job.producer.RocketMQProducerProperty;
import com.snowalker.shield.job.producer.listener.JobProducerListener;
import com.snowalker.shield.jobdemo.protocol.OrderInfoJobProcotol;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/9 9:49
 * @className OrderInfoJobProducer
 * @desc 测试订单生产者
 */
@Component
public class OrderInfoJobProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInfoJobProducer.class);

    /**
     * 测试订单任务TOPIC
     */
    private static final String TOPIC = "SNOWALKER_TEST_SHIELD_JOB_TOPIC";
    /**
     * 测试订单任务TAG
     */
    private static final String TAG = "SNOWALKER_TEST_SHIELD_JOB_TAG";
    /**
     * 测试订单生产者组
     */
    private static final String PRODUCER_GROUP = "PID_SNOWALKER_TEST_SHIELD_JOB";
    /**
     * 测试订单消费者组
     */
    private static final String CONSUMER_GROUP = "CID_SNOWALKER_TEST_SHIELD_JOB";

    @Value("${rocketmq.nameServer}")
    private String nameSrvAddr;

    private JobProducerExecutor jobProducerExecutor;

    @PostConstruct
    public void init() throws MQClientException {
        // 实例化作业生产调度器
        jobProducerExecutor = new JobProducerExecutor()
                .init(new RocketMQProducerProperty(PRODUCER_GROUP,nameSrvAddr));
        jobProducerExecutor.getProducer().start();
    }

    @Scheduled(cron = "${order.resend.cron}")
    public void execute() {
        try {
            // 传入JobProducerListener实现类，返回作业实体
            Result<JobSendResult> jobSendResult = jobProducerExecutor.execute(
                    (JobProducerListener<OrderInfoJobProcotol>) arg -> {
                        List<OrderInfoJobProcotol> jobs = new ArrayList<>(10);
                        for (int i = 0; i < 100; i++) {
                            OrderInfoJobProcotol orderInfoJobProcotol = new OrderInfoJobProcotol();
                            orderInfoJobProcotol.setOrderId("OD_" + UUID.randomUUID().toString())
                                    .setUserId("SNOWALKER_" + UUID.randomUUID().toString())
                                    .setUserName("SNOWALKER_" + i)
                                    .setJobTraceId("TRACE_" + UUID.randomUUID().toString())
                                    .setJobTopic(TOPIC)
                                    .setJobTag(TAG)
                                    .setJobProducerGroup(PRODUCER_GROUP)
                                    .setJobConsumerGroup(CONSUMER_GROUP)
                            ;
                            jobs.add(orderInfoJobProcotol);
                        }
                        return jobs;
                    }, null);




            if (jobSendResult == null) {
                LOGGER.warn("执行作业分发失败,返回为空,topic={}", TOPIC);
            }
            if (jobSendResult.isSuccess()) {
                LOGGER.info("执行作业分发成功,jobSendResult={}", jobSendResult.toString());
            }
        } catch (Exception e) {
            LOGGER.error("执行作业分发异常", e);
        }
    }

    private void withoutLambda() {
        Result<JobSendResult> jobSendResult2 = jobProducerExecutor.execute(
                (new JobProducerListener() {
                    @Override
                    public List produce(Object arg) {
                        List<OrderInfoJobProcotol> jobs = new ArrayList<>(10);
                        for (int i = 0; i < 1; i++) {
                            OrderInfoJobProcotol orderInfoJobProcotol = new OrderInfoJobProcotol();
                            orderInfoJobProcotol.setOrderId("OD_" + UUID.randomUUID().toString())
                                    .setUserId("SNOWALKER_" + UUID.randomUUID().toString())
                                    .setUserName("SNOWALKER_" + i)
                                    .setJobTraceId("TRACE_" + UUID.randomUUID().toString())
                                    .setJobTopic(TOPIC)
                                    .setJobTag(TAG)
                                    .setJobProducerGroup(PRODUCER_GROUP)
                                    .setJobConsumerGroup(CONSUMER_GROUP)
                            ;
                            jobs.add(orderInfoJobProcotol);
                        }
                        return jobs;
                    }
                }), null);
    }
}
