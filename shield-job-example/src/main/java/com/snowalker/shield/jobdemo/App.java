package com.snowalker.shield.jobdemo;

import com.snowalker.shield.job.mapper.OrderEntity;
import com.snowalker.shield.jobdemo.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;
import java.util.UUID;

//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
@ImportResource({"classpath*:applicationContext-mapper.xml"})
@EnableScheduling
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        LOGGER.info("shield-job-server启动完成......");

        OrderService orderService = context.getBean("orderService", OrderService.class);
        String purseId = "1c52616b3e204c7e92e1d4f924e95ba7";
        String userName = "SnoWalker_1c52616b3e";
        String chargeMoney = "100.000";
        String orderId = UUID.randomUUID().toString().replace("-", "");
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderId)
                .setUserName(userName)
                .setPurseId(purseId)
                .setChargeMoney(new BigDecimal(chargeMoney).setScale(3));
        orderService.insertOrder(orderEntity);
    }
}
