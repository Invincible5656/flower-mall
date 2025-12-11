package org.example.test.infrastructure.dao;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.dao.ISpeciesDao;
import org.example.infrastructure.dao.po.Species;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 19:30
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpeciesDaoTest {

    @Resource
    private ISpeciesDao speciesDao;

    @Test
    public void test_species_crud_operations() {
        // --- 1. 准备测试数据 ---
        String testName = "单元测试品类";
        Species speciesPO = new Species();
        speciesPO.setSpeciesName(testName);

        // --- 2. 测试插入 ---
        int insertCount = speciesDao.insert(speciesPO);
        log.info("插入品类'{}', 影响行数: {}", testName, insertCount);
        Assert.assertEquals(1, insertCount);
        Assert.assertNotNull("ID回填失败", speciesPO.getId()); // 验证主键回填
        Integer testId = speciesPO.getId();

        // --- 3. 测试根据ID查询 ---
        Species selectedById = speciesDao.findById(testId);
        log.info("根据ID查询结果: {}", JSON.toJSONString(selectedById));
        Assert.assertNotNull(selectedById);
        Assert.assertEquals(testName, selectedById.getSpeciesName());

        // --- 4. 测试更新 ---
        String updatedName = "单元测试品类-已更新";
        speciesPO.setSpeciesName(updatedName);
        int updateCount = speciesDao.update(speciesPO);
        log.info("更新品类名称为'{}', 影响行数: {}", updatedName, updateCount);
        Assert.assertEquals(1, updateCount);

        // 再次查询验证更新是否成功
        Species updatedEntity = speciesDao.findById(testId);
        Assert.assertEquals(updatedName, updatedEntity.getSpeciesName());

        // --- 5. 测试列表查询 (模糊搜索) ---
        List<Species> list = speciesDao.findAll("单元测试");
        log.info("模糊搜索'单元测试'结果: {}", JSON.toJSONString(list));
        Assert.assertFalse("模糊搜索未找到结果", list.isEmpty());

        // --- 6. 测试删除 (清理测试数据) ---
        int deleteCount = speciesDao.deleteById(testId);
        log.info("删除ID为{}的品类, 影响行数: {}", testId, deleteCount);
        Assert.assertEquals(1, deleteCount);

        Species deletedEntity = speciesDao.findById(testId);
        Assert.assertNull("删除失败，仍能查到数据", deletedEntity);
    }
}

