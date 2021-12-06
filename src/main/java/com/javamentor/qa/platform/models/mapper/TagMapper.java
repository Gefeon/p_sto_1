package com.javamentor.qa.platform.models.mapper;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag tag);

    Tag toModel(TagDto tagDto);

    List<TagDto> toDto(List<Tag> tag);

    List<Tag> toModel(List<TagDto> tagDto);

}
