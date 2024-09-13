package com.zubrilka.VideoManager.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TestController.class)
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetails userDetails; // Мок для UserDetails

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testSayHello() throws Exception {


        // Выполняем запрос и проверяем результат
        mockMvc.perform(get("/test/hello")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{\"greeting\":\"Hello1, %s!\",".formatted(userDetails) +
                        " \"greeting2\":\"Hello2, %s!\",".formatted(userDetails) +
                                " \"greeting3\":\"Hello3, %s!\",".formatted(userDetails) +
                                " \"greeting4\":\"Hello4, %s!\"}".formatted(userDetails.getUsername())));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void privacyUser_ReturnsCorrectString() throws Exception {
        mockMvc.perform(get("/test/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("privacyUser"));
    }

    @Test
    void privacyAdmin() {}
}