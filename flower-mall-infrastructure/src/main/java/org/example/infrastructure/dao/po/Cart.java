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

    private Integer id;

    private Integer fid;

    private String flower;

    private Integer amount;

    private float price;

    private Integer uid;
}
