package org.example.domain.cart.repository;

import org.example.domain.cart.model.aggregate.CartAggregate;

/**
 * @author lanjiajun
 * @description
 * 仓储接口
 * @create 2025-12-11 12:49
 */
public interface ICartRepository {
    /**
     * 保存购物车聚合根
     * (Infrastructure层需要负责把这个立体对象拆解成数据库的行)
     */
    void save(CartAggregate cartAggregate);

    /**
     * 根据用户ID获取购物车
     * (Infrastructure层需要负责把数据库的多行记录组装成这个聚合根)
     */
    CartAggregate findByUserId(Integer userId);
}
