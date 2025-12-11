package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanjiajun
 * @description
 * @create 2025-12-11 19:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpeciesCreateRequest {
    private String name;
}
