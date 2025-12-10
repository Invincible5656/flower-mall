package org.example.trigger.http.User;

import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.UserLoginRequest;
import org.example.api.dto.UserRegisterRequest;
import org.example.api.dto.UserUpdateRequest;
import org.example.api.response.Result;
import org.example.domain.user.model.entity.UserEntity;
import org.example.domain.user.service.UserApplicationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-10 21:22
 */

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Resource
    private UserApplicationService userDomainService;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result<UserEntity> login(@RequestBody UserLoginRequest request) {

        log.info("【用户登录】收到请求: account={}", request.getAccount());

        try {
            // 调用领域服务，这里假设 login 只需要账号密码
            // 如果你的 Service 需要 Entity，这里就转成 Entity
            UserEntity user = userDomainService.login(request.getAccount(), request.getPassword(), "user");
            log.info("【用户登录】成功: id={}, account={}", user.getId(), user.getAccount());
            return Result.success(user);
        } catch (Exception e) {
            log.error("【用户登录】失败: account={}, cause={}", request.getAccount(), e.getMessage());
            return Result.error("4000", e.getMessage());
        }
    }

    /**
     * 注册接口
     */
    @PostMapping("/create")
    public Result<String> create(@RequestBody UserRegisterRequest request) {
        // 1. DTO (Request) -> Entity (Domain)
        // 因为 Domain 层不认识 DTO，所以要在 Controller 转
        log.info("【用户注册】收到请求: account={}, name={}", request.getAccount(), request.getName());

        UserEntity entity = new UserEntity();
        entity.setAccount(request.getAccount());
        entity.setPassword(request.getPassword());
        entity.setName(request.getName());
        entity.setPhone(request.getPhone());
        entity.setAddress(request.getAddress());

        try {
            userDomainService.register(entity);
            log.info("【用户注册】成功: account={}", request.getAccount());
            return Result.success("注册成功");
        } catch (Exception e) {
            log.error("【用户注册】失败: account={}, cause={}", request.getAccount(), e.getMessage(), e);
            return Result.error("4000", e.getMessage());
        }
    }

    /**
     * 更新接口
     */
    @PostMapping("/update")
    public Result<String> update(@RequestBody UserUpdateRequest request) {
        log.info("【用户信息更新】收到请求: id={}", request.getId());

        UserEntity entity = new UserEntity();
        entity.setId(request.getId()); // 更新必须有ID
        entity.setName(request.getName());
        entity.setPassword(request.getPassword());
        entity.setPhone(request.getPhone());
        entity.setAddress(request.getAddress());

        try {
            userDomainService.update(entity);
            log.info("【用户信息更新】成功: id={}", request.getId());
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("【用户信息更新】失败: id={}, cause={}", request.getId(), e.getMessage());
            return Result.error("4000", e.getMessage());
        }
    }

    /**
     * 分页查询
     */
    @GetMapping("/find")
    public Result<Map<String, Object>> find(@RequestParam("page") int page, @RequestParam("searchKey") String searchKey) {
        log.info("【用户搜索】page={}, key={}", page, searchKey);
        Map<String, Object> map = userDomainService.findByPage(page, searchKey);
        log.info("【用户搜索】完成，返回记录数: {}", ((java.util.List<?>)map.get("items")).size());
        return Result.success(map);
    }

    /**
     * 删除接口
     */
    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam("id") int id) {
        log.warn("【用户删除】收到危险请求: id={}", id);

        try {
            userDomainService.delete(id);
            log.info("【用户删除】成功: id={}", id);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("【用户删除】失败: id={}, cause={}", id, e.getMessage());
            return Result.error("4000", e.getMessage());
        }
    }
}