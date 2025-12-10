package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lanjiajun
 * @description 登录请求信息
 * @create 2025-12-10 20:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest{
    /** 账号 */
    private String account;

    /** 密码 */
    private String password;

    /** 角色 (user/admin)，如果前端不传，可由后端默认处理 */
    private String role;
}
