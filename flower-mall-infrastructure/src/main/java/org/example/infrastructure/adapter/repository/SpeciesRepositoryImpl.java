package org.example.infrastructure.adapter.repository;

import org.example.domain.flower.adapter.repository.ISpeciesRepository;
import org.example.domain.flower.model.entity.SpeciesEntity;
import org.example.infrastructure.dao.ISpeciesDao;
import org.example.infrastructure.dao.po.Species;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 19:40
 */
@Repository
public class SpeciesRepositoryImpl implements ISpeciesRepository {

    @Resource
    private ISpeciesDao speciesDao;

    @Override
    public void save(SpeciesEntity speciesEntity) {
        // 1. 转换: Entity -> PO
        Species po = toPO(speciesEntity);

        // 2. 判断是新增还是更新
        if (speciesEntity.getId() == null) {
            speciesDao.insert(po);
            // 回填自增 ID
            speciesEntity.initId(po.getId());
        } else {
            speciesDao.update(po);
        }
    }
    @Override
    public void delete(Integer id) {
        speciesDao.deleteById(id);
    }

    @Override
    public SpeciesEntity findById(Integer id) {
        Species po = speciesDao.findById(id);
        // 转换: PO -> Entity
        return toEntity(po);
    }

    @Override
    public List<SpeciesEntity> findAll(String searchKey) {
        List<Species> poList = speciesDao.findAll(searchKey);
        if (poList == null || poList.isEmpty()) {
            return Collections.emptyList();
        }
        // 批量转换
        return poList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * PO -> Entity
     */
    private SpeciesEntity toEntity(Species po) {
        if (po == null) {
            return null;
        }
        SpeciesEntity entity = new SpeciesEntity();
        // 假设你的 SpeciesEntity 有一个 init 方法或对应的 setter
        entity.init(po.getId(), po.getSpeciesName());
        return entity;
    }

    /**
     * Entity -> PO
     */
    private Species toPO(SpeciesEntity entity) {
        if (entity == null) {
            return null;
        }
        Species po = new Species();
        po.setId(entity.getId());
        po.setSpeciesName(entity.getName());
        return po;
    }
}
