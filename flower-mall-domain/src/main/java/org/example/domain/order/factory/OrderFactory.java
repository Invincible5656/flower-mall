package org.example.domain.order.factory;

import org.example.domain.cart.model.aggregate.CartAggregate;
import org.example.domain.order.model.aggregate.OrderAggregate;
import org.example.domain.order.model.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 16:34
 */
@Component
public class OrderFactory {
    public OrderAggregate createOrder(Integer userId, CartAggregate cart,
                                      String receiverName, String receiverPhone, String address) {

        // 1. 业务校验
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("购物车为空，不能下单");
        }

        // 2. 数据转换: CartItem -> OrderItem
        List<OrderItemEntity> orderItems = cart.getItems().stream()
                .map(cartItem -> new OrderItemEntity(
                        cartItem.getFlowerId(),
                        cartItem.getFlowerName(),
                        cartItem.getPrice(),
                        cartItem.getAmount()
                )).collect(Collectors.toList());

        // 3. 计算总价
        Float totalAmount = cart.calculateTotalPrice();

        // 4. 使用 Builder 创建聚合根
        return new OrderAggregate.Builder(userId, totalAmount, orderItems)
                .shippingInfo(receiverName, receiverPhone, address)
                .build();
    }

}
