package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@NoArgsConstructor
public class PageDtoServiceImpl<T> implements PageDtoService<T> {

    public Map<String, PageDtoDao> pageDtoDaoMap;

    @Autowired
    public void setPageDtoDaoMap(Map<String, PageDtoDao> pageDtoDaoMap) {
        this.pageDtoDaoMap = pageDtoDaoMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageDto<T> getPage(int currentPageNumber, int itemsOnPage, Map<Object, Object> map) {
        PageDtoDao<T> pageDtoDao = pageDtoDaoMap.get((String) map.get("class"));

        long totalResultCount = pageDtoDao.getTotalResultCount(map);

        return new PageDto<>(currentPageNumber, (int) Math.ceil((double) totalResultCount/itemsOnPage),
                itemsOnPage, totalResultCount, pageDtoDao.getItems(map));
    }
}
