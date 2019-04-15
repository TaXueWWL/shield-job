package com.snowalker.shield.job.producer.listener;

import com.snowalker.shield.job.BaseJob;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/8 10:22
 * @className JobListener
 * @desc 作业生产者接口
 */
public interface JobProducerListener<T extends BaseJob> {

    /**
     * 作业生产
     * @param arg 需要透传的业务参数
     * @return
     */
    List<T> produce(final Object arg);
}
