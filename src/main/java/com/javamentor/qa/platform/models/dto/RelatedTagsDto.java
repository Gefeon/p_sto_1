package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RelatedTagsDto {
    private Long id;
    private String title;
    private Long countQuestion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelatedTagsDto that = (RelatedTagsDto) o;
        return id.equals(that.id) && title.equals(that.title) && countQuestion.equals(that.countQuestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, countQuestion);
    }
}
