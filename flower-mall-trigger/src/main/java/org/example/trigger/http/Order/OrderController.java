package org.example.trigger.http.Order;

import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.OrderCreateRequest;
import org.example.api.response.Result;
import org.example.domain.order.model.aggregate.OrderAggregate;
import org.example.domain.order.service.OrderApplicationService;
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
        log.info(">>> 收到[创建订单]请求: userId={}", request.getUserId());
        try {
            String orderId = orderService.createOrder(
                    request.getUserId(),
                    request.getReceiverName(),
                    request.getReceiverPhone(),
                    request.getAddress()
            );
            log.info("<<< [创建订单]成功, 订单号: {}", orderId);
            return Result.success(orderId);
        } catch (Exception e) {
            log.error("!!! [创建订单]失败: userId={}", request.getUserId(), e);
            return Result.error("500", e.getMessage());
        }
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/detail")
    public Result<OrderAggregate> detail(@RequestParam String orderId) {
        log.info(">>> 收到[查询订单详情]请求: orderId={}", orderId);
        OrderAggregate order = orderService.getOrderDetail(orderId);
        return Result.success(order);
    }
}