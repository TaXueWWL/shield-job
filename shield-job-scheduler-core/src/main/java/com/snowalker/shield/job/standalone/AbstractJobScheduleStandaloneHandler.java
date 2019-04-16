package com.snowalker.shield.job.standalone;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/16 9:24
 * @className AbstractJobScheduleHandler
 * @desc 抽象单机模式调度处理器
 */
public abstract class AbstractJobScheduleStandaloneHandler<T> {

    /**
     * 调度生产
     * @param jobScheduleStandaloneListener
     * @return
     */
    public List<T> produce(JobScheduleProducerListener<T> jobScheduleStandaloneListener, Object arg) {
        return jobScheduleStandaloneListener.produce(arg);
    }

    /**
     * 调度消费
     * @param jobScheduleConsumerListener
     */
    public void consume(JobScheduleConsumerListener<T> jobScheduleConsumerListener, List<T> list) {
        list.stream().forEach((T t) -> {
            jobScheduleConsumerListener.consume(t);
        });
    }

    /**
     * 客户端实现具体的调度逻辑
     */
    public abstract void execute();

    /**
     * 默认调度逻辑
     * @param jobScheduleStandaloneListener
     * @param jobScheduleConsumerListener
     */
    public void executeOneway(JobScheduleProducerListener<T> jobScheduleStandaloneListener,
                    JobScheduleConsumerListener<T> jobScheduleConsumerListener) {
        this.produce(jobScheduleStandaloneListener, null).stream().forEach((T t) -> {
            jobScheduleConsumerListener.consume(t);
        });
    }

    /**
     * 默认调度逻辑
     * @param jobScheduleConsumerListener
     */
    public void executeOneway(JobScheduleProducerListener<T> jobScheduleStandaloneListener,
                        JobScheduleConsumerListener<T> jobScheduleConsumerListener,
                        Object arg) {
        this.produce(jobScheduleStandaloneListener, arg).stream().forEach((T t) -> {
            jobScheduleConsumerListener.consume(t);
        });
    }
}
