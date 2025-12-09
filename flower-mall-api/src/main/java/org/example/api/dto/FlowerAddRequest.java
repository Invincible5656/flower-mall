package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description 新增鲜花的请求对象
 * @create 2025-12-09 18:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowerAddRequest {
    private String name;
    private Float price;
    private String speciesName;
    private String detail;
}
