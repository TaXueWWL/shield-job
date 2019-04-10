package com.snowalker.shield.job;

import com.snowalker.shield.job.constant.ShieldJobMsgResendStoreTypeEnum;
import com.snowalker.shield.job.consumer.store.JobRetryMessageHandlerFactory;
import org.junit.Test;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 14:34
 * @className ShieldJobTest
 * @desc
 */
public class ShieldJobTest {

    /**
     * 测试任务重发处理器工厂
     */
    @Test
    public void testJobRetryMessageHandlerFactory() {
        ShieldJobMsgResendStoreTypeEnum redis = ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_REDIS;

        JobRetryMessageHandlerFactory.createRetryMessageHandler(redis);

        ShieldJobMsgResendStoreTypeEnum mysql = ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_MYSQL;

        JobRetryMessageHandlerFactory.createRetryMessageHandler(mysql);

        JobRetryMessageHandlerFactory.createRetryMessageHandler(null);
    }
}
