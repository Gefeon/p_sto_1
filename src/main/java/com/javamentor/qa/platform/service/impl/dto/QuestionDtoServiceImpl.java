package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.*;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionDtoServiceImpl extends PageDtoServiceImpl<QuestionViewDto> implements QuestionDtoService {

    private final QuestionDtoDao questionDao;
    private final TagDtoDao tagDtoDao;
    private  final CommentDtoDao commentDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDao questionDao, TagDtoDao tagDtoDao, CommentDtoDao commentDtoDao) {
        this.questionDao = questionDao;
        this.tagDtoDao = tagDtoDao;
        this.commentDtoDao = commentDtoDao;
    }

    @Override
    @Transactional
    public PageDto<QuestionViewDto> getPage(int currentPageNumber, int itemsOnPage, Map<Object, Object> map) {

        PageDto<QuestionViewDto> pageDto = super.getPage(currentPageNumber, itemsOnPage, map);
        List<QuestionViewDto> questionViewDtos = pageDto.getItems();

        List<Long> questionIds = questionViewDtos.stream()
                .map(QuestionViewDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<TagDto>> tagsMap = tagDtoDao.getMapTagsByQuestionIds(questionIds);
        for (QuestionViewDto questionViewDto : questionViewDtos) {
            questionViewDto.setListTagDto(tagsMap.get(questionViewDto.getId()));
        }
        pageDto.setItems(questionViewDtos);
        return pageDto;
    }

    @Override
    @Transactional
    public Optional<QuestionDto> getQuestionDtoByIdAndUserAuthId(long id, long userId) {
        Optional<QuestionDto> questionDto = questionDao.getQuestionDtoByIdAndUserAuthId(id, userId);
        questionDto.ifPresent(dto -> dto.setListTagDto(tagDtoDao.getTagDtoListByQuestionId(id)));
        questionDto.ifPresent(dto -> dto.setListCommentsDto(commentDtoDao.getCommentDtoListByQuestionId(id)));
        return questionDto;
    }


    @Override
    @Transactional
    public List<QuestionCommentDto> getQuestionCommentDtoById(Long id) {
        return commentDtoDao.getQuestionCommentDtoById(id);
    }
}
