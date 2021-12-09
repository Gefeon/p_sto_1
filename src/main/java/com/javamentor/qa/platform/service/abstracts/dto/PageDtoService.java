package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.PageDto;

import java.util.Map;

public interface PageDtoService<T, E> {
    PageDto<T> getPage(int currentPageNumber, int itemsOnPage,
                       Map<E, Object> map);
}
