package com.javamentor.qa.platform.models.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagViewDto {
    private Long id;
    private String name;
    private String description;
    private Long questionCount;
    private Long questionCountOneDay;
    private Long questionCountOneWeek;
}
