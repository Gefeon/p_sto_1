package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor

public class QuestionDto {
    public static final String ID_ALIAS = "question_id";
    public static final String TITLE_ALIAS = "question_title";
    public static final String AUTHOR_ID_ALIAS = "question_author_id";
    public static final String NAME_ALIAS = "question_author_name";
    public static final String IMAGE_ALIAS = "question_author_image";
    public static final String DESCRIPTION_ALIAS = "question_description";
    public static final String VIEW_COUNT_ALIAS = "question_view_count";
    public static final String COUNT_ANSWER_ALIAS = "question_count_answer";
    public static final String COUNT_VALUABLE_ALIAS = "question_count_valuable";
//    public static final String PERSIST_DATE_ALIAS = "question_persist_date";
//    public static final String LAST_UPDATE_DATE_ALIAS = "question_last_update_date";

    public QuestionDto() {
    }

    public QuestionDto(Long id, String title, Long authorId, String authorName, String authorImage, String description, int viewCount, int countAnswer, int countValuable) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorImage = authorImage;
        this.description = description;
        this.viewCount = viewCount;
        this.countAnswer = countAnswer;
        this.countValuable = countValuable;
    }

    public QuestionDto(Object[] tuples, Map<String, Integer> aliasToIndexMap) {
        this.id = ((Number) tuples[aliasToIndexMap.get(ID_ALIAS)]).longValue();
        this.title = tuples[aliasToIndexMap.get(TITLE_ALIAS)].toString();
        this.authorId = ((Number) tuples[aliasToIndexMap.get(AUTHOR_ID_ALIAS)]).longValue();
        this.authorName = tuples[aliasToIndexMap.get(NAME_ALIAS)].toString();
        this.authorImage = tuples[aliasToIndexMap.get(IMAGE_ALIAS)].toString();
        this.description = tuples[aliasToIndexMap.get(DESCRIPTION_ALIAS)].toString();
        this.viewCount = ((Number) tuples[aliasToIndexMap.get(VIEW_COUNT_ALIAS)]).intValue();
        this.countAnswer = ((Number) tuples[aliasToIndexMap.get(COUNT_ANSWER_ALIAS)]).intValue();
        this.countValuable = ((Number) tuples[aliasToIndexMap.get(COUNT_VALUABLE_ALIAS)]).intValue();
//        this.persistDateTime = ((LocalDateTime) tuples[aliasToIndexMap.get(PERSIST_DATE_ALIAS)]);
//        this.lastUpdateDateTime = ((LocalDateTime) tuples[aliasToIndexMap.get(LAST_UPDATE_DATE_ALIAS)]);
    }

    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private String authorImage;
    private String description;
    private int viewCount;
    private int countAnswer;
    private int countValuable;
    //    private LocalDateTime persistDateTime;
//    private LocalDateTime lastUpdateDateTime;
    private List<TagDto> listTagDto = new ArrayList<>();
}
