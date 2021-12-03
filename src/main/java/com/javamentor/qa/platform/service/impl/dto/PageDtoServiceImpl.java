package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.service.abstracts.dto.PageDtoService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
@Data
public abstract class PageDtoServiceImpl<T> {

    //Dto class
    @SuppressWarnings("unchecked")
    private final Class<T> dtoClazz = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    private List<PageDtoDao> pageDtoDaoImplements;

    @Autowired
    public PageDtoServiceImpl(List<PageDtoDao> pageDtoDaoImplements) {
        this.pageDtoDaoImplements = pageDtoDaoImplements;
    }

    PageDtoDao baseClass = pageDtoDaoImplements.stream().filter(f -> f.getClass().getName().contains(dtoClazz.getName()))
            .filter(f -> !f.getClass().getName().contains("Controller")).findFirst().get();

    public List<T> getItems(Object param) {
        baseClass.getItems(param);
        return (List<T>) baseClass.getItems(param);
    }

    public long getTotalResultCount(Map param) {
        return baseClass.getTotalResultCount(param);
    }

    abstract List<Object> getCommonItems(Map<String, Object> param);

}
