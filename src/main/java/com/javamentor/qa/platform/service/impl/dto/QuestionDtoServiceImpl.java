package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
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
    private  final CommentDtoDao commentDtoDao;

    public QuestionDtoServiceImpl(QuestionDtoDao questionDao, TagDtoDao tagDtoDao, CommentDtoDao commentDtoDao) {
        this.questionDao = questionDao;
        this.tagDtoDao = tagDtoDao;
        this.commentDtoDao = commentDtoDao;
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
        Map<Long, List<CommentDto>> commentsMap = commentDtoDao.getMapCommentsByQuestionIds(questionIds);
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
        questionDto.ifPresent(dto -> dto.setListCommentsDto(commentDtoDao.getCommentDtoListByQuestionId(id)));
        return questionDto;
    }


    @Override
    @Transactional
    public List<QuestionCommentDto> getQuestionCommentDtoById(Long id) {
        return commentDtoDao.getQuestionCommentDtoById(id);
    }
}
