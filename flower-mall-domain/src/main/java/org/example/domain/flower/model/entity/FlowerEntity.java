package org.example.domain.flower.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * 领域层花实体；业务逻辑的载体
 * 注意和数据库PO对象区别
 * @create 2025-12-09 11:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowerEntity {
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

    /**
     * 业务行为：修改图片
     * 对应原来的 updateImg 接口
     */
    public void changeImg(String newImgGuid) {
        // 1. 可以在这里加校验逻辑（这是业务逻辑，属于 Domain）
        if (newImgGuid == null || newImgGuid.trim().isEmpty()) {
            throw new IllegalArgumentException("图片ID不能为空");
        }
        // 2. 修改自身的属性（只是在内存里改，还没存数据库呢）
        this.imgGuid = newImgGuid;
    }

    /**
     * 业务行为：修改基本信息
     */
    public void changeInfo(String name, Float price, String speciesName) {
        if (price != null && price < 0) {
            throw new IllegalArgumentException("价格不能小于0");
        }

        // 只有不为空时才更新
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (speciesName != null) this.speciesName = speciesName;
    }
}
