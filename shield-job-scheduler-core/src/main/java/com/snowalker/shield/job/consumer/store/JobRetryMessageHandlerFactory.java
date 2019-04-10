package com.snowalker.shield.job.consumer.store;

import com.snowalker.shield.job.constant.ShieldJobMsgResendStoreTypeEnum;
import com.snowalker.shield.job.consumer.store.impl.JobRetryMessageMySQLHandler;
import com.snowalker.shield.job.consumer.store.impl.JobRetryMessageRedisHandler;

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

    private final static Map<String, JobRetryMessageHandler> retryMessageHandlerContext =
            new ConcurrentHashMap<>(2);

    /**
     * 从容器获取消息重试处理器实例，不存在则创建并放置到容器中
     * @param handlerType
     * @return
     */
    public static synchronized JobRetryMessageHandler createRetryMessageHandler(String handlerType) {

        JobRetryMessageHandler retryMessageHandler;
        // 根据handlerType是否存在于Map中判断Handler实例是否存在
        if (retryMessageHandlerContext.containsKey(handlerType)) {
            retryMessageHandler = retryMessageHandlerContext.get(handlerType);
            return retryMessageHandler;
        }
        // 不存在则创建后放置到容器中
        if (ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_REDIS.equals(handlerType)) {
            retryMessageHandler = new JobRetryMessageRedisHandler();
            retryMessageHandlerContext.put(handlerType, retryMessageHandler);
        } else if (ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_MYSQL.equals(handlerType)) {
            retryMessageHandler = new JobRetryMessageMySQLHandler();
            retryMessageHandlerContext.put(handlerType, retryMessageHandler);
        } else {
            retryMessageHandler = null;
        }
        return retryMessageHandler;
    }
}
