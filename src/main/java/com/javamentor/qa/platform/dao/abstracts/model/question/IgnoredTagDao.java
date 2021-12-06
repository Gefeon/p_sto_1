package com.javamentor.qa.platform.dao.abstracts.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.List;

public interface IgnoredTagDao extends ReadWriteDao<IgnoredTag, Long> {
    List<Tag> getTagsByUser(User user);
}
