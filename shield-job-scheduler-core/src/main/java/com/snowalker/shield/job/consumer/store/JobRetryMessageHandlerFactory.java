package com.snowalker.shield.job.consumer.store;

import com.google.common.base.Preconditions;
import com.snowalker.shield.job.constant.ShieldJobMsgResendStoreTypeEnum;
import com.snowalker.shield.job.consumer.store.impl.JobRetryMessageMySQLHandler;
import com.snowalker.shield.job.consumer.store.impl.JobRetryMessageRedisHandler;
import com.snowalker.shield.job.consumer.store.impl.MessageStoreMySQLTemplate;
import com.snowalker.shield.job.consumer.store.impl.MessageStoreRedisTemplate;
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

    private MessageStoreClientTemplate messageStoreClientTemplate;

    private MessageStoreRedisTemplate messageStoreRedisTemplate;

    private MessageStoreMySQLTemplate messageStoreMySQLTemplate;

    private JobRetryMessageHandlerFactory() {}

    private static JobRetryMessageHandlerFactory factory = null;

    public static JobRetryMessageHandlerFactory getInstance() {
        if(factory == null) {
            synchronized (Object.class) {
                if (factory == null) {
                    factory = new JobRetryMessageHandlerFactory();
                }
            }
        }
        return factory;
    }

    /**
     * 从容器获取消息重试处理器实例，不存在则创建并放置到容器中
     * @param resendStoreHandlerType
     * @return
     */
    public synchronized JobRetryMessageHandler createRetryMessageHandler(ShieldJobMsgResendStoreTypeEnum resendStoreHandlerType) {

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
            retryMessageHandler = new JobRetryMessageRedisHandler(this.messageStoreRedisTemplate);
            retryMessageHandlerContext.put(resendStoreHandlerType.toString(), retryMessageHandler);
        } else if (ShieldJobMsgResendStoreTypeEnum.MSG_STORE_TYPE_MYSQL.equals(resendStoreHandlerType)) {
            retryMessageHandler = new JobRetryMessageMySQLHandler(this.messageStoreMySQLTemplate);
            retryMessageHandlerContext.put(resendStoreHandlerType.toString(), retryMessageHandler);
        } else {
            retryMessageHandler = null;
        }
        LOGGER.debug("retryMessageHandler={}", retryMessageHandler.getClass());
        return retryMessageHandler;
    }

    public MessageStoreRedisTemplate getMessageStoreRedisTemplate() {
        return messageStoreRedisTemplate;
    }

    public JobRetryMessageHandlerFactory setMessageStoreRedisTemplate(MessageStoreRedisTemplate messageStoreRedisTemplate) {
        this.messageStoreRedisTemplate = messageStoreRedisTemplate;
        return this;
    }

    public MessageStoreMySQLTemplate getMessageStoreMySQLTemplate() {
        return messageStoreMySQLTemplate;
    }

    public JobRetryMessageHandlerFactory setMessageStoreMySQLTemplate(MessageStoreMySQLTemplate messageStoreMySQLTemplate) {
        this.messageStoreMySQLTemplate = messageStoreMySQLTemplate;
        return this;
    }

    public MessageStoreClientTemplate getMessageStoreClientTemplate() {
        return messageStoreClientTemplate;
    }

    public JobRetryMessageHandlerFactory setMessageStoreClientTemplate(MessageStoreClientTemplate messageStoreClientTemplate) {
        this.messageStoreClientTemplate = messageStoreClientTemplate;
        return this;
    }


}
