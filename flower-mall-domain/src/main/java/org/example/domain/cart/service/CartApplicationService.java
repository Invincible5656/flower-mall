package org.example.domain.cart.service;

import org.example.domain.cart.model.aggregate.CartAggregate;
import org.example.domain.cart.repository.ICartRepository;
import org.example.domain.flower.adapter.repository.IFlowerRepository;
import org.example.domain.flower.model.entity.FlowerEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 14:12
 */
@Service
public class CartApplicationService {

    @Resource
    private ICartRepository cartRepository;

    @Resource
    private IFlowerRepository flowerRepository;

    /**
     * 添加商品到购物车
     * (逻辑：先查 -> 内存修改 -> 整体保存)
     */
    @Transactional(rollbackFor = Exception.class)
    public void addToCart(Integer userId, Integer flowerId, Integer count) {
        // 1. 【安全校验】先去查鲜花模块，获取真实的价格和名字
        FlowerEntity flower = flowerRepository.findById(flowerId);

        if (flower == null) {
            throw new RuntimeException("商品不存在，无法加入购物车");
        }

        // (可选) 这里还可以校验库存
        // if (flower.getStock() < count) throw ...

        // 2. Load: 取出购物车
        CartAggregate cart = cartRepository.findByUserId(userId);

        // 3. Do: 加入购物车
        // ⭐️ 注意：这里的 price 和 name 使用的是从数据库查出来的 flower 对象里的数据！
        cart.addFlower(
                flowerId,
                flower.getName(), // 真实名字
                flower.getPrice(), // 真实价格
                count
        );

        // 4. Save: 保存
        cartRepository.save(cart);
    }
    /**
     * 查询我的购物车
     */
    public CartAggregate getMyCart(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

}
