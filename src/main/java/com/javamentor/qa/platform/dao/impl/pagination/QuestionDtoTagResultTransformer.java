package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.hibernate.transform.ResultTransformer;

import java.time.LocalDateTime;
import java.util.*;

public class QuestionDtoTagResultTransformer implements ResultTransformer {

    private Map<Long, QuestionDto> questionDtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuples, String[] aliases) {

        List<String> aliasList = Arrays.asList(aliases);

        Map<String, Object> tupleMap = aliasList.stream()
                .collect(HashMap::new,(m, a) -> m.put(a, tuples[aliasList.indexOf(a)]),
                        HashMap::putAll);

        Long questionId = (Long) tupleMap.get(QuestionDto.ID_ALIAS);
        String questionTitle = (String) tupleMap.get(QuestionDto.TITLE_ALIAS);
        Long questionAuthorId = (Long) tupleMap.get(QuestionDto.AUTHOR_ID_ALIAS);
        String questionAuthorName = (String) tupleMap.get(QuestionDto.AUTHOR_NAME_ALIAS);
        String questionAuthorImage = (String) tupleMap.get(QuestionDto.AUTHOR_IMAGE_ALIAS);
        String questionDescription= (String) tupleMap.get(QuestionDto.DESCRIPTION_ALIAS);
        Long questionViewCount = (Long) tupleMap.get(QuestionDto.VIEW_COUNT_ALIAS);
        Long questionCountAnswer = (Long) tupleMap.get(QuestionDto.COUNT_ANSWER_ALIAS);
        Long questionCountValuable = (Long) tupleMap.get(QuestionDto.COUNT_VALUABLE_ALIAS);
        Long questionAuthorReputation = (Long) tupleMap.get(QuestionDto.AUTHOR_REPUTATION_ALIAS);
        LocalDateTime questionPersistDateTime = (LocalDateTime) tupleMap.get(QuestionDto.PERSIST_DATE_TIME_ALIAS);
        LocalDateTime questionLastUpdateDate = (LocalDateTime) tupleMap.get(QuestionDto.LAST_UPDATE_DATE_TIME_ALIAS);

        QuestionDto questionDto = questionDtoMap.computeIfAbsent(questionId, id ->
                new QuestionDto(questionId, questionTitle, questionAuthorId,
                        questionAuthorName, questionAuthorImage, questionDescription,
                        questionViewCount, questionCountAnswer, questionCountValuable, questionAuthorReputation, questionPersistDateTime, questionLastUpdateDate));
        if (tupleMap.get(TagDto.ID_ALIAS) != null) {
            TagDto tagDto = new TagDto((Long) tupleMap.get(TagDto.ID_ALIAS), (String) tupleMap.get(TagDto.NAME_ALIAS), (String) tupleMap.get(TagDto.DESCRIPTION_ALIAS));
            if (!questionDto.getListTagDto().contains(tagDto)) {
                questionDto.getListTagDto().add(tagDto);
            }
        }
        return questionDto;
    }

    @Override
    public List transformList(List list) {
        return new ArrayList(questionDtoMap.values());
    }
}
