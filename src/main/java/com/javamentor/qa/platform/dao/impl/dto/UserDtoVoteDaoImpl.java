package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.hibernate.EntityMode;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository(value = "UserPaginationByVote")
public class UserDtoVoteDaoImpl implements PageDtoDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;
    private EntityMode TupleClassObject;

    @Override
    public List<UserDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");


        List<UserDto> resultList = entityManager.createQuery("SELECT " +
                        "u.id, u.email, u.fullName, u.imageLink, u.city, SUM(r.count)," +
                        "((Select count(vq.vote) from VoteQuestion vq WHERE vq.user.id = u.id) +" +
                        "(Select count(va.vote) from VoteAnswer va where va.user.id = u.id)) as sort " +
                        " FROM User u LEFT JOIN Reputation r ON u.id = r.author.id " +
                        " GROUP BY u.id" +
                        " ORDER BY sort desc", Tuple.class).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] tuples, String[] aliases) {
                        UserDto userDTO = new UserDto();
                        userDTO.setId((long) tuples[0]);
                        userDTO.setEmail((String) tuples[1]);
                        userDTO.setFullName((String) tuples[2]);
                        userDTO.setLinkImage((String) tuples[3]);
                        userDTO.setCity((String) tuples[4]);
                //        userDTO.setReputation((int)tuples[5]);
                        return userDTO;
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                }).setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();

        return resultList;


    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return 0;
    }
}
