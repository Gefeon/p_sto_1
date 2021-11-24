package com.javamentor.qa.platform.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class TagDto {
    private Long id;
    private String name;
    private String description;
}
