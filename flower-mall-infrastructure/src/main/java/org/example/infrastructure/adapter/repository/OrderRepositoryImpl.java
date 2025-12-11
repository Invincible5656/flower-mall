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
        // --- 第一步：把聚合根拆解成 PO 对象 ---

        // 1. 转换主表对象
        Order orderPO = toOrderPO(order);

        // 2. 转换子表对象列表
        List<OrderItem> itemPOList = order.getItems().stream()
                .map(item -> toOrderItemPO(item, order.getOrderId()))
                .collect(Collectors.toList());

        // --- 第二步：事务性地保存到数据库 (事务由 App 层的 @Transactional 保证) ---
        // 3. 插入主表 (order)
        orderDao.insertOrder(orderPO);

        // 4. 批量插入子表 (order_items)
        orderDao.insertOrderItems(itemPOList);
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
}
