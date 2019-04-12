package com.snowalker.shield.job.consumer.store.impl;

import com.google.common.base.Preconditions;
import com.snowalker.shield.job.consumer.store.MessageStoreClientTemplate;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 20:01
 * @className MessageStoreRedisTemplate
 * @desc Redis消息存储模板，包装
 */
public class MessageStoreRedisTemplate<K, V> extends RedisTemplate<K, V> implements MessageStoreClientTemplate {

    private RedisTemplate redisTemplate;

    public MessageStoreRedisTemplate(RedisTemplate redisTemplate) {
        Preconditions.checkNotNull(redisTemplate, "RedisTemplate cannot be NULL! Please init an useful redisTemplate instance");
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取RedisTemplate
     * @return
     */
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

}
