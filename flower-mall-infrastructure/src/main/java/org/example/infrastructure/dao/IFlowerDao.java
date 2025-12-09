package org.example.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.dao.po.Flower;

import java.util.List;

/**
 * @author lanjiajun
 * @description
 * 花数据访问接口
 * @create 2025-12-09 10:05
 */
@Mapper
public interface IFlowerDao {
    // find
    List<Flower> find(@Param("searchKey") String searchKey, @Param("searchType") String searchType);

    // findAll
    List<Flower> findAll(@Param("searchKey") String searchKey);

    // queryPrice
    Float queryPrice(@Param("id") Long id);

    // update
    int update(Flower flowerPO);

    // updateImg
    int updateImg(@Param("imgGuid") String imgGuid, @Param("id") Long id);

    // changeState
    int changeState(Flower flowerPO);

    // delete
    int delete(@Param("id") Long id);

    // add
    int insert(Flower flowerPO);
}
