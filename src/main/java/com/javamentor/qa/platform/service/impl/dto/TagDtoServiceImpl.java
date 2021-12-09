package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagDtoServiceImpl implements TagDtoService {

    private final TagDtoDao tagDao;

    public TagDtoServiceImpl(TagDtoDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<TagDto> getTagsByFirstLetters(String letters) {
        return tagDao.getTagsByFirstLetters(letters);
    }
}
