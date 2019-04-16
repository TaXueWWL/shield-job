package com.snowalker.shield.job.standalone;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/16 9:26
 * @className JobScheduleProducerListener
 * @desc 调度监听器单机模式
 */
public interface JobScheduleProducerListener<T> {

    /**
     * 调度列表生产
     * @param arg
     * @return
     */
    List<T> produce(final Object arg);

}
