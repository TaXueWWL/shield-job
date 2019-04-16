package com.snowalker.shield.job.standalone;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/16 9:26
 * @className JobScheduleConsumerListener
 * @desc 调度监听器单机模式
 */
public interface JobScheduleConsumerListener<T> {


    /**
     * 消费方法
     * @param t
     * @return
     */
    Object consume(T t);
}
