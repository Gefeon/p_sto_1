package com.javamentor.qa.platform.service.abstracts.model.question;

import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;

import java.util.List;
import java.util.Optional;

public interface TrackedTagService extends ReadWriteService<TrackedTag, Long> {

    Optional<TrackedTag> getTrackedTagByName(Long id, String name);
}
