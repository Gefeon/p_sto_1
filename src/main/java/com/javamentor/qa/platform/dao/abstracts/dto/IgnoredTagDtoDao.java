package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.List;

public interface IgnoredTagDtoDao extends ReadWriteDao<IgnoredTag, Long> {
    List<TagDto> getTagsByUser(User user);
}
