package org.example.infrastructure.adapter.repository;

import org.example.domain.cart.model.aggregate.CartAggregate;
import org.example.domain.cart.model.entity.CartItemEntity;
import org.example.domain.cart.repository.ICartRepository;
import org.example.infrastructure.dao.ICartDao;
import org.example.infrastructure.dao.po.Cart;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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
        List<CartItemEntity> currentItems = cartAggregate.getItems();
        Integer userId = cartAggregate.getUserId();

        // 1. 先查出数据库里现有的数据，用于比对
        List<Cart> dbList = cartDao.selectByUserId(userId);
        // Key: 商品ID(Integer), Value: 购物车主键ID(Integer)
        Map<Integer, Integer> dbMap = dbList.stream()
                .collect(Collectors.toMap(Cart::getFid, Cart::getId));

        // 2. 遍历当前内存里的新数据 (处理 新增 & 更新)
        if (currentItems != null) {
            for (CartItemEntity item : currentItems) {
                // 【修改点】：item.getFlowerId() 现在应该返回 Integer
                // 检查数据库里是否已经有这个商品
                if (dbMap.containsKey(item.getFlowerId())) {
                    // A. 如果有：说明是【更新】
                    Integer existingId = dbMap.get(item.getFlowerId());
                    item.initId(existingId); // 确保实体有 ID

                    // 从 Map 中移除，剩下的就是待删除的
                    dbMap.remove(item.getFlowerId());
                }

                // 转 PO
                Cart po = toPO(item, userId);

                // 执行数据库操作
                if (item.getId() == null) {
                    // --- 新增 ---
                    cartDao.insert(po);
                    item.initId(po.getId()); // 回填 ID
                } else {
                    // --- 更新 ---
                    cartDao.updateItem(po);
                }
            }
        }
        // 3. 处理被删除的数据
        // 此时 dbMap 里剩下的，就是“数据库里有，但当前 items 里没有”的数据
        // 就是用户点击“删除”的商品
        for (Integer idToDelete : dbMap.values()) {
            cartDao.deleteById(idToDelete);
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