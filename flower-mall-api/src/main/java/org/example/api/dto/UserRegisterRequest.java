package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-10 20:46
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest{
    /** 账号 (必填) */
    private String account;

    /** 昵称 (必填) */
    private String name;

    /** 密码 (必填) */
    private String password;

    /** 电话 (必填，鲜花配送需要) */
    private String phone;

    /** 地址 (必填，鲜花配送需要) */
    private String address;
}
