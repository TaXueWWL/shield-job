package com.snowalker.shield.job.mapper;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/2/17 19:01
 * @className OrderMapper
 * @desc 订单Mapper
 */
public interface OrderMapper {

    /***
     * 下单
     * @param orderEntity
     */
    void insertOrder(OrderEntity orderEntity);

    /**
     * 订单状态修改为处理中
     * @param orderEntity
     */
    int updateOrderDealing(OrderEntity orderEntity);

    /**
     * 订单状态修改为成功
     * @param orderEntity
     */
    int updateOrderSuccess(OrderEntity orderEntity);
}
