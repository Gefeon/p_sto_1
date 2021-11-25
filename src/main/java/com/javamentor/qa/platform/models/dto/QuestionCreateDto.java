package com.javamentor.qa.platform.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Component
@Data
@NoArgsConstructor
public class QuestionCreateDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotEmpty
    private List<TagDto> tags;
}
