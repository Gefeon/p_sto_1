package com.javamentor.qa.platform.service.abstracts.dto;

import java.util.List;
import java.util.Map;

public interface PageDtoService<T> {

    List<T> getItems(Map<Object,Object> param);
    long getTotalResultCount(Map<Object,Object> param);
}
