package com.snowalker.shield.job.consumer.store;

import com.google.common.base.Preconditions;
import com.snowalker.shield.job.constant.ShieldJobMsgResendStoreTypeEnum;
import com.snowalker.shield.job.consumer.store.impl.JobRetryMessageMySQLHandler;
import com.snowalker.shield.job.consumer.store.impl.JobRetryMessageRedisHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 16:20
 * @className JobRetryMessageHandlerFactory
 * @desc 作业消息重试处理器实现工厂
 */
public class JobRetryMessageHandlerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRetryMessageHandlerFactory.class);

    private final static Map<String, JobRetryMessageHandler> retryMessageHandlerContext =
            new ConcurrentHashMap<>(2);

    /**
     * 从容器获取消息重试处理器实例，不存在则创建并放置到容器中
     * @param resendStoreHandlerType
     * @return
     */
    public static synchronized JobRetryMessageHandler createRetryMessageHandler(ShieldJobMsgResendStoreTypeEnum resendStoreHandlerType) {

        Preconditions.checkNotNull(resendStoreHandlerType, "Parameter resendStoreHandlerType cannot be NULL! Please select an correct ShieldJobMsgResendStoreTypeEnum value");
        LOGGER.debug("resendStoreHandlerType={}", resendStoreHandlerType);
        JobRetryMessageHandler retryMessageHandler;
        // 根据handlerType是否存在于Map中判断Handler实例是否存在
        if (retryMessageHandlerContext.containsKey(resendStoreHandlerType)) {
            retryMessageHandler = retryMessageHandlerContext.get(resendStoreHandlerType);
            LOGGER.debug("retryMessageHandler={}", retryMessageHandler.getClass());
            return retryMessageHandler;
        }
        // 不存在则创建后放置到容器中
        if (ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_REDIS.equals(resendStoreHandlerType)) {
            retryMessageHandler = new JobRetryMessageRedisHandler();
            retryMessageHandlerContext.put(resendStoreHandlerType.toString(), retryMessageHandler);
        } else if (ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_MYSQL.equals(resendStoreHandlerType)) {
            retryMessageHandler = new JobRetryMessageMySQLHandler();
            retryMessageHandlerContext.put(resendStoreHandlerType.toString(), retryMessageHandler);
        } else {
            retryMessageHandler = null;
        }
        LOGGER.debug("retryMessageHandler={}", retryMessageHandler.getClass());
        return retryMessageHandler;
    }

}
