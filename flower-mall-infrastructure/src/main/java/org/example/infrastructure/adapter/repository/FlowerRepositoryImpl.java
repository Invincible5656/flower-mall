package org.example.infrastructure.adapter.repository;

import org.example.domain.flower.adapter.repository.IFlowerRepository;
import org.example.domain.flower.model.entity.FlowerEntity;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-09 11:39
 */
@Repository
public class FlowerRepositoryImpl implements IFlowerRepository {

    @Override
    public void save(FlowerEntity flower) {

    }

    @Override
    public void delete(FlowerEntity flower) {

    }

    @Override
    public FlowerEntity findById(int id) {
        return null;
    }

    @Override
    public List<FlowerEntity> queryList(String searchKey, String searchType) {
        return Collections.emptyList();
    }

}