package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.TrackedTagDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.question.TrackedTagService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrackedTagServiceImpl extends ReadWriteServiceImpl<TrackedTag, Long> implements TrackedTagService {

    private final TrackedTagDao trackedTagDao;

    public TrackedTagServiceImpl(TrackedTagDao trackedTagDao) {
        super(trackedTagDao);
        this.trackedTagDao = trackedTagDao;
    }

    @Transactional
    @Override
    public Optional<TrackedTag> getTrackedTagByName(Long id, String name) {
        return trackedTagDao.getTrackedTagByName(id, name);
    }
}
