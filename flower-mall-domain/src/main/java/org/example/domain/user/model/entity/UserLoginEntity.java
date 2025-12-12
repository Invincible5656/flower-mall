package org.example.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-12 23:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginEntity implements Serializable {
    private String token;
    private Object userInfo; // 这里用 UserEntity
}
