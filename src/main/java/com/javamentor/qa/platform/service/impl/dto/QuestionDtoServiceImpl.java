package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentQuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionDtoServiceImpl extends PageDtoServiceImpl<QuestionDto> implements QuestionDtoService {

    private final QuestionDtoDao questionDao;
    private final TagDtoDao tagDtoDao;
    private final CommentQuestionDtoDao commentQuestionDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDao questionDao, TagDtoDao tagDtoDao1, CommentQuestionDtoDao commentQuestionDtoDao) {
        this.questionDao = questionDao;
        this.tagDtoDao = tagDtoDao1;
        this.commentQuestionDtoDao = commentQuestionDtoDao;
    }

    @Override
    @Transactional
    public PageDto<QuestionDto> getPage(int currentPageNumber, int itemsOnPage, Map<Object, Object> map) {

        PageDto<QuestionDto> pageDto = super.getPage(currentPageNumber, itemsOnPage, map);
        List<QuestionDto> questionDtos = pageDto.getItems();

        List<Long> questionIds = questionDtos.stream()
                .map(QuestionDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<TagDto>> tagsMap = tagDtoDao.getMapTagsByQuestionIds(questionIds);
        Map<Long, List<CommentDto>> commentsMap = commentQuestionDtoDao.getMapCommentsByQuestionIds(questionIds);
        for (QuestionDto questionDto : questionDtos) {
            questionDto.setListTagDto(tagsMap.get(questionDto.getId()));
            questionDto.setListCommentsDto(commentsMap.get(questionDto.getId()));
        }
        pageDto.setItems(questionDtos);
        return pageDto;
    }

    @Override
    @Transactional
    public Optional<QuestionDto> getQuestionDtoById(long id) {
        Optional<QuestionDto> questionDto = questionDao.getQuestionDtoById(id);
        questionDto.ifPresent(dto -> dto.setListTagDto(tagDtoDao.getTagDtoListByQuestionId(id)));
        questionDto.ifPresent(dto -> dto.setListCommentsDto(commentQuestionDtoDao.getCommentDtoListByQuestionId(id)));
        return questionDto;
    }
}
