package org.example.trigger.http.flower;

import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.SpeciesCreateRequest;
import org.example.api.dto.SpeciesUpdateRequest;
import org.example.api.response.Result;
import org.example.domain.flower.model.entity.SpeciesEntity;
import org.example.domain.flower.service.SpeciesApplicationService;
import org.example.types.Annotaton.AdminOnly;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 19:37
 */
@Slf4j
@RestController
@RequestMapping("/api/species")
@CrossOrigin
public class SpeciesController {

    @Resource
    private SpeciesApplicationService speciesService;

    /**
     * 新增品类
     */
    @AdminOnly
    @PostMapping("/create")
    public Result<Void> create(@RequestBody SpeciesCreateRequest request) {
        log.info(">>> 收到[新增品类]请求: name={}", request.getName());
        try {
            speciesService.addSpecies(request.getName());
            return Result.success();
        } catch (Exception e) {
            log.error("!!! [新增品类]失败", e);
            return Result.error("500", e.getMessage());
        }
    }

    /**
     * 修改品类
     */
    @AdminOnly
    @PostMapping("/update")
    public Result<Void> update(@RequestBody SpeciesUpdateRequest request) {
        log.info(">>> 收到[修改品类]请求: id={}, name={}", request.getId(), request.getName());
        try {
            speciesService.updateSpecies(request.getId(), request.getName());
            return Result.success();
        } catch (Exception e) {
            log.error("!!! [修改品类]失败: id={}", request.getId(), e);
            return Result.error("500", e.getMessage());
        }
    }

    /**
     * 删除品类
     */
    @AdminOnly
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam("id") Integer id) {
        log.info(">>> 收到[删除品类]请求: id={}", id);
        try {
            speciesService.deleteSpecies(id);
            return Result.success();
        } catch (Exception e) {
            log.error("!!! [删除品类]失败: id={}", id, e);
            return Result.error("500", e.getMessage());
        }
    }

    /**
     * 查询品类列表 (支持模糊搜索)
     */
    @GetMapping("/list")
    public Result<List<SpeciesEntity>> list(@RequestParam(name = "searchKey", required = false, defaultValue = "") String searchKey) {
        log.info(">>> 收到[查询品类列表]请求: searchKey={}", searchKey);
        List<SpeciesEntity> list = speciesService.listAll(searchKey);
        return Result.success(list);
    }
}