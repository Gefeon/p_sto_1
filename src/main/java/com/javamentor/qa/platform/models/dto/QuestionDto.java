package com.javamentor.qa.platform.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class QuestionDto implements Serializable {
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

    public QuestionDto(Long id, String title, Long authorId, Long authorReputation, String authorName,
                       String authorImage, String description, long viewCountLong, long countAnswerLong, long countValuableLong,
                       LocalDateTime persistDateTime, LocalDateTime lastUpdateDateTime) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorReputation = authorReputation;
        this.authorName = authorName;
        this.authorImage = authorImage;
        this.description = description;
        this.viewCount = viewCountLong;
        this.countAnswer = countAnswerLong;
        this.countValuable = countValuableLong;
        this.persistDateTime = persistDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
    }
}
