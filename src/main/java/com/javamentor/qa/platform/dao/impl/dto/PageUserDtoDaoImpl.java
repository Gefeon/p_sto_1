package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@NoArgsConstructor
@Data
public class PageUserDtoDaoImpl implements PageDtoDao {

    @Override
    public List<Object> getItems(Object param) {
        UserDto userDto = new UserDto((Long) param, "e", "n", "l", "Moscow", 41L);
        return List.of(userDto);
    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return 0;
    }
}
