package com.javamentor.qa.platform.models.mapper;


import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {TagMapper.class})
public abstract class QuestionMapper {

    @Mapping(source = "question.tags", target = "listTagDto")
    @Mapping(source = "question.id", target = "id")
//    @Mapping(source = "question.persistDateTime", target = "persistDateTime")
//    @Mapping(source = "question.lastUpdateDateTime", target = "lastUpdateDateTime")
    @Mapping(target = "countAnswer", constant = "0")
    @Mapping(target = "countValuable", constant = "0")
    @Mapping(target = "viewCount", constant = "0")
    @Mapping( source = "question.user.id", target = "authorId")
    @Mapping( source = "question.user.fullName", target = "authorName")
    @Mapping( source = "question.user.imageLink", target = "authorImage")
    public abstract QuestionDto persistConvertToDto(Question question);

    public abstract Question toModel(QuestionCreateDto questionCreateDto);
    
}
