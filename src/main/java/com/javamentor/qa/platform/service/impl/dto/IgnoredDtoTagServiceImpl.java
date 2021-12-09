package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.IgnoredTagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.IgnoredTagDtoService;
import com.javamentor.qa.platform.service.impl.model.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgnoredDtoTagServiceImpl implements IgnoredTagDtoService {

    private final IgnoredTagDtoDao ignoredTagDao;

    public IgnoredDtoTagServiceImpl( IgnoredTagDtoDao ignoredTagDao) {
        this.ignoredTagDao = ignoredTagDao;
    }

    @Override
    public List<TagDto> getTagsByUserId(Long id) {
        return ignoredTagDao.getTagsByUserId(id);
    }
}
