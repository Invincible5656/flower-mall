package org.example.infrastructure.adapter.repository;

import org.example.domain.order.model.aggregate.OrderAggregate;
import org.example.domain.order.model.entity.OrderItemEntity;
import org.example.domain.order.model.valobj.OrderStatusVO;
import org.example.domain.order.repository.IOrderRepository;
import org.example.infrastructure.dao.IOrderDao;
import org.example.infrastructure.dao.po.Order;
import org.example.infrastructure.dao.po.OrderItem;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 17:01
 */
@Repository
public class OrderRepositoryImpl implements IOrderRepository {

    @Resource
    private IOrderDao orderDao;

    @Override
    public void save(OrderAggregate order) {
        Order orderPO = toOrderPO(order);

        //判断是新增还是更新
        // 尝试查一下数据库里有没有这个单号
        Order existPO = orderDao.selectByOrderId(order.getOrderId());
        if (existPO == null) {
            // A. 不存在 -> 执行【新增】逻辑 (下单流程)
            // 1. 插入主表
            orderDao.insertOrder(orderPO);
            // 2. 转换并插入子表 (order_items)
            // 只有新增时才需要插子表，因为发货时通常不改商品明细
            List<OrderItem> itemPOList = order.getItems().stream()
                    .map(item -> toOrderItemPO(item, order.getOrderId()))
                    .collect(Collectors.toList());

            if (!itemPOList.isEmpty()) {
                orderDao.insertOrderItems(itemPOList);
            }
        } else {
            // B. 已存在 -> 执行【更新】逻辑 (发货流程)
            // 或者：如果你只想改状态，可以写得更轻量一点：
            orderDao.updateStatus(order.getOrderId(), order.getStatus().getCode());
        }
    }

    @Override
    public OrderAggregate findByOrderId(String orderId) {
        // --- 把数据库的扁平数据，重组成一个完整的聚合根对象 ---

        // 1. 查主表
        Order orderPO = orderDao.selectByOrderId(orderId);
        if (orderPO == null) return null;

        // 2. 查子表
        List<OrderItem> itemPOList = orderDao.selectItemsByOrderId(orderId);

        // 3. 转换 + 组装
        List<OrderItemEntity> items = itemPOList.stream()
                .map(this::toOrderItemEntity)
                .collect(Collectors.toList());

        return OrderAggregate.reconstruct(
                orderPO.getId(),
                orderPO.getOrderId(),
                orderPO.getUserId(),
                orderPO.getTotalAmount(),
                OrderStatusVO.of(orderPO.getStatus()), // 假设枚举有 valueOf 方法
                items,
                orderPO.getReceiverName(),
                orderPO.getReceiverPhone(),
                orderPO.getAddress(),
                orderPO.getCreateTime()
        );
    }

    @Override
    public List<OrderAggregate> findByUserId(Integer userId) {
        // 1. 查主表
        List<Order> orderPOs = orderDao.selectByUserId(userId);
        if (orderPOs == null) return new ArrayList<>();

        // 2. 批量转为聚合根 (这里为了简单，循环查 Item，性能好一点可以用 Join)
        return orderPOs.stream().map(this::toAggregate).collect(Collectors.toList());
    }


    @Override
    public List<OrderAggregate> findAll() {
        // 1. 调用 DAO 查询所有订单主表数据
        List<Order> orderPOs = orderDao.selectAll();

        if (orderPOs == null || orderPOs.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 将 PO 列表转换为 Aggregate 列表
        // 这里复用了 toAggregate 方法（如果你没有这个方法，请往下看辅助方法的实现）
        return orderPOs.stream()
                .map(this::toAggregate)
                .collect(Collectors.toList());
    }

    @Override
    public Integer countOrders() {
        return orderDao.count();
    }

    @Override
    public BigDecimal sumTotalSales(List<Integer> statusList) {
        return orderDao.sumTotalPriceByStatus(statusList);
    }


    private Order toOrderPO(OrderAggregate order) {
        Order po = new Order();
        po.setOrderId(order.getOrderId());
        po.setUserId(order.getUserId());
        po.setTotalAmount(order.getTotalAmount());
        po.setStatus(order.getStatus().getCode()); // 枚举转 code
        po.setReceiverName(order.getReceiverName());
        po.setReceiverPhone(order.getReceiverPhone());
        po.setAddress(order.getAddress());

        return po;
    }

    private OrderItem toOrderItemPO(OrderItemEntity entity, String orderId) {
        OrderItem po = new OrderItem();
        po.setOrderId(orderId); // 关联主表
        po.setFlowerId(entity.getFlowerId());
        po.setFlowerName(entity.getFlowerName());
        po.setPrice(entity.getPrice());
        po.setQuantity(entity.getQuantity());
        po.setSubTotal(entity.getSubTotal());
        return po;
    }

    private OrderItemEntity toOrderItemEntity(OrderItem po) {
        return new OrderItemEntity(
                po.getFlowerId(), po.getFlowerName(), po.getPrice(), po.getQuantity()
        );
    }


    private OrderAggregate toAggregate(Order po) {
        if (po == null) return null;

        // 1. 查出该订单下的所有明细 (Items)
        // 确保你的 DAO 方法名是对的，有些叫 selectByOrderId，有些叫 selectItemsByOrderId
        List<OrderItem> itemPos = orderDao.selectItemsByOrderId(po.getOrderId());

        // 2. 将 Item PO 转为 Entity
        List<OrderItemEntity> items = new ArrayList<>();
        if (itemPos != null) {
            items = itemPos.stream().map(itemPo -> new OrderItemEntity(
                    Math.toIntExact(itemPo.getId()),           // 对应构造函数第1个参数: Integer id
                    itemPo.getFlowerId(),     // 对应构造函数第2个参数: Integer flowerId
                    itemPo.getFlowerName(),   // 对应构造函数第3个参数: String flowerName
                    itemPo.getQuantity(),     // 对应构造函数第4个参数: Integer quantity
                    itemPo.getPrice()         // 对应构造函数第5个参数: Float price
            )).collect(Collectors.toList());
        }

        // 3. 构建状态值对象
        // 【关键修改】枚举不能 new，改用静态方法 getByCode
        OrderStatusVO statusVO = OrderStatusVO.of(po.getStatus());

        // 4. 重建聚合根
        // 【注意】这里使用了 OrderAggregate.reconstruct 静态工厂方法
        return OrderAggregate.reconstruct(
                po.getId(),            // 数据库自增ID
                po.getOrderId(),       // 业务订单号
                po.getUserId(),
                po.getTotalAmount(),    // 注意：PO里叫 getTotalPrice() 还是 getTotalAmount()？请检查 PO 类
                statusVO,              // 状态枚举
                items,                 // 商品列表
                po.getReceiverName(),
                po.getReceiverPhone(),
                po.getAddress(),
                po.getCreateTime()
        );
    }
}
