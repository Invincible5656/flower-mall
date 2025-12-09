package org.example.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * 订单PO，对应数据库表orders
 * @create 2025-12-09 09:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int id;
    private String orderGuid;
    private String flower;
    private int amount;
    private float price;
    private float state;
    // 用户 id
    private int uid;
}
