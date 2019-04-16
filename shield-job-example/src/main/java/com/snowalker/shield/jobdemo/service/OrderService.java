package com.snowalker.shield.jobdemo.service;

import com.snowalker.shield.job.mapper.OrderEntity;
import com.snowalker.shield.job.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/2/17 19:29
 * @className OrderService
 * @desc 订单业务类
 */
@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    OrderMapper orderMapper;

    /**
     * 模拟下单
     * 发送钱包支付普通消息
     * 此处只是为了演示如何发送普通消息，在实际的业务中，
     * 应当保证订单状态修改与消息发送同时成功，同时失败
     * 也就是该逻辑在实际开发中应当使用事务消息
     * @param orderEntity
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertOrder(OrderEntity orderEntity) {
        try {
            orderMapper.insertOrder(orderEntity);
        } catch (Exception e) {
            LOGGER.error("订单插入异常,purseId={},orderId={},e={}", orderEntity.getPurseId(), orderEntity.getOrderId(), e);
            throw e;
        }
    }

    /**
     * 修改订单状态处理中
     * @param orderEntity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderDealing(OrderEntity orderEntity) {
        try {
            int count = orderMapper.updateOrderDealing(orderEntity);
            if (count == 1) {
                LOGGER.info("订单状态修改为处理中成功,orderId={}", orderEntity.getOrderId());
                return true;
            } else {
                throw new RuntimeException("修改订单状态失败,orderId={}" + orderEntity.getOrderId());
            }
        } catch (Exception e) {
            LOGGER.error("订单状态修改为处理中异常,orderId={},e={}", orderEntity.getOrderId(), e);
            throw e;
        }
    }

    /**
     * 订单状态改成功，订单处理中改成功，支付改成功
     * @param orderEntity
     * @return
     */
    public boolean updateOrderSuccess(OrderEntity orderEntity) {
        try {
            int count = orderMapper.updateOrderSuccess(orderEntity);
            if (count == 1) {
                LOGGER.info("[订单状态改成功]完成,orderId={}", orderEntity.getOrderId());
                return true;
            } else {
                throw new RuntimeException("[订单状态改成功]失败,orderId={}" + orderEntity.getOrderId());
            }
        } catch (Exception e) {
            LOGGER.error("[订单状态改成功]异常,orderId={},e={}", orderEntity.getOrderId(), e);
            throw e;
        }
    }

}
