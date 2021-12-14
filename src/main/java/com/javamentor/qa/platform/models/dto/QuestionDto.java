package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class QuestionDto implements Serializable {
    private Long id;
    private String title;
    private Long authorId;
    private Long authorReputation;
    private String authorName;
    private String authorImage;
    private String description;
    private int viewCount;
    private long viewCountLong;
    private int countAnswer;
    private long countAnswerLong;
    private int countValuable;
    private long countValuableLong;
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
        this.viewCountLong = viewCountLong;
        this.viewCount = (int)viewCountLong;
        this.countAnswerLong = countAnswerLong;
        this.countAnswer = (int) countAnswerLong;
        this.countValuableLong = countValuableLong;
        this.countValuable = (int) countValuableLong;
        this.persistDateTime = persistDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
    }
}
