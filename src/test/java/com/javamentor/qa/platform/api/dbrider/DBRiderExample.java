package com.javamentor.qa.platform.api.dbrider;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.dataset.DataSetExecutorImpl;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.SQLException;
import java.util.List;

import static com.github.database.rider.junit5.util.EntityManagerProvider.em;
import static com.github.database.rider.junit5.util.EntityManagerProvider.tx;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class DBRiderExample {

    private final EntityManagerProvider emProvider = EntityManagerProvider.instance("postgre-it");

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(emProvider.connection());


    @Test
    @DataSet(value = "dataset/DBUserExample.yml")
    public void shouldListOfUsers(){
        List<User> users = em().
                createQuery("select u from User u", User.class).
                getResultList();
        assertThat(users).
                isNotNull().
                isNotEmpty().
                hasSize(2);
    }

    @Test
    @DataSet(value = "dataset/DBRoleExample.yml")
    public void shouldListOfRoles(){
        List<Role> roles = em().
                createQuery("select r from Role r", Role.class).
                getResultList();
        assertThat(roles).
                isNotNull().
                isNotEmpty().
                hasSize(2);
    }

    @Test
    @DataSet(value="dataset/DBUserExample.yml", disableConstraints=true)
    public void shouldUpdateUser() {
        EntityManagerProvider.clear("postgre-it");
        User user = em().createQuery("select u from User u where u.id = 100", User.class).
                getSingleResult();
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("admin100@admin.ru");
        tx().begin();
        user.setEmail("admin111@admin.ru");
        em().merge(user);
        tx().commit();
        assertThat(user.getEmail()).isEqualTo("admin111@admin.ru");
    }

    @Test
    @DataSet(value="dataset/DBUserExample.yml")
    public void shouldDeleteUser() {
        User user = em().
                createQuery("select u from User u where u.id = 100", User.class).
                getSingleResult();
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("admin111@admin.ru");
        tx().begin();
        em().remove(user);
        tx().commit();
        List<User> users = em().
                createQuery("select u from User u", User.class).
                getResultList();
        assertThat(users).
                hasSize(1);
    }

    @AfterClass
    public static void close() throws SQLException {
        DataSetExecutorImpl.getExecutorById(DataSetExecutorImpl.DEFAULT_EXECUTOR_ID).getRiderDataSource().getDBUnitConnection().getConnection().close();
    }

}
