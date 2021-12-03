package com.javamentor.qa.platform.dao.abstracts.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

public interface PageDtoDao {
//    @GetRuntime
    List<Object> getItems(Object param);
//    @GetRuntime
    long getTotalResultCount(Map<Object,Object> param);
//    @GetRuntime
//    default List<Object> getCommonItems(Map<String, Object> param){return null;}; // удалить т.к. метод буд абстрактным в классе
}

//@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.METHOD)
//@interface GetRuntime {
//}
