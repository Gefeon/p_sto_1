package com.javamentor.qa.platform.models.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackedTagDto {
    private Long id;
    private String name;
    private String description;
}
