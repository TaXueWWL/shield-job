package com.snowalker.shield.job.consumer.resend;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/11 17:29
 * @className MessageResendExecutor
 * @desc 消息重投递接口
 * 基于Redis的重发：
 *      1. 不需要对重发的数据做清理操作，因为一旦弹出队列就不存在了，只需要在达到重发的最大阈值后入死信即可
 *      2. 不需要标记死信，原因同上，入死信的就是死信
 *
 * 基于MySQL的重发：
 *      1. 需要清理重发的数据，起线程将重发表中的数据清理，同时转移到死信表中（同一本地事务）
 *      2. 不需要标记死信，原因同上，入死信表的就是死信
 */
public interface MessageResendScheduler {

    /**
     * 获取定时调度线程执行器ScheduledExecutorService
     */
    ScheduledExecutorService getResendExecutorService();

    /**
     * 重发核心逻辑
     */
    void doResend();
}
