package com.snowalker.shield.jobdemo.producer.standalone;

import com.snowalker.shield.job.producer.JobProducerExecutor;
import com.snowalker.shield.job.standalone.AbstractJobScheduleStandaloneHandler;
import com.snowalker.shield.job.standalone.JobScheduleConsumerListener;
import com.snowalker.shield.job.standalone.JobScheduleProducerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/4/9 9:49
 * @className OrderInfoJobProducer
 * @desc 单机模式调度执行器
 */
@Component
public class OrderInfoJobProducerStandalone extends AbstractJobScheduleStandaloneHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInfoJobProducerStandalone.class);

    private JobProducerExecutor jobProducerExecutor;

    @PostConstruct
    public void init() {
        LOGGER.info("单机模式调度执行开......");
    }

//    @Scheduled(cron = "${order.standalone.cron}")
    public void execute() {
        executeOneway(new JobScheduleProducerListener<String>() {
            @Override
            public List<String> produce(Object arg) {
                LOGGER.info("单机模式作业生产开始...");
                final List<String> strings2 = new ArrayList<>(10);
                for (int i = 0; i < 10; i++) {
                    String str = "executeOneway---snowalker-----" + i;
                    strings2.add(str);
                }
                LOGGER.info("单机模式作业生产结束...");
                return strings2;
            }
        }, new JobScheduleConsumerListener<String>() {
            @Override
            public Object consume(String s) {
                LOGGER.info("executeOneway---consuming start!!!! s = " + s);
                return null;
            }
        });
    }

}
