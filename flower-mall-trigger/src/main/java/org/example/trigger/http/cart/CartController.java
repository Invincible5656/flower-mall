package org.example.trigger.http.cart;

import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.CartAddRequest;
import org.example.api.response.Result;
import org.example.domain.cart.model.aggregate.CartAggregate;
import org.example.domain.cart.service.CartApplicationService;
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
        log.info(">>> 收到[添加购物车]请求: userId={}, flowerId={}, count={}",
                request.getUserId(), request.getFlowerId(), request.getCount());

        long start = System.currentTimeMillis();

        try {
            cartService.addToCart(
                    request.getUserId(),
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
    public Result<CartAggregate> list(@RequestParam Integer userId) {
        log.info(">>> 收到[查询购物车]请求: userId={}", userId);

        CartAggregate cart = cartService.getMyCart(userId);

        int itemCount = (cart != null && cart.getItems() != null) ? cart.getItems().size() : 0;

        log.info("<<< [查询购物车]返回成功: userId={}, 包含商品数={}", userId, itemCount);

        return Result.success(cart);
    }
}

