package org.example.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author lanjiajun
 * @description 订单项表
 * 对应数据库order_items表
 * @create 2025-12-11 15:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    /** 自增主键ID (对应 id) */
    private Long id;

    /** 业务订单号 (对应 order_id) */
    private String orderId;

    /** 商品ID (对应 flower_id) */
    private Integer flowerId;

    /** 商品名称快照 (对应 flower_name) */
    private String flowerName;

    /** 下单时单价快照 (对应 price) */
    private Float price;

    /** 购买数量 (对应 quantity) */
    private Integer quantity;

    /** 该项小计 (对应 sub_total) */
    private Float subTotal;

    /** 创建时间 (对应 create_time) */
    private LocalDateTime createTime;

    /** 更新时间 (对应 update_time) */
    private LocalDateTime updateTime;



}
