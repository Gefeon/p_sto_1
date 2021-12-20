package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class QuestionDto {


    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private String authorImage;
    private String description;
    private Long viewCount;
    private Long countAnswer;
    private Long countValuable;
    private Long authorReputation;
    private LocalDateTime persistDateTime;
    private LocalDateTime lastUpdateDateTime;
    private List<TagDto> listTagDto;

    public QuestionDto(Long id, String title, String description,
                        LocalDateTime persistDateTime, LocalDateTime lastUpdateDateTime, Long authorId,
                       String authorName, String authorImage, Long authorReputation, Long countValuable,
                       Long countAnswer
                        ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.persistDateTime = persistDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorImage = authorImage;
        this.authorReputation = authorReputation;
        this.countValuable = countValuable;
        this.countAnswer= countAnswer;


    }
}