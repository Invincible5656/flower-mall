package org.example.domain.flower.adapter.repository;

import org.example.domain.flower.model.entity.SpeciesEntity;

import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 19:24
 */
public interface ISpeciesRepository {
    void save(SpeciesEntity species);

    void delete(Integer id);

    SpeciesEntity findById(Integer id);

    List<SpeciesEntity> findAll(String searchKey);
}
