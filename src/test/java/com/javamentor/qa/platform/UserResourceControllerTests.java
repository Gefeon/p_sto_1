package com.javamentor.qa.platform;


import com.javamentor.qa.platform.webapp.configs.JmApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = JmApplication.class)
public class UserResourceControllerTests {

    private final Long id = 33L;
    private final String url = "/api/user/" + id;
    private final String name = "Roman";
    private final String email = "Rom@ya.ru";
    private final String linkImage = null;
    private final String city = "Surgut";
    private final int reputation = 41;

    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testGetUserById() throws Exception {

        ResultActions res = mockMvc.perform(get(url));

        if (res.andReturn().getResponse().getHeaderValue("Body") == null) {
            res.andExpect(status().isNotFound());
        } else {
            res
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(city))
                .andExpect(MockMvcResultMatchers.jsonPath("$.linkImage").value(linkImage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reputation").value(reputation));
        }
    }
}
