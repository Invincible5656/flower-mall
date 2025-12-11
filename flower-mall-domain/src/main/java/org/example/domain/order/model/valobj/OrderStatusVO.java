package org.example.domain.order.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author lanjiajun
 * @description
 * 值对象-订单状态枚举
 * @create 2025-12-11 16:19
 */
@Getter
@AllArgsConstructor
public enum OrderStatusVO {
    WAIT_PAY(1, "待支付"),
    PAID(2, "已支付"),
    SHIPPED(3, "已发货"),
    COMPLETED(4, "已完成"),
    CANCELED(5, "已取消");

    private final Integer code;
    private final String desc;

    /**
     * 根据 code 获取枚举实例
     * @param code 数据库中存的整数值
     * @return 对应的枚举
     */
    public static OrderStatusVO of(Integer code) {
        if (code == null) {
            return null; // 或者抛出异常，看业务需求
        }

        return Arrays.stream(OrderStatusVO.values())
                .filter(status -> status.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("无效的订单状态码: " + code));
    }
}
