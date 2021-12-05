package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;

import java.util.List;
import java.util.Map;

public abstract class PageDtoDaoImpl<T> implements PageDtoDao<T> {

    public List<T> getItems(Map<Object, Object> param) {
        return List.of((T) null);
    }

    public long getTotalResultCount(Map<Object, Object> param) {
        return 0;
    }
}
