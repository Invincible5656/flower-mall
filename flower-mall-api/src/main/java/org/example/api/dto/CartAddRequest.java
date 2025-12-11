package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 14:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartAddRequest {
    // userId 应该从登录 Token 中获取
    // 但为了测试方便，我们先显式传一个 userId
    private Integer userId;

    private Integer flowerId;

    private Integer count;
}
