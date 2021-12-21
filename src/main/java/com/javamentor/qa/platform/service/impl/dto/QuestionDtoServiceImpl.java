package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuestionDtoServiceImpl extends PageDtoServiceImpl<QuestionDto> implements QuestionDtoService {

    private final TagDtoDao tagDtoDao;

    public QuestionDtoServiceImpl(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public PageDto<QuestionDto> getPage(int currentPageNumber, int itemsOnPage, Map<Object, Object> map) {

        PageDtoDao<QuestionDto> pageDtoDao = (PageDtoDao<QuestionDto>) pageDtoDaoMap.get((String) map.get("class"));

        map.put("currentPageNumber", currentPageNumber);
        map.put("itemsOnPage", itemsOnPage);

        long totalResultCount = pageDtoDao.getTotalResultCount(map);

        int totalPageCount = (int) Math.ceil((double) totalResultCount / itemsOnPage);

        List<QuestionDto> questionDtos = pageDtoDao.getItems(map);

        List<Long> questionIds = questionDtos.stream()
                .map(QuestionDto::getId)
                .collect(Collectors.toList());

        List<Tuple> tags = tagDtoDao.getTagsTupleByQuestionIds(questionIds);

        Map<Long, List<TagDto>> tagsMap = new HashMap<>();
        tags.forEach(tuple -> {
            tagsMap.computeIfAbsent(tuple.get("question_id", Long.class), id -> new ArrayList<>())
                    .add(new TagDto(tuple.get("tag_id", Long.class), tuple.get("tag_name", String.class), tuple.get("tag_description", String.class)));
        });

        for (QuestionDto questionDto : questionDtos) {
            questionDto.setListTagDto(tagsMap.get(questionDto.getId()));
        }

        return new PageDto<>(currentPageNumber, totalPageCount,
                itemsOnPage, totalResultCount, questionDtos);
    }
}
