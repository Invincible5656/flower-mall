package org.example.domain.flower.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 19:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeciesEntity {
    private Integer id;
    private String name;

    // 构造函数用于创建
    public SpeciesEntity(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("品类名称不能为空");
        }
        this.name = name;
    }

    // 业务行为：修改名称
    public void changeName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("品类名称不能为空");
        }
        this.name = newName;
    }

    // 用于从数据库重建
    public void init(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void initId(Integer id) {
        if (this.id == null) {
            this.id = id;
        }
    }
}
