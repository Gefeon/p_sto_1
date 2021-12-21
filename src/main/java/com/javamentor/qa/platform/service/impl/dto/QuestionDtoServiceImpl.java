package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;

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
    public PageDto<QuestionDto> getPage(int currentPageNumber, int itemsOnPage, Map<Object, Object> map) {

       PageDto<QuestionDto> page = super.getPage(currentPageNumber, itemsOnPage, map);

        List<Long> questionIds = page.getItems().stream()
                .map(QuestionDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<TagDto>> mapQIdTags =tagDtoDao.getMapTagsByQuestionIds(questionIds);

        page.getItems().forEach(q -> q.setListTagDto(mapQIdTags.get(q.getId())));

        return page;
    }
}
