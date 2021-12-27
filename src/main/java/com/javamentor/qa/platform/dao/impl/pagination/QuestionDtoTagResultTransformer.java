package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.hibernate.transform.ResultTransformer;

import java.time.LocalDateTime;
import java.util.*;

public class QuestionDtoTagResultTransformer implements ResultTransformer {

    private Map<Long, QuestionDto> questionDtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuples, String[] aliases) {

        List<String> aliasList = Arrays.asList(aliases);

        Map<String, Object> tupleMap = aliasList.stream()
                .collect(HashMap::new, (m, a) -> m.put(a, tuples[aliasList.indexOf(a)]),
                        HashMap::putAll);

        Long questionId = (Long) tupleMap.get("q_id");
        String questionTitle = (String) tupleMap.get("q_title");
        Long questionAuthorId = (Long) tupleMap.get("q_author_id");
        String questionAuthorName = (String) tupleMap.get("q_author_name");
        String questionAuthorImage = (String) tupleMap.get("q_author_image");
        String questionDescription = (String) tupleMap.get("q_description");
        Long questionViewCount = (Long) tupleMap.get("q_view_count");
        Long questionCountAnswer = (Long) tupleMap.get("q_count_answer");
        Long questionCountValuable = (Long) tupleMap.get("q_count_valuable");
        Long questionAuthorReputation = (Long) tupleMap.get("q_author_reputation");
        LocalDateTime questionPersistDateTime = (LocalDateTime) tupleMap.get("q_persist_date_time");
        LocalDateTime questionLastUpdateDate = (LocalDateTime) tupleMap.get("q_last_update_datetime");

        QuestionDto questionDto = questionDtoMap.computeIfAbsent(questionId, id ->
                new QuestionDto(questionId, questionTitle, questionAuthorId,
                        questionAuthorName, questionAuthorImage, questionDescription,
                        questionViewCount, questionCountAnswer, questionCountValuable, questionAuthorReputation, questionPersistDateTime, questionLastUpdateDate));

        return questionDto;
    }

    @Override
    public List transformList(List list) {
        return new ArrayList(questionDtoMap.values());
    }
}
