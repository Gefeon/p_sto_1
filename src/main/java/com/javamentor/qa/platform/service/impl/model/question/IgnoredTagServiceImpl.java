package com.javamentor.qa.platform.service.impl.model.question;

import com.javamentor.qa.platform.dao.abstracts.model.question.IgnoredTagDao;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.service.abstracts.model.question.IgnoredTagService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IgnoredTagServiceImpl extends ReadWriteServiceImpl<IgnoredTag, Long> implements IgnoredTagService {

    private final IgnoredTagDao ignoredTagDao;

    public IgnoredTagServiceImpl(IgnoredTagDao ignoredTagDao) {
        super(ignoredTagDao);
        this.ignoredTagDao = ignoredTagDao;
    }

    @Transactional
    @Override
    public Optional<IgnoredTag> getIgnoredTagByName(Long id, String name) {
        return ignoredTagDao.getIgnoredTagByName(id, name);
    }
}
