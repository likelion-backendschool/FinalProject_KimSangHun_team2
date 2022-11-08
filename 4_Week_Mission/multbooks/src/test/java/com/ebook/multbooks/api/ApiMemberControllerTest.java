package com.ebook.multbooks.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test","secret"})
@Transactional
public class ApiMemberControllerTest {
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
    @Test
    @DisplayName("Post /api/v1/member/login 유효성 체크 ")
    void t2() throws Exception {
        ResultActions resultAction1=mvc.perform(
                        post("/api/v1/member/login")
                                .with(csrf())
                                .content("""
                        {
                            "username": "",
                            "password": "1234"
                        }
                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());
        resultAction1.andExpect(status().is4xxClientError());

        ResultActions resultAction2=mvc.perform(
                        post("/api/v1/member/login")
                                .with(csrf())
                                .content("""
                        {
                            "username": "",
                            "password": ""
                        }
                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());
        resultAction2.andExpect(status().is4xxClientError());
    }
    @Test
    @DisplayName("로그인 후 얻은 JWT 토큰으로 현재 로그인 한 회원의 정보를 얻을 수 있다.")
    void t3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/member/login")
                                .content("""
                                        {
                                            "username": "user1",
                                            "password": "1234"
                                        }
                                        """.stripIndent())
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());

        MvcResult mvcResult = resultActions.andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        String accessToken = response.getHeader("Authentication");

        resultActions = mvc
                .perform(
                        get("/api/v1/member/me")
                                .header("Authorization", "Bearer " + accessToken)
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                 . andExpect(jsonPath("$.msg").value("성공"))
                . andExpect(jsonPath("$.data.id").isNotEmpty())
                . andExpect(jsonPath("$.data.createDate").isNotEmpty())
                 . andExpect(jsonPath("$.data.updateDate").isNotEmpty())
                 . andExpect(jsonPath("$.data.username").isNotEmpty())
                . andExpect(jsonPath("$.data.email").isNotEmpty());
        // MemberController me 메서드에서는 @AuthenticationPrincipal MemberContext memberContext 를 사용해서 현재 로그인 한 회원의 정보를 얻어야 한다.

        // 추가
        // /member/me 에 응답 본문
        /*
          {
            "resultCode": "S-1",
            "msg": "성공",
            "data": {
              "id": 1,
              "createData": "날짜",
              "modifyData": "날짜",
              "username": "user1",
              "email": "user1@test.com"
            }
            "success": true,
            "fail": false
          }
       */
    }
}
