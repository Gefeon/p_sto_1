package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;

import java.util.List;

public interface TagDtoService extends PageDtoService<TagDto> {

    List<TagDto> getTagsByLetters(String letters);

    List<TagDto> getTrackedTagsByUserId(Long id);

    List<TagDto> getIgnoredTagsByUserId(Long id);

    List<RelatedTagsDto> getRelatedTagsDto();
}
