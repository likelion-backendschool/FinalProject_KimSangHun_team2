package com.ebook.multbooks.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test","secret"})
@Transactional
public class ApiControllerTest {
    @Autowired
    private MockMvc mvc;
    @Test
    @DisplayName("Post /api/v1/member/login 은 jwt 로그인 ")
    void t1() throws Exception {
        ResultActions resultActions=mvc.perform(
                post("/api/v1/member/login")
                        .with(csrf())
                        .content("""
                        {
                            "username": "user1",
                            "password": "1234"
                        }
                        """.stripIndent())
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
        )
                .andDo(print());
        resultActions.andExpect(status().is2xxSuccessful());
    }
}
