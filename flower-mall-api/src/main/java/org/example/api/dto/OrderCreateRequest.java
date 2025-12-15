package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 18:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    // 同样，userId 应该从 Token 获取，这里为了测试先传
    private Integer userId;
    private String receiverName;
    private String receiverPhone;
    private String address;
    private String email; // 本次订单的通知邮箱
}
