package com.javamentor.qa.platform.dao.impl.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.hibernate.transform.ResultTransformer;
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

    @SuppressWarnings("unchecked")
    @Override
    public List<UserDto> getItems(Map<Object, Object> param) {
        int curPageNumber = (int) param.get("currentPageNumber");
        int itemsOnPage = (int) param.get("itemsOnPage");

        return entityManager.createQuery("SELECT " +
                        "u.id, u.email, u.fullName, u.imageLink, u.city, SUM(COALESCE(r.count, 0))," +
                        "((SELECT COUNT(vq.vote) FROM VoteQuestion vq WHERE vq.user.id = u.id) +" +
                        "(SELECT COUNT(va.vote) FROM VoteAnswer va WHERE va.user.id = u.id)) AS sort " +
                        " FROM User u LEFT JOIN Reputation r ON u.id = r.author.id " +
                        " GROUP BY u.id" +
                        " ORDER BY sort DESC", Tuple.class).unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] tuples, String[] aliases) {
                        UserDto userDTO = new UserDto();
                        userDTO.setId((long) tuples[0]);
                        userDTO.setEmail((String) tuples[1]);
                        userDTO.setFullName((String) tuples[2]);
                        userDTO.setLinkImage((String) tuples[3]);
                        userDTO.setCity((String) tuples[4]);
                        userDTO.setReputationLong((Long) tuples[5]);
                        return userDTO;
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                }).setFirstResult((curPageNumber - 1) * itemsOnPage).setMaxResults(itemsOnPage)
                .getResultList();


    }

    @Override
    public long getTotalResultCount(Map<Object, Object> param) {
        return (Long) entityManager.createQuery("SELECT COUNT(id) FROM User").getSingleResult();
    }
}
