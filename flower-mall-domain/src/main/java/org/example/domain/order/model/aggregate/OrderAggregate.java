package org.example.domain.order.model.aggregate;

import lombok.Getter;
import org.example.domain.order.model.entity.OrderItemEntity;
import org.example.domain.order.model.valobj.OrderStatusVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author lanjiajun
 * @description
 * 创建订单是一个复杂的过程（需要算钱、校验），不适合直接 new OrderAggregate()
 * 用一个工厂模式来封装这个创建过程。
 * @create 2025-12-11 16:09
 */
@Getter
public class OrderAggregate {
    // 聚合根标识
    private Long id; // 数据库自增ID
    private String orderId; // 业务订单号

    // 业务属性
    private Integer userId;
    private Float totalAmount;
    private OrderStatusVO status; // 使用值对象，而不是简单的 int
    private List<OrderItemEntity> items;

    // 收货信息
    private String receiverName;
    private String receiverPhone;
    private String address;

    // 时间戳
    private LocalDateTime createTime;

    // 构造函数设为 private，强制通过工厂创建
    private OrderAggregate() {}

    public static OrderAggregate reconstruct(
            Long id, String orderId, Integer userId, Float totalAmount,
            OrderStatusVO status, List<OrderItemEntity> items,
            String receiverName, String receiverPhone, String address,
            LocalDateTime createTime
    ) {
        OrderAggregate order = new OrderAggregate();
        // 这里只是单纯地赋值，没有任何业务计算
        order.id = id;
        order.orderId = orderId;
        order.userId = userId;
        order.totalAmount = totalAmount;
        order.status = status;
        order.items = items;
        order.receiverName = receiverName;
        order.receiverPhone = receiverPhone;
        order.address = address;
        order.createTime = createTime;

        return order;
    }
    // ================== 业务行为 ==================

    /**
     * 支付成功
     */
    public void paySuccess() {
        if (this.status != OrderStatusVO.WAIT_PAY) {
            throw new RuntimeException("订单状态异常，无法支付");
        }
        this.status = OrderStatusVO.PAID;
    }

    /**
     * 发货
     */
    public void ship() {
        if (this.status != OrderStatusVO.PAID) {
            throw new RuntimeException("订单未支付，无法发货");
        }
        this.status = OrderStatusVO.SHIPPED;
    }

    // 内部构建器，用于工厂模式
    public static class Builder {
        private final OrderAggregate order = new OrderAggregate();

        public Builder(Integer userId, Float totalAmount, List<OrderItemEntity> items) {
            order.orderId = UUID.randomUUID().toString(); // 简单生成唯一ID
            order.userId = userId;
            order.totalAmount = totalAmount;
            order.items = items;
            order.status = OrderStatusVO.WAIT_PAY; // 初始状态
            order.createTime = LocalDateTime.now();        }

        public Builder shippingInfo(String name, String phone, String address) {
            order.receiverName = name;
            order.receiverPhone = phone;
            order.address = address;
            return this;
        }

        public OrderAggregate build() {
            return order;
        }
    }

}
