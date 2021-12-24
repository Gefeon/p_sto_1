package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {
    public static final String ID_ALIAS = "t_id";
    public static final String NAME_ALIAS = "t_name";
    public static final String DESCRIPTION_ALIAS = "t_description";

    private Long id;
    private String name;
    private String description;
}
