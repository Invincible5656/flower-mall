package org.example.domain.cart.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * 负责具体的活
 * 聚合包含实体;
 * 聚合根是老大，内部实体是小弟。
 * @create 2025-12-11 10:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemEntity {
    private Integer id; //记录ID

    private Integer flowerId;

    private String flowerName;

    private Integer amount;

    private Float price;

    public CartItemEntity(Integer flowerId, String flowerName, Integer amount, Float price) {
        this.id = null; // 显式置空，或者不写也行，默认就是 null
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.amount = amount;
        this.price = price;
    }

    // 2. 有行为
    // 增加数量
    public void addCount(Integer count) {
        this.amount += count;
    }

    // 计算本行小计
    public Float getSubTotal() {
        return this.price * this.amount;
    }

    // 初始化ID（用于存库后回填）
    public void initId(Integer id) {
        this.id = id;
    }

    public void updatePrice(Float newPrice) {
        this.price = newPrice;
    }
}
