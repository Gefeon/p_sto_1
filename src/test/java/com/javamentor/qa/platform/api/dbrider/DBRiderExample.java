package com.javamentor.qa.platform.api.dbrider;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.example.ExampleControllerService;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JmApplication.class)
@TestPropertySource("classpath:application-test.properties")
public class DBRiderExample {

    @Autowired
    private ExampleControllerService ecs;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance();

    @Test
    @DataSet(value = "dataset/DBUserExample.yml")
    public void shouldListOfUsers(){
            List<User> users = ecs.getListOfUsers();
        assertThat(users).
                isNotNull().
                isNotEmpty().
                hasSize(2);
    }

    @Test
    @DataSet(value = "dataset/DBRoleExample.yml", disableConstraints = true)
    public void shouldListOfRoles(){
        List<Role> roles = ecs.getListOfRoles();
        assertThat(roles).
                isNotNull().
                isNotEmpty().
                hasSize(2);
    }

//    @Test
//    @DataSet(value="dataset/DBUserExample.yml", disableConstraints=true)
//    public void shouldUpdateUser() {
//        EntityManagerProvider.clear("postgre-it");
//        User user = em().createQuery("select u from User u where u.id = 100", User.class).
//                getSingleResult();
//        assertThat(user).isNotNull();
//        assertThat(user.getEmail()).isEqualTo("admin100@admin.ru");
//        tx().begin();
//        user.setEmail("admin111@admin.ru");
//        em().merge(user);
//        tx().commit();
//        assertThat(user.getEmail()).isEqualTo("admin111@admin.ru");
//    }
//
//    @Test
//    @DataSet(value="dataset/DBUserExample.yml")
//    public void shouldDeleteUser() {
//        User user = em().
//                createQuery("select u from User u where u.id = 100", User.class).
//                getSingleResult();
//        assertThat(user).isNotNull();
//        assertThat(user.getEmail()).isEqualTo("admin111@admin.ru");
//        tx().begin();
//        em().remove(user);
//        tx().commit();
//        List<User> users = em().
//                createQuery("select u from User u", User.class).
//                getResultList();
//        assertThat(users).
//                hasSize(1);
//    }
//
//    @AfterClass
//    public static void close() throws SQLException {
//        DataSetExecutorImpl.getExecutorById(DataSetExecutorImpl.DEFAULT_EXECUTOR_ID).getRiderDataSource().getDBUnitConnection().getConnection().close();
//    }

}
