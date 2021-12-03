package com.javamentor.qa.platform.service.abstracts.dto;

import java.util.List;
import java.util.Map;

public interface PageDtoService<E, P> {

    List<E> getItems(Map<P,E> param);
    long getTotalResultCount(Map<P,E> param);
}
