package org.example.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * 花PO，对应数据库表flowers
 * @create 2025-12-09 09:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flower {
    private Integer id;

    private String name;
    // 分类名称
    private String speciesName;

    private float price;
    // 详细介绍
    private String detail;
    // 图片
    private String imgGuid;
    // 是否上架
    private int state;
}
