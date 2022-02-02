package com.javamentor.qa.platform.models.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IgnoredTagDto {
    private Long id;
    private String name;
    private String description;
}
