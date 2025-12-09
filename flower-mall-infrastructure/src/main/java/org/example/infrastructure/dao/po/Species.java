package org.example.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * 花品种PO，对应数据库表species
 * @create 2025-12-09 09:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Species {
    private int id;
    private String speciesName;
}
