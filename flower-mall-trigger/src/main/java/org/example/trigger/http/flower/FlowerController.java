package org.example.trigger.http.flower;

import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.FlowerAddRequest;
import org.example.api.dto.FlowerUpdateRequest;
import org.example.api.response.Result;
import org.example.domain.flower.model.entity.FlowerEntity;
import org.example.domain.flower.service.FlowerApplicationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lanjiajun
 * @description 鲜花管理接口 (Trigger Layer)
 * 职责：解析 HTTP 请求 -> 调用 App Service -> 返回 JSON
 * @create 2025-12-09 17:43
 */
@Slf4j
@RestController
@RequestMapping("/api/flower")
@CrossOrigin
public class FlowerController {

    @Resource
    private FlowerApplicationService flowerApplicationService;

    /**
     * 新增鲜花
     * POST /api/flower/add
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody FlowerAddRequest flowerAddRequest) {
        log.info("接收到新增鲜花请求：{}", flowerAddRequest.getName());

        //调用app层
        flowerApplicationService.addFlower(
                flowerAddRequest.getName(),
                flowerAddRequest.getPrice(),
                flowerAddRequest.getSpeciesName(),
                flowerAddRequest.getDetail()
        );
        log.info("完成新增鲜花请求：{}", flowerAddRequest.getName());
        return Result.success();
    }

    /**
     * 修改鲜花信息
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody FlowerUpdateRequest request) {
        log.info("接收到修改鲜花请求: id={}", request.getId());

        flowerApplicationService.updateFlower(
                request.getId(),
                request.getName(),
                request.getPrice(),
                request.getSpeciesName()
        );
        return Result.success();
    }

    /**
     * 单独修改图片
     */
    @PostMapping("/update/image")
    public Result<Void> updateImage(@RequestParam Long id, @RequestParam String imgGuid) {
        flowerApplicationService.updateFlowerImage(id, imgGuid);
        return Result.success();
    }

    /**
     * 删除鲜花
     */
    @PostMapping("/delete") // 或者用 @DeleteMapping("/{id}")
    public Result<Void> delete(@RequestParam Integer id) {
        log.info("接收到删除鲜花请求：{}", id);
        flowerApplicationService.deleteFlower(id);
        log.info("完成删除鲜花请求：{}", id);
        return Result.success();
    }

    /**
     * 查询列表
     * 直接返回 Entity，标准做法是转成 FlowerVO 再返回
     */
    @GetMapping("/list")
    public Result<List<FlowerEntity>> list(@RequestParam(required = false) String key,
                                           @RequestParam(required = false) String type) {
        log.info("接收到查询鲜花请求");
        List<FlowerEntity> list = flowerApplicationService.searchFlowers(key, type);
        log.info("完成查询鲜花请求");
        return Result.success(list);
    }
}
