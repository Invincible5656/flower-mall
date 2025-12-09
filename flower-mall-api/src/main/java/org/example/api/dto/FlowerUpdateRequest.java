package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description 修改鲜花的请求对象
 * @create 2025-12-09 18:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowerUpdateRequest {
    private Integer id;
    private String name;
    private Float price;
    private String speciesName;
}
