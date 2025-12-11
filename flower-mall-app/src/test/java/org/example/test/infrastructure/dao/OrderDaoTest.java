package org.example.test.infrastructure.dao;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.dao.IOrderDao;
import org.example.infrastructure.dao.po.Order;
import org.example.infrastructure.dao.po.OrderItem;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 17:16
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoTest {
    @Resource
    private IOrderDao orderDao;

    @Test
    public void test_order_persistence() {
        // ==================== 1. 准备测试数据 ====================

        // --- 订单主表 PO ---
        Order orderPO = new Order();
        String orderId = UUID.randomUUID().toString(); // 生成唯一的业务订单号
        orderPO.setOrderId(orderId);
        orderPO.setUserId(14);
        orderPO.setStatus(1); // 1-待支付
        orderPO.setReceiverName("张三");
        orderPO.setReceiverPhone("13800138000");
        orderPO.setAddress("北京市朝阳区");

        // --- 订单项 PO 列表 (模拟买了两种花) ---
        OrderItem item1 = new OrderItem();
        item1.setOrderId(orderId);
        item1.setFlowerId(88);
        item1.setFlowerName("红玫瑰(测试)");
        item1.setPrice(99.0f);
        item1.setQuantity(2);
        item1.setSubTotal(198.0f);

        OrderItem item2 = new OrderItem();
        item2.setOrderId(orderId);
        item2.setFlowerId(7);
        item2.setFlowerName("百合");
        item2.setPrice(120.0f);
        item2.setQuantity(1);
        item2.setSubTotal(120.0f);

        List<OrderItem> items = Arrays.asList(item1, item2);

        // --- 计算总价并设置 ---
        float totalAmount = item1.getSubTotal() + item2.getSubTotal();
        orderPO.setTotalAmount(totalAmount);

        // ==================== 2. 测试插入 ====================

        // a. 插入主表
        orderDao.insertOrder(orderPO);
        log.info("插入订单主表成功, 回填的自增ID: {}", orderPO.getId());
        Assert.assertNotNull("主表ID回填失败", orderPO.getId());

        // b. 批量插入子表
        orderDao.insertOrderItems(items);
        log.info("批量插入订单项成功");

        // ==================== 3. 测试查询 ====================

        // a. 查询主表
        Order selectedOrder = orderDao.selectByOrderId(orderId);
        log.info("查询出的订单主表: {}", JSON.toJSONString(selectedOrder));
        Assert.assertNotNull("未查询到订单主表", selectedOrder);
        Assert.assertEquals("总金额不匹配", totalAmount, selectedOrder.getTotalAmount(), 0.01);

        // b. 查询子表
        List<OrderItem> selectedItems = orderDao.selectItemsByOrderId(orderId);
        log.info("查询出的订单项列表: {}", JSON.toJSONString(selectedItems));
        Assert.assertEquals("订单项数量不匹配", 2, selectedItems.size());
    }
}
