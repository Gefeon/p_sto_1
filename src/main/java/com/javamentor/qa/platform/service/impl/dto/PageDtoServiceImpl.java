package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public abstract class PageDtoServiceImpl<T> implements PageDtoService<T> {

    private PageDtoDao<T> pageDtoDao;

    public PageDtoServiceImpl(PageDtoDao<T> pageDtoDao) {
        this.pageDtoDao = pageDtoDao;
    }

    @Transactional
    public List<T> getItems(Map<Object, Object> param) {
        return pageDtoDao.getItems(param);
    }

    @Transactional
    public long getTotalResultCount(Map<Object, Object> param) {
        return pageDtoDao.getTotalResultCount(param);
    }

    @SuppressWarnings("unchecked")
    public PageDto<T> getPage(int currentPageNumber, int totalPageCount, int itemsOnPage, Map<Object, Object> resultCountParam, Map<Object, Object> itemsParam) {
        return new PageDto<>(currentPageNumber, totalPageCount, itemsOnPage, getTotalResultCount(resultCountParam), getItems(itemsParam));
    }
}
