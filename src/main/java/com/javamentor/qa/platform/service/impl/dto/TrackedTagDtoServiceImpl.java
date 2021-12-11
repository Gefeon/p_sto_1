package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TrackedTagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TrackedTagDtoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackedTagDtoServiceImpl implements TrackedTagDtoService {

    private final TrackedTagDtoDao trackedTagDao;

    public TrackedTagDtoServiceImpl(TrackedTagDtoDao trackedTagDao) {
        this.trackedTagDao = trackedTagDao;
    }

    @Override
    public List<TagDto> getTagsByUserId(Long id) {
        return trackedTagDao.getTagsByUserId(id);
    }
}
