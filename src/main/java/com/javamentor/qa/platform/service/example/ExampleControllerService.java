package com.javamentor.qa.platform.service.example;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class ExampleControllerService {

    @PersistenceContext
    private EntityManager em;

    public List<Role> getListOfRoles() {
        return em.createQuery("select r from Role r", Role.class).getResultList();
    }

    public List<User> getListOfUsers() {
        return em.createQuery("select u from User u join fetch u.role r", User.class).getResultList();
    }
}
