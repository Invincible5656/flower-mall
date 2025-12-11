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
    //在当前购物车场景下，采取 “懒更新” 或者 “查时更新” 策略。
    //用户点“加入购物车”时，后端去查鲜花表最新价格 -> 更新购物车表的 price 字段，
    //如果商家改价了，用户必须再次触发“加入购物车”或者“修改数量”的操作，购物车里的单价才会变！
    //查时更新业务逻辑，评估之后，发现较复杂就暂时不做这个功能！
    public void addFlower(Integer flowerId, String name, Float price, Integer count) {
        // 1. 内部逻辑：判断有没有重复
        Optional<CartItemEntity> exist = items.stream()
                .filter(i -> i.getFlowerId().equals(flowerId))
                .findFirst();

        if (exist.isPresent()) {
            // 1. 先把找到的这个实体取出来，赋值给一个变量
            CartItemEntity item = exist.get();

            // 2. 对这个具体的实体对象进行操作
            item.addCount(count);
            item.updatePrice(price);
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
