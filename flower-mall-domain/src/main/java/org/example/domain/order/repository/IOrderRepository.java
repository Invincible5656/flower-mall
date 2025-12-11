package org.example.domain.order.repository;

import org.example.domain.order.model.aggregate.OrderAggregate;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 16:47
 */
public interface IOrderRepository {
    void save(OrderAggregate order);

    OrderAggregate findByOrderId(String orderId);
}
