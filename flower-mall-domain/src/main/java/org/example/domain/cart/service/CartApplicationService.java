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

        // 2.取出购物车
        CartAggregate cart = cartRepository.findByUserId(userId);

        // 3. 加入购物车
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

    /**
     * 业务：修改购物车商品数量 (覆盖模式)
     */
    public void updateItemQuantity(Integer userId, Integer flowerId, Integer count) {
        // 1. 查出购物车
        CartAggregate cart = cartRepository.findByUserId(userId);
        if (cart == null) return;

        cart.getItems().stream()
                .filter(item -> item.getFlowerId().equals(flowerId))
                .findFirst()
                .ifPresent(item -> item.setAmount(count));

        cartRepository.save(cart);
    }

    /**
     * 业务：移除购物车商品
     */
    public void removeCartItem(Integer userId, Integer flowerId) {
        CartAggregate cart = cartRepository.findByUserId(userId);
        if (cart != null) {
            // 移除指定 ID 的商品
            cart.getItems().removeIf(item -> item.getFlowerId().equals(flowerId));
            // 保存
            cartRepository.save(cart);
        }
    }

    /**
     * 业务：清空购物车 (下单成功后调用)
     */
    public void clearCart(Integer userId) {
        // 1. 查出该用户的购物车
        CartAggregate cart = cartRepository.findByUserId(userId);

        if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
            // 2. 清空所有列表项
            cart.getItems().clear();

            // 3. 保存状态 (数据库里的 items 也会被删掉)
            cartRepository.save(cart);
        }
    }

}
