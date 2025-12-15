package org.example.domain.order.service;

import org.example.domain.cart.model.aggregate.CartAggregate;
import org.example.domain.cart.repository.ICartRepository;
import org.example.domain.cart.service.CartApplicationService;
import org.example.domain.flower.adapter.repository.IFlowerRepository;
import org.example.domain.order.factory.OrderFactory;
import org.example.domain.order.model.aggregate.OrderAggregate;
import org.example.domain.order.model.valobj.OrderStatusVO;
import org.example.domain.order.repository.IOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private EmailService emailService;

    /**
     * 创建订单
     *
     * @param userId        用户ID
     * @param receiverName  收货人
     * @param receiverPhone 电话
     * @param address       地址
     * @param email
     * @return 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(Integer userId, String receiverName, String receiverPhone, String address, String email) {
        // 加载用户的购物车
        CartAggregate cart = cartRepository.findByUserId(userId);

        // 使用工厂创建订单聚合根 (这里面包含了校验、计算、转换等复杂逻辑)
        OrderAggregate order = orderFactory.createOrder(userId, cart, receiverName, receiverPhone, address);

        // 保存订单 (RepositoryImpl 会负责拆解成主子表插入)
        orderRepository.save(order);

        // 下单成功后，清空该用户的购物车
        cartService.clearCart(userId);

        // 发送邮件
        String orderId = order.getOrderId();
        emailService.sendOrderConfirm(email, orderId);

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

    /**
     * 用户查询自己的订单
     */
    public List<OrderAggregate> findMyOrders(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        // 直接调用仓储层
        return orderRepository.findByUserId(userId);
    }

    /**
     * 管理员查询所有订单
     */
    public List<OrderAggregate> findAllOrders() {
        // 直接调用仓储层
        return orderRepository.findAll();
    }

    /**
     * 发货业务
     */
    @Transactional(rollbackFor = Exception.class) // 涉及修改，最好加事务
    public void shipOrder(String orderId) {
        // 1. 通过仓储层查询订单聚合根
        // (注意：这里你需要确保 Repository 有 findByOrderId 方法)
        OrderAggregate order = orderRepository.findByOrderId(orderId);

        if (order == null) {
            throw new RuntimeException("订单号不存在: " + orderId);
        }

        // 修改状态为 2 (已发货)
        // 这里的 OrderStatusVO 是你在 domain 层定义的值对象
        order.setStatus(OrderStatusVO.of(2));

        // 4. 保存修改
        orderRepository.save(order);
    }

    /**
     * 获取销售统计
     */
    public Map<String, Object> getSalesStats() {
        // 1. 查询总销售额 (已支付/已发货/已完成的订单)
        BigDecimal totalSales = orderRepository.sumTotalSales(Arrays.asList(1,2,3));

        // 如果没有销量，给个 0，防止前端 NaN
        if (totalSales == null) totalSales = BigDecimal.ZERO;

        // 2. 查询总订单数
        Integer totalOrders = orderRepository.countOrders();

        // 3. 组装结果
        Map<String, Object> map = new HashMap<>();
        map.put("sales", totalSales);
        map.put("orders", totalOrders);
        return map;
    }
}
