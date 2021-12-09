package com.javamentor.qa.platform.dao.abstracts.dto;

import java.util.List;
import java.util.Map;

public interface PageDtoDao<T, E> {
    List<T> getItems(Map<E, Object> param);
    long getTotalResultCount(Map<E, Object> param);
}
