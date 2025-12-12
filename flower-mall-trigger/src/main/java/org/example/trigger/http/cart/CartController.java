package org.example.trigger.http.cart;

import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.CartAddRequest;
import org.example.api.response.Result;
import org.example.domain.cart.model.aggregate.CartAggregate;
import org.example.domain.cart.service.CartApplicationService;
import org.example.types.common.UserContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 14:20
 */
@Slf4j
@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    @Resource
    private CartApplicationService cartService;

    /**
     * 添加到购物车
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody CartAddRequest request) {
        // 1. 【核心修改】从 Token 上下文获取当前用户ID
        String userIdStr = UserContext.getUserId();
        if (userIdStr == null) return Result.error("4001", "未登录");

        Integer currentUserId = Integer.parseInt(userIdStr);
        // 日志中要记录的是“当前登录人”，而不是“请求体里的人”！
        log.info(">>> 收到[添加购物车]请求: userId={}, flowerId={}, count={}",
                currentUserId, request.getFlowerId(), request.getCount());

        long start = System.currentTimeMillis();

        try {
            // 2. 调用 Service，传入 Token 解析出的 ID
            // request.getUserId() 已经被无视，前端传什么都没用
            cartService.addToCart(
                    currentUserId,
                    request.getFlowerId(),
                    request.getCount()
            );

            log.info("<<< [添加购物车]处理成功, 耗时: {}ms", System.currentTimeMillis() - start);
            return Result.success();

        } catch (Exception e) {
            log.error("!!! [添加购物车]发生异常: userId={}", request.getUserId(), e);
            return Result.error("500", "添加失败: " + e.getMessage());
        }
    }

    /**
     * 查看购物车列表
     */
    @GetMapping("/list")
    public Result<CartAggregate> list() {
        // 1. 【核心修改】直接获取当前用户ID
        String userIdStr = UserContext.getUserId();
        if (userIdStr == null) return Result.error("4001", "未登录");

        Integer currentUserId = Integer.parseInt(userIdStr);

        log.info(">>> 收到[查询购物车]请求: userId={}", currentUserId);

        // 2. 查询自己的购物车
        CartAggregate cart = cartService.getMyCart(currentUserId);

        int itemCount = (cart != null && cart.getItems() != null) ? cart.getItems().size() : 0;

        log.info("<<< [查询购物车]返回成功: userId={}, 包含商品数={}", currentUserId, itemCount);

        return Result.success(cart);
    }
}