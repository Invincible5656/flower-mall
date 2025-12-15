package org.example.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description 代表订单里的一行商品快照
 * @create 2025-12-11 16:09
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    // 2. 【新增】重建构造函数 (用于从数据库恢复，需要接收所有字段)
    // 注意：顺序必须和 Repository 调用时一致
    public OrderItemEntity(Integer id, Integer flowerId, String flowerName, Integer quantity, Float price) {
        this.id = id;
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.quantity = quantity;
        this.price = price;
        // 小计可以重新算，或者如果你数据库里存了 subTotal 也可以传进来
        this.subTotal = (price != null && quantity != null) ? price * quantity : 0f;
    }
}
