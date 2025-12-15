package org.example.domain.order.repository;

import org.example.domain.order.model.aggregate.OrderAggregate;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 16:47
 */
public interface IOrderRepository {
    void save(OrderAggregate order);

    OrderAggregate findByOrderId(String orderId);

    // 【新增】根据用户ID查询订单列表
    List<OrderAggregate> findByUserId(Integer userId);

    // 【新增】查询所有订单 (管理员用)
    List<OrderAggregate> findAll();

    Integer countOrders();

    BigDecimal sumTotalSales(List<Integer> list);
}
