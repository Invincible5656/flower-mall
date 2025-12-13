package org.example.domain.order.service;

import org.example.domain.cart.model.aggregate.CartAggregate;
import org.example.domain.cart.repository.ICartRepository;
import org.example.domain.cart.service.CartApplicationService;
import org.example.domain.flower.adapter.repository.IFlowerRepository;
import org.example.domain.order.factory.OrderFactory;
import org.example.domain.order.model.aggregate.OrderAggregate;
import org.example.domain.order.repository.IOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 18:47
 */
@Service
public class OrderApplicationService {
    @Resource
    private IOrderRepository orderRepository;

    @Resource
    private ICartRepository cartRepository;

    @Resource
    private OrderFactory orderFactory;

    @Resource
    private IFlowerRepository flowerRepository;

    @Resource
    private CartApplicationService cartService;

    /**
     * 创建订单
     * @param userId 用户ID
     * @param receiverName 收货人
     * @param receiverPhone 电话
     * @param address 地址
     * @return 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(Integer userId, String receiverName, String receiverPhone, String address) {
        // 加载用户的购物车
        CartAggregate cart = cartRepository.findByUserId(userId);

        // 使用工厂创建订单聚合根 (这里面包含了校验、计算、转换等复杂逻辑)
        OrderAggregate order = orderFactory.createOrder(userId, cart, receiverName, receiverPhone, address);

        // 保存订单 (RepositoryImpl 会负责拆解成主子表插入)
        orderRepository.save(order);

        // 下单成功后，清空该用户的购物车
        cartService.clearCart(userId);

        // 返回业务订单号给前端
        return order.getOrderId();
    }

    /**
     * 查询订单详情
     * @param orderId 订单号
     * @return 订单聚合对象
     */
    public OrderAggregate getOrderDetail(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }
}
