package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;

import java.util.List;
import java.util.Map;

public abstract class PageDtoDaoImpl<T> implements PageDtoDao<T> {

    public abstract List<T> getItems(Map<Object, Object> param);

    public abstract long getTotalResultCount(Map<Object, Object> param);
}
