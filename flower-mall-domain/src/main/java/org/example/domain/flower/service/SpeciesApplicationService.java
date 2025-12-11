package org.example.domain.flower.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.flower.adapter.repository.ISpeciesRepository;
import org.example.domain.flower.model.entity.SpeciesEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.print.attribute.standard.MediaSize;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 19:52
 */
@Service
public class SpeciesApplicationService {

    @Resource
    private ISpeciesRepository speciesRepository;

    public void addSpecies(String name) {
        // 1. 创建领域对象
        SpeciesEntity species = new SpeciesEntity(name);

        // 2. 保存
        speciesRepository.save(species);
    }

    public void updateSpecies(Integer id, String name) {
        // 1. 先查
        SpeciesEntity species = speciesRepository.findById(id);
        if (species == null) {
            throw new RuntimeException("品类不存在，无法更新");
        }

        // 2. 再改 (调用领域行为)
        species.changeName(name);

        // 3. 后存
        speciesRepository.save(species);
    }

    public void deleteSpecies(Integer id) {
        // 直接调用仓储删除
        speciesRepository.delete(id);
    }

    public List<SpeciesEntity> listAll(String searchKey) {
        // 直接调用仓储查询
        return speciesRepository.findAll(searchKey);
    }
}
