package com.javamentor.qa.platform.models.dto;

import org.hibernate.transform.ResultTransformer;

import java.util.*;

public class QuestionDtoResultTransformer implements ResultTransformer {

    private Map<Long, QuestionDto> questionDtoMap = new LinkedHashMap<>();
    @Override
    public Object transformTuple(Object[] tuples, String[] aliases) {
        List<String> aliasList = Arrays.asList(aliases);

        Map<String, Object> tupleMap = aliasList.stream()
                .collect(
                        HashMap::new, (m, a) -> m.put(a, tuples[aliasList.indexOf(a)]), HashMap::putAll);

        Long questionId = (Long) tupleMap.get(QuestionDto.ID_ALIAS);
        String title = (String) tupleMap.get(QuestionDto.TITLE_ALIAS);
        Long authorId = (Long) tupleMap.get(QuestionDto.AUTHOR_ID_ALIAS);
        String authorName = (String) tupleMap.get(QuestionDto.NAME_ALIAS);
        String authorImage = (String) tupleMap.get(QuestionDto.IMAGE_ALIAS);
        String description = (String) tupleMap.get(QuestionDto.DESCRIPTION_ALIAS);
        int viewCount = (int) tupleMap.get(QuestionDto.VIEW_COUNT_ALIAS);
        int countAnswer = (int) tupleMap.get(QuestionDto.COUNT_ANSWER_ALIAS);
        int countValuable = (int) tupleMap.get(QuestionDto.COUNT_VALUABLE_ALIAS);
        QuestionDto questionDto = questionDtoMap.computeIfAbsent(
                questionId,
                id -> new QuestionDto(questionId, title, authorId, authorName, authorImage, description, viewCount,countAnswer,countValuable)
        );
        if (tupleMap.get(QuestionDto.ID_ALIAS) != null) {
            TagDto tagDto = new TagDto((Long) tupleMap.get(TagDto.ID_ALIAS), (String) tupleMap.get(TagDto.NAME_ALIAS),(String) tupleMap.get(TagDto.DESCRIPTION_ALIAS));
            if (!questionDto.getListTagDto().contains(tagDto))
                questionDto.getListTagDto().add(tagDto);
        }
        return questionDto;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList(questionDtoMap.values());
    }

}
