package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class PageDtoServiceImpl<T> implements PageDtoService<T> {

    private int currentPageNumber;
    private int totalPageCount;
    private int itemsOnPage;
    private Map<Object, Object> itemsParam;
    private Map<Object, Object> resultCountParam;
    public T objectDto;

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
    public PageDto<T> getPageDto() {
        return new PageDto<>(currentPageNumber, totalPageCount, itemsOnPage, getTotalResultCount(resultCountParam), getItems(itemsParam));
    }
}
