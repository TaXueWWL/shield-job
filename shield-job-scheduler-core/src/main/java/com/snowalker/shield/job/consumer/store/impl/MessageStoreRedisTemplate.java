package com.snowalker.shield.job.consumer.store.impl;

import com.snowalker.shield.job.consumer.store.MessageStoreClientTemplate;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 20:01
 * @className MessageStoreRedisTemplate
 * @desc Redis消息存储模板，包装
 */
public class MessageStoreRedisTemplate extends RedisTemplate implements MessageStoreClientTemplate {

    private RedisTemplate redisTemplate;

    public MessageStoreRedisTemplate() {
    }

    public MessageStoreRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public MessageStoreRedisTemplate setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        return this;
    }
}
