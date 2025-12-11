package org.example.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.infrastructure.dao.po.Species;

import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 19:26
 */
@Mapper
public interface ISpeciesDao {
    int insert(Species speciesPO);

    int update(Species speciesPO);

    int deleteById(@Param("id") Integer id);

    Species findById(@Param("id") Integer id);

    List<Species> findAll(@Param("searchKey") String searchKey);

}
