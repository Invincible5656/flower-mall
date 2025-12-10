package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-10 20:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest  {
    /**
     * 用户ID (核心字段，用于定位修改谁)
     */
    private Integer id;

    /**
     * 新昵称
     */
    private String name;

    /**
     * 新密码
     */
    private String password;

    /**
     * 新电话
     */
    private String phone;

    /**
     * 新地址
     */
    private String address;
}
