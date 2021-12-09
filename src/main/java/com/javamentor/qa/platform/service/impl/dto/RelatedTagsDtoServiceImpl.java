package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.RelatedTagsDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagsDto;
import com.javamentor.qa.platform.service.abstracts.dto.RelatedTagsDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelatedTagsDtoServiceImpl implements RelatedTagsDtoService {

    @Autowired
    private RelatedTagsDtoDao relatedTagsDtoDao;

    @Override
    @Transactional
    public List<RelatedTagsDto> getRelatedTagsDto() {
        return relatedTagsDtoDao.getRelatedTagsDto();
    }
}
