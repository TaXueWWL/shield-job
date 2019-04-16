package com.snowalker.shield.job;

import com.snowalker.shield.job.consumer.store.JobRetryMessage;
import com.snowalker.shield.job.standalone.AbstractJobScheduleStandaloneHandler;
import com.snowalker.shield.job.standalone.JobScheduleConsumerListener;
import com.snowalker.shield.job.standalone.JobScheduleProducerListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 14:34
 * @className ShieldJobTest
 * @desc
 */
public class ShieldJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger("COMMON-APPENDER");

    /**
     * 测试任务重发处理器工厂
     */
    @Test
    public void testJobRetryMessageHandlerFactory() {
    }

    /**
     * 测试解码重发消息体
     */
    @Test
    public void testDecodeRetryMsg() {
        String decodeMsgString = "{\"msgId\":\"AC1E53471E602437C6DC365C04030018\",\"msgTopic\":\"SNOWALKER_TEST_SHIELD_JOB_TOPIC\",\"msgTag\":\"SNOWALKER_TEST_SHIELD_JOB_TAG\",\"msgRetryProducerGroup\":\"PID_MSG_RESEND_SNOWALKER_TEST_SHIELD_JOB_TOPIC\",\"msgNameSrvAddr\":\"172.30.66.50:9876;172.30.66.51:9876\",\"msgBody\":\"{\\\"header\\\":{\\\"version\\\":\\\"1.0\\\",\\\"jobTopic\\\":\\\"SNOWALKER_TEST_SHIELD_JOB_TOPIC\\\",\\\"jobTag\\\":\\\"SNOWALKER_TEST_SHIELD_JOB_TAG\\\",\\\"jobBizDesc\\\":\\\"测试订单协议\\\",\\\"jobProducerGroup\\\":\\\"PID_SNOWALKER_TEST_SHIELD_JOB\\\",\\\"jobConsumerGroup\\\":\\\"CID_SNOWALKER_TEST_SHIELD_JOB\\\",\\\"jobTraceId\\\":\\\"TRACE_093008b3-b316-40a8-9130-aaa2c4d2fb4b\\\"},\\\"body\\\":{\\\"userId\\\":\\\"SNOWALKER_377cd341-29cd-487e-91a6-ece9bd61e629\\\",\\\"userName\\\":\\\"SNOWALKER_0\\\",\\\"orderId\\\":\\\"OD_aff7c099-05b0-4384-83ae-b9974c73ed07\\\"}}\",\"msgResendStatusEnum\":\"MSG_RESEND_STATUS_RECONSUMELATER\"}";
        JobRetryMessage jobRetryMessage = new JobRetryMessage();
        jobRetryMessage.decode(decodeMsgString);
        LOGGER.info(jobRetryMessage.toString());
        LOGGER.info("msgBody={}", jobRetryMessage.getMsgBody());
    }

    @Test
    public void testStandaloneHandler() {
        MyStandaloneScheduleHandler scheduleHandler = new MyStandaloneScheduleHandler();
        // 实现方式1.客户端实现调度策略
        scheduleHandler.execute();
        // 实现方式2.使用oneWay调度策略
        scheduleHandler.executeOneway(new JobScheduleProducerListener<String>() {
            @Override
            public List<String> produce(Object arg) {
                final List<String> strings2 = new ArrayList<>(10);
                for (int i = 0; i < 10; i++) {
                    String str = "snowalker-----" + i;
                    strings2.add(str);
                }
                return strings2;
            }
        }, new JobScheduleConsumerListener<String>() {
            @Override
            public Object consume(String s) {
                System.out.println("consuming start!!!! s = " + s);
                return null;
            }
        });
    }

    class MyStandaloneScheduleHandler extends AbstractJobScheduleStandaloneHandler<String> {

        @Override
        public void execute() {
            // 生产
            List<String> list = super.produce(new JobScheduleProducerListener<String>() {
                @Override
                public List<String> produce(Object arg) {
                    final List<String> strings2 = new ArrayList<>(10);
                    for (int i = 0; i < 10; i++) {
                        String str = "snowalker-----" + i;
                        strings2.add(str);
                    }
                    return strings2;
                }
            }, null);

            // 消费
            super.consume(new JobScheduleConsumerListener<String>() {
                @Override
                public Object consume(String s) {
                    System.out.println("consuming start!!!! s = " + s);
                    return null;
                }
            }, list);
        }
    }
}
