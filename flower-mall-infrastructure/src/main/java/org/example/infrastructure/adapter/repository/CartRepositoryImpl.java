package org.example.infrastructure.adapter.repository;

import org.example.domain.cart.model.aggregate.CartAggregate;
import org.example.domain.cart.model.entity.CartItemEntity;
import org.example.domain.cart.repository.ICartRepository;
import org.example.infrastructure.dao.ICartDao;
import org.example.infrastructure.dao.po.Cart;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 12:52
 */
@Repository
public class CartRepositoryImpl implements ICartRepository {

    @Resource
    private ICartDao cartDao;

    // 作用：把“聚合根对象”拆解，根据每一项的状态，决定是 Insert 还是 Update
    @Override
    public void save(CartAggregate cartAggregate) {
        // 获取聚合根里的所有实体
        List<CartItemEntity> items = cartAggregate.getItems();
        Integer userId = cartAggregate.getUserId();

        // 循环处理每一项
        for (CartItemEntity item : items) {
            // 1. 实体转 PO
            Cart po = toPO(item, userId);

            // 2. 关键逻辑：根据是否有 ID 判断是新增还是更新
            if (item.getId() == null) {
                // --- 新增逻辑 ---
                cartDao.insert(po);

                // ！！关键步骤！！
                // 数据库生成了 ID (比如 101)，必须回填给 Domain 实体
                // 否则这一次请求还没结束，业务层如果再用这个实体，ID 还是 null
                item.initId(po.getId());
            } else {
                // --- 更新逻辑 ---
                cartDao.updateItem(po);
            }
        }
    }

    // 把数据库里的“多行数据”，组装成一个“聚合根对象”
    @Override
    public CartAggregate findByUserId(Integer userId) {
        // 1. 从数据库查出扁平列表 List<PO>
        List<Cart> poList = cartDao.selectByUserId(userId);

        // 2. 将 List<PO> 转换为 List<Entity>
        List<CartItemEntity> items = poList.stream()
                .map(this::toEntity) // 调用下面的转换方法
                .collect(Collectors.toList());

        // 3. 组装成聚合根并返回
        // 哪怕 items 是空的，也要返回一个空的 CartAggregate 对象，不能返回 null
        return new CartAggregate(userId, items);
    }

    /**
     * 将 领域对象 转为 数据库对象
     * 注意：PO 需要 userId，但 Entity 里没有 (因为 Entity 在 Aggregate 内部)，
     * 所以 userId 需要从 Aggregate Root 传进来
     */
    private Cart toPO(CartItemEntity entity, Integer userId) {
        if (entity == null) return null;
        Cart po = new Cart();
        po.setId(entity.getId());
        po.setUid(userId);              // 关联用户
        po.setFid(entity.getFlowerId());// 关联商品
        po.setFlower(entity.getFlowerName());
        po.setPrice(entity.getPrice());
        po.setAmount(entity.getAmount());
        return po;
    }

    /**
     * 将 数据库对象 转为 领域对象
     */
    private CartItemEntity toEntity(Cart po) {
        if (po == null) return null;
        return new CartItemEntity(
                        po.getId(),       // id
                        po.getFid(),      // flowerId
                        po.getFlower(),   // flowerName
                po.getAmount(),
                po.getPrice()
                );
    }
}