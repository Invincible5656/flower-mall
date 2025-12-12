package org.example.trigger.http.User;

import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.UserLoginRequest;
import org.example.api.dto.UserRegisterRequest;
import org.example.api.dto.UserUpdateRequest;
import org.example.api.response.Result;
import org.example.domain.user.model.entity.UserEntity;
import org.example.domain.user.model.entity.UserLoginEntity;
import org.example.domain.user.service.UserApplicationService;
import org.example.types.Annotaton.AdminOnly;
import org.example.types.common.UserContext;
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
    public Result<UserLoginEntity> login(@RequestBody UserLoginRequest request) {

        log.info("【用户登录】收到请求: account={}", request.getAccount());

        try {
            // 调用领域服务，这里假设 login 只需要账号密码
            // 如果你的 Service 需要 Entity，这里就转成 Entity
            UserLoginEntity response = userDomainService
                    .login(request.getAccount(),
                            request.getPassword(),
                            request.getRole());
            log.info("【用户登录】成功: account={}", request.getAccount());
            return Result.success(response);
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
     * 从 UserContext.getUserId() 获取当前用户ID，
     * 防止用户恶意修改 request.getId() 来更新别人的信息。
     *（测试的时候发现这种问题了，立刻修改！！）
     */
    @PostMapping("/update")
    public Result<String> update(@RequestBody UserUpdateRequest request) {
        // 1. 获取当前登录用户的真实ID (这是从 Token 解析出来的，绝对可信)
        String userIdStr = UserContext.getUserId();

        if (userIdStr == null) {
            return Result.error("4001", "未登录或Token失效");
        }

        // 假设数据库 ID 是 Integer 类型，这里需要转换一下
        Integer currentUserId = Integer.parseInt(userIdStr);

        log.info("【用户信息更新】收到请求，操作人ID={}", currentUserId);

        UserEntity entity = new UserEntity();

        // 【关键点】强制设置 ID 为当前登录用户 ID
        // 无论前端传的 request.getId() 是多少，都覆盖掉，只改自己！
        entity.setId(currentUserId);

        entity.setName(request.getName());
        entity.setPassword(request.getPassword());
        entity.setPhone(request.getPhone());
        entity.setAddress(request.getAddress());

        try {
            userDomainService.update(entity);
            log.info("【用户信息更新】成功: id={}", currentUserId);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("【用户信息更新】失败: id={}, cause={}", currentUserId, e.getMessage());
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