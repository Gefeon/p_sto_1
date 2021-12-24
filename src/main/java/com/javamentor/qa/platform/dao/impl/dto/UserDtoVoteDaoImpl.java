package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

@Repository(value = "UserPaginationByVote")
public class UserDtoVoteDaoImpl implements PageDtoDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");


        List<Tuple> resultList = entityManager.createQuery("SELECT " +
                        "u.id, u.email, u.fullName, u.imageLink, u.city, SUM(r.count )," +
                        "((Select count(vq.vote) from VoteQuestion vq WHERE vq.user.id = u.id) +" +
                        "(Select count(va.vote) from VoteAnswer va where va.user.id = u.id)) as sort " +
                        " FROM User u LEFT JOIN Reputation r ON u.id = r.author.id " +
                        " GROUP BY u.id" +
                        " ORDER BY sort desc", Tuple.class)
                .setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();
        System.out.println(resultList);

        return null;


    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return 0;
    }
}
