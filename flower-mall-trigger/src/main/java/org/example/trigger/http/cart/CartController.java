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
 * 当前逻辑是用户下单后全量清空购物车
 * 暂时不支持勾选商品下单
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
        // 1. 从 Token 上下文获取当前用户ID
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
        // 1. 直接获取当前用户ID
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

    /**
     * 更新数量
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody CartAddRequest request) {
        String userIdStr = UserContext.getUserId();
        if (userIdStr == null) return Result.error("4001", "未登录");

        Integer currentUserId = Integer.parseInt(userIdStr);

        log.info(">>> 收到[更新购物车数量]请求: userId={}, flowerId={}, newCount={}",
                currentUserId, request.getFlowerId(), request.getCount());

        long start = System.currentTimeMillis();

        try {
            cartService.updateItemQuantity(
                    currentUserId,
                    request.getFlowerId(),
                    request.getCount()
            );
            log.info("<<< [更新购物车数量]成功, 耗时: {}ms", System.currentTimeMillis() - start);
            return Result.success();

        } catch (Exception e) {
            log.error("!!! [更新购物车数量]失败: userId={}, flowerId={}, cause={}",
                    currentUserId, request.getFlowerId(), e.getMessage(), e);
            return Result.error("500", "更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除购物车商品
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody CartAddRequest request) {
        String userIdStr = UserContext.getUserId();
        if (userIdStr == null) return Result.error("4001", "未登录");

        Integer currentUserId = Integer.parseInt(userIdStr);

        log.info(">>> 收到[删除购物车商品]请求: userId={}, flowerId={}",
                currentUserId, request.getFlowerId());

        long start = System.currentTimeMillis();

        try {
            cartService.removeCartItem(
                    currentUserId,
                    request.getFlowerId()
            );
            log.info("<<< [删除购物车商品]成功, 耗时: {}ms", System.currentTimeMillis() - start);
            return Result.success();

        } catch (Exception e) {
            log.error("!!! [删除购物车商品]失败: userId={}, flowerId={}, cause={}",
                    currentUserId, request.getFlowerId(), e.getMessage(), e);
            return Result.error("500", "删除失败: " + e.getMessage());
        }
    }




}