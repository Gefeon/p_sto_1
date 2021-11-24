package com.javamentor.qa.platform.models.mapper;


import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.question.QuestionViewedService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", uses = {TagMapper.class})
public abstract class QuestionMapper {

    @Autowired
    protected QuestionViewedService questionViewedService;

    @Mapping(source = "question.tags", target = "listTagDto", ignore = true)
    @Mapping(source = "question.id", target = "id")
    @Mapping(source = "question.persistDateTime", target = "persistDateTime")
    @Mapping(source = "question.lastUpdateDateTime", target = "lastUpdateDateTime")
    @Mapping(target = "countAnswer", expression = "java(question.getAnswers().size())")
    @Mapping(target = "countValuable", expression = "java((int) question.getVoteQuestions().stream().map(question1 -> question1.getVote()).mapToInt(i -> i.ordinal()).sum())")
    @Mapping(target = "viewCount", expression = "java((int)questionViewedService.getAll().stream().filter(questionViewed1 -> questionViewed1.getQuestion().getId() == question.getId()).count())")
    @Mapping( source = "question.user.id", target = "authorId")
    @Mapping( source = "question.user.fullName", target = "authorName")
    @Mapping( source = "question.user.imageLink", target = "authorImage")
    public abstract QuestionDto toDto(Question question);


    public abstract Question toModel(QuestionCreateDto questionCreateDto);

//    List<UserDTO> toDto(List<User> users);
//
//    List<User> toModel(List<UserDTO> userDTO);
}
