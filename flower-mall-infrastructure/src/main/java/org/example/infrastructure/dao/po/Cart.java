package org.example.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * 购物车PO，对应数据库表carts
 * @create 2025-12-09 09:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private int id;
    private int fid;
    private String flower;
    private int amount;
    private float price;
    private int uid;
    private String account;
}
