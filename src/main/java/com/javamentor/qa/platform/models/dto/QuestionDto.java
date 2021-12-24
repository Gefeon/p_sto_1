package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    public static final String ID_ALIAS = "q_id";
    public static final String TITLE_ALIAS = "q_title";
    public static final String AUTHOR_ID_ALIAS = "q_author_id";
    public static final String AUTHOR_NAME_ALIAS = "q_author_name";
    public static final String AUTHOR_IMAGE_ALIAS = "q_author_image";
    public static final String DESCRIPTION_ALIAS = "q_description";
    public static final String VIEW_COUNT_ALIAS = "q_view_count";
    public static final String COUNT_ANSWER_ALIAS = "q_count_answer";
    public static final String COUNT_VALUABLE_ALIAS = "q_count_valuable";
    public static final String AUTHOR_REPUTATION_ALIAS = "q_author_reputation";
    public static final String PERSIST_DATE_TIME_ALIAS = "q_persist_date_time";
    public static final String LAST_UPDATE_DATE_TIME_ALIAS = "q_last_update_datetime";
    public static final String LIST_TAG_DTO_ALIAS = "q_tag_dto";

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
    private List<TagDto> listTagDto = new ArrayList<>();

    public QuestionDto(Long id, String title, Long authorId, String authorName, String authorImage, String description, Long viewCount, Long countAnswer, Long countValuable, Long authorReputation, LocalDateTime persistDateTime, LocalDateTime lastUpdateDateTime) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorImage = authorImage;
        this.description = description;
        this.viewCount = viewCount;
        this.countAnswer = countAnswer;
        this.countValuable = countValuable;
        this.authorReputation = authorReputation;
        this.persistDateTime = persistDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public QuestionDto(Long id, String title, Long authorId, Long authorReputation, String authorName,
                       String authorImage, String description, Long viewCount, Long countAnswer, Long countValuable,
                       LocalDateTime persistDateTime, LocalDateTime lastUpdateDateTime) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorReputation = authorReputation;
        this.authorName = authorName;
        this.authorImage = authorImage;
        this.description = description;
        this.viewCount = viewCount;
        this.countAnswer = countAnswer;
        this.countValuable = countValuable;
        this.persistDateTime = persistDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public QuestionDto(Long id, String title, Long authorId,
                       String authorName, String authorImage, Long authorReputation,
                       String description, LocalDateTime persistDateTime,
                       LocalDateTime lastUpdateDateTime, Long viewCount,
                       Long countAnswer, Long countValuable) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorImage = authorImage;
        this.authorReputation = authorReputation;
        this.description = description;
        this.persistDateTime = persistDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.viewCount = viewCount;
        this.countAnswer = countAnswer;
        this.countValuable = countValuable;
    }


}
