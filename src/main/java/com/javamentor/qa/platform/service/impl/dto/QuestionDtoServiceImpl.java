package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QuestionDtoServiceImpl implements QuestionDtoService {

    private PageDtoService<QuestionDto> pageDtoService;

    @Autowired
    public void setPageDtoService(PageDtoService<QuestionDto> pageDtoService) {
        this.pageDtoService = pageDtoService;
    }

    @Override
    public PageDto<QuestionDto> getPage(int currentPageNumber, int itemsOnPage, Map<Object, Object> map) {
        return pageDtoService.getPage(currentPageNumber, itemsOnPage, map);
    }
}
