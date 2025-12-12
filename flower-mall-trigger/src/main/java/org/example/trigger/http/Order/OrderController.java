package org.example.trigger.http.Order;

import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.OrderCreateRequest;
import org.example.api.response.Result;
import org.example.domain.order.model.aggregate.OrderAggregate;
import org.example.domain.order.service.OrderApplicationService;
import org.example.types.common.UserContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 19:01
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Resource
    private OrderApplicationService orderService;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<String> create(@RequestBody OrderCreateRequest request) {
        String userIdStr = UserContext.getUserId();
        if (userIdStr == null) return Result.error("4001", "未登录");

        Integer currentUserId = Integer.parseInt(userIdStr);

        log.info(">>> 收到[创建订单]请求: userId={}", currentUserId);

        try {
            // 2. 使用 Token ID 创建订单
            String orderId = orderService.createOrder(
                    currentUserId,
                    request.getReceiverName(),
                    request.getReceiverPhone(),
                    request.getAddress()
            );

            log.info("<<< [创建订单]成功, 订单号: {}", orderId);
            return Result.success(orderId);
        } catch (Exception e) {
            log.error("!!! [创建订单]失败: userId={}", currentUserId, e);
            return Result.error("500", e.getMessage());
        }
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/detail")
    public Result<OrderAggregate> detail(@RequestParam String orderId) {
        String userIdStr = UserContext.getUserId();
        Integer currentUserId = Integer.parseInt(userIdStr);

        log.info(">>> 收到[查询订单详情]请求: orderId={}, operator={}", orderId, userIdStr);

        OrderAggregate order = orderService.getOrderDetail(orderId);

        //【安全建议】AI建议我进行改造！
        // 严格来说，这里应该检查 order.getUserId() 是否等于 currentUserId
        // 如果不相等，说明你在查别人的订单，应该抛出异常！
        if (order != null && !order.getUserId().equals(currentUserId)) {
             return Result.error("4003", "无权查看他人订单");
        }
        return Result.success(order);
    }
}