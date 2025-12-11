package org.example.domain.cart.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.cart.model.entity.CartItemEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author lanjiajun
 * @description
 * 聚合根，对外代表整个购物车。
 * 外部只能通过它操作购物车里面的Item
 * 聚合 (Aggregate)：为了完成一个完整业务功能捆绑在一起的一组对象
 * （如 Cart 包裹 List<CartItem>）。
 * @create 2025-12-11 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartAggregate {

    private Integer userId;
    private List<CartItemEntity> items;

    // ============对外暴露的业务方法============

    public void addFlower(Integer flowerId, String name, Float price, Integer count) {
        // 1. 内部逻辑：判断有没有重复
        Optional<CartItemEntity> exist = items.stream()
                .filter(i -> i.getFlowerId().equals(flowerId))
                .findFirst();

        if (exist.isPresent()) {
            // 2. 调度内部实体干活
            exist.get().addCount(count);
        } else {
            // 3. 创建新实体加入聚合
            CartItemEntity newItem = new CartItemEntity(flowerId, name, count, price);
            items.add(newItem);
        }
    }

    // 计算整个聚合的总价
    public Float calculateTotalPrice() {
        return items.stream()
                .map(CartItemEntity::getSubTotal)
                .reduce(0f, Float::sum);
    }
}
