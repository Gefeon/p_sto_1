package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.*;
import com.javamentor.qa.platform.service.abstracts.dto.UserReputationDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserReputationDtoServiceImpl extends PageDtoServiceImpl<UserReputationDto> implements UserReputationDtoService {

    private final TagDtoDao tagDtoDao;

    public UserReputationDtoServiceImpl(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    @Transactional
    public PageDto<UserReputationDto> getPage(int currentPageNumber, int itemsOnPage, Map<Object, Object> map) {

        PageDto<UserReputationDto> pageDto = super.getPage(currentPageNumber, itemsOnPage, map);
        List<UserReputationDto> reputationDtos = pageDto.getItems();

        List<Long> userIds = reputationDtos.stream()
                .map(UserReputationDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<TagDto>> tagsMap = tagDtoDao.getMapTagsByUserIds(userIds);
        for (UserReputationDto reputationDto : reputationDtos) {
            reputationDto.setTags(tagsMap.get(reputationDto.getId()));
        }

        pageDto.setItems(reputationDtos);
        return pageDto;
    }

}
