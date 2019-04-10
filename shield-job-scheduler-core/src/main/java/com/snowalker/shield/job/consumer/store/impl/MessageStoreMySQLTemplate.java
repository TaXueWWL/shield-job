package com.snowalker.shield.job.consumer.store.impl;

import com.snowalker.shield.job.consumer.store.MessageStoreClientTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/10 20:02
 * @className MessageStoreMySQLTemplate
 * @desc MySQL消息存储模板，包装JdbcTemplate
 */
public class MessageStoreMySQLTemplate extends JdbcTemplate implements MessageStoreClientTemplate {

    private JdbcTemplate jdbcTemplate;

    public MessageStoreMySQLTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MessageStoreMySQLTemplate() {
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public MessageStoreMySQLTemplate setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        return this;
    }
}
