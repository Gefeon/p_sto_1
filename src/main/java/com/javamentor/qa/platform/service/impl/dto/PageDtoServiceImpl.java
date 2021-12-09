package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@NoArgsConstructor
public class PageDtoServiceImpl<T, E> implements PageDtoService<T, E> {

    private PageDtoDao<T, E> pageDtoDao;

    public PageDtoServiceImpl(PageDtoDao<T, E> pageDtoDao) {
        this.pageDtoDao = pageDtoDao;
    }

    @Override
    public PageDto<T> getPage(int currentPageNumber, int itemsOnPage, Map<E, Object> map) {

        long totalResultCount = pageDtoDao.getTotalResultCount(map);

        return new PageDto<>(currentPageNumber, (int) Math.ceil((double) totalResultCount/itemsOnPage),
                itemsOnPage, totalResultCount, pageDtoDao.getItems(map));
    }
}
