package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.models.dto.TagDto;

import java.util.List;
import java.util.Map;

public interface TagDtoDao {

    List<TagDto> getTrackedTagsByUserId(Long id);

    List<TagDto> getIgnoredTagsByUserId(Long id);

    List<RelatedTagsDto> getRelatedTagsDto();

    List<TagDto> getTagsByLetters(String letters);

    Map<Long, List<TagDto>> getMapTagsByQuestionIds(List<Long> questionIds);

    List<TagDto> getTagDtoListByQuestionId(Long id);
}
