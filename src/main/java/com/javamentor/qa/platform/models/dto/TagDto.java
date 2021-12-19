package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {
    public static final String ID_ALIAS = "tag_id";
    public static final String NAME_ALIAS = "tag_name";
    public static final String DESCRIPTION_ALIAS = "tag_description";

    private Long id;
    private String name;
    private String description;
}
