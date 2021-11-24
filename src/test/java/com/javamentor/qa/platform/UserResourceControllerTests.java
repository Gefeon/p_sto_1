package com.javamentor.qa.platform;


import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest(classes = JmApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceControllerTests {

    private final Long id = 3L;
    private final String url = "/api/user/" + id;
    private final String name = "Roman";
    private final String email = "Rom@ya.ru";
    private final String linkImage = "";
    private final String city = "Surgut";
    private final int reputation = 41;
    private TestRestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    public void testGetUserById() throws Exception {

        ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), is(id));
        assertThat(response.getBody().getFullName(), is(name));
        assertThat(response.getBody().getEmail(), is(email));
        assertThat(response.getBody().getLinkImage(), is(nullValue()));
        assertThat(response.getBody().getCity(), is(city));
        assertThat(response.getBody().getReputation(), is(reputation));

    }
}
