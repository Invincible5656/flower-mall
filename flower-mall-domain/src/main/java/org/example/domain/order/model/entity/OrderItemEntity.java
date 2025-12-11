package org.example.domain.order.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description 代表订单里的一行商品快照
 * @create 2025-12-11 16:09
 */
@Getter
@NoArgsConstructor
public class OrderItemEntity {
    // 数据库自增ID，在创建时为null
    private Integer id;

    // 业务属性 (快照)
    private Integer flowerId;
    private String flowerName;
    private Float price;
    private Integer quantity;
    private Float subTotal;

    // 构造函数，用于工厂创建时
    public OrderItemEntity(Integer flowerId, String flowerName, Float price, Integer quantity) {
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.price = price;
        this.quantity = quantity;
        this.subTotal = price * quantity; // 小计在创建时直接算好
    }
}
