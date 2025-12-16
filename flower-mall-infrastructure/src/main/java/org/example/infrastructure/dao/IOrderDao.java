package org.example.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.dao.po.Order;
import org.example.infrastructure.dao.po.OrderItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-09 10:59
 */
@Mapper
public interface IOrderDao {
    // 插入订单主表
    void insertOrder(Order orderPO);

    // 批量插入订单项 (性能更高)
    void insertOrderItems(@Param("items") List<OrderItem> items);

    // 根据业务订单号查询主表
    Order selectByOrderId(@Param("orderId") String orderId);

    // 根据业务订单号查询所有订单项
    List<OrderItem> selectItemsByOrderId(@Param("orderId") String orderId);

    List<Order> selectAll();

    List<Order> selectByUserId(Integer userId);

    void updateStatus(@Param("orderId") String orderId, @Param("status") Integer status);

    Integer count();

    BigDecimal sumTotalPriceByStatus(@Param("statusList") List<Integer> statusList);
}
