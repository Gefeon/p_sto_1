package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.TrackedTagDao;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.service.abstracts.model.question.TrackedTagService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TrackedTagServiceImpl extends ReadWriteServiceImpl<TrackedTag, Long> implements TrackedTagService {
    public TrackedTagServiceImpl(TrackedTagDao trackedTagDao) {
        super(trackedTagDao);
    }
}
