package org.example.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.dao.po.Cart;

import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 13:01
 */
@Mapper
public interface ICartDao {
    // 1. 根据用户ID查询所有记录 (用于组装购物车)
    List<Cart> selectByUserId(@Param("uid") Integer uid);

    // 2. 插入一条新记录
    // 关键：必须配置 useGeneratedKeys="true" keyProperty="id" 才能拿到自增ID
    int insert(Cart cartPO);

    // 3. 更新一条记录 (通常只更新数量)
    int updateAmount(Cart cartPO);

    // 删除
    int deleteById(@Param("id") Integer id);
}
