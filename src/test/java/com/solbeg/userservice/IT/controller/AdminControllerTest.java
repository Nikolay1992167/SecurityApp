package com.solbeg.userservice.IT.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.solbeg.userservice.dto.request.UserUpdateRequest;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.service.impl.UserServiceImpl;
import com.solbeg.userservice.util.PostgresSqlContainerInitializer;
import com.solbeg.userservice.util.testdata.UserTestData;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.solbeg.userservice.util.initdata.InitData.ID_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.ID_JOURNALIST_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.ID_NOT_EXIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_SUBSCRIBER_FOR_IT;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_USERTOKEN;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_USERTOKEN_NOT_EXIST;
import static com.solbeg.userservice.util.initdata.InitData.URL_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class AdminControllerTest extends PostgresSqlContainerInitializer {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @SpyBean
    private UserServiceImpl userService;

    @Nested
    class FindAllUserTokensGetEndpointTest {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get(URL_ADMIN + "/tokens"))
                    .andExpect(status().isOk())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(1);
            assertThat(jsonObject.get("number")).isEqualTo(0);
        }
    }

    @Nested
    class FindAllGetEndpointTest {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get(URL_ADMIN))
                    .andExpect(status().isOk())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            JSONObject jsonObject = new JSONObject(response.getContentAsString());
            assertThat(jsonObject.get("totalPages")).isEqualTo(1);
            assertThat(jsonObject.get("totalElements")).isEqualTo(4);
            assertThat(jsonObject.get("number")).isEqualTo(0);
            assertThat(jsonObject.get("content")).isNotNull();
        }
    }

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            UUID userId = ID_JOURNALIST_FOR_IT;

            // when, then
            mockMvc.perform(get(URL_ADMIN + "/" + userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userId.toString()));
        }

        @Test
        void shouldReturnThrowExceptionAndStatus401() throws Exception {
            mockMvc.perform(get(URL_ADMIN + "/" + ID_JOURNALIST_FOR_IT))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldReturnThrowExceptionAndStatus404() throws Exception {
            mockMvc.perform(get(URL_ADMIN + "/" + ID_NOT_EXIST))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.USER_NOT_FOUND.getMessage() + ID_NOT_EXIST));
        }

        @Test
        @WithMockUser(authorities = "USER")
        void shouldReturnThrowExceptionAndStatus403() throws Exception {
            mockMvc.perform(get(URL_ADMIN + "/" + ID_JOURNALIST_FOR_IT))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class UpdatePutEndpointTest {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            UUID userId = ID_JOURNALIST_FOR_IT;
            UserUpdateRequest updateRequest = UserTestData.builder()
                    .build()
                    .getUserUpdateRequest();
            String json = objectMapper.writeValueAsString(updateRequest);

            // when, then
            mockMvc.perform(put(URL_ADMIN + "/" + userId)
                    .content(json)
                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpectAll(jsonPath("$.id").value(userId.toString()),
                            jsonPath("$.firstName").value(updateRequest.getFirstName()),
                            jsonPath("$.lastName").value(updateRequest.getLastName()),
                            jsonPath("$.email").value(updateRequest.getEmail()));
        }
    }

    @Nested
    @WireMockTest(httpPort = 8088)
    class ActivateUserJournalistPathEndpointTest {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldActivateUserJournalist() throws Exception {
            //String adminToken = tokenProvider.createRefreshToken(ID_ADMIN, EMAIL_ADMIN);
            stubFor(WireMock.post(WireMock.urlEqualTo("/api/v1/send/email"))
                    .willReturn(WireMock.aResponse()
                            .withStatus(200)));

            mockMvc.perform(patch(URL_ADMIN + "/activation")
                    .param("userToken", TOKEN_USERTOKEN)
                    .header(AUTHORIZATION, TOKEN_ADMIN)
                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldThrowExceptionWhenUserTokenNotFound() throws Exception {
            mockMvc.perform(patch(URL_ADMIN + "/activation")
                    .param("userToken", TOKEN_USERTOKEN_NOT_EXIST))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.USERTOKEN_NOT_FOUND.getMessage() + TOKEN_USERTOKEN_NOT_EXIST));
        }
    }

    @Nested
    class DeactivateUserPathEndpointTest {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldDeactivateUser() throws Exception {
            mockMvc.perform(patch(URL_ADMIN + "/deactivate/{id}", ID_JOURNALIST_FOR_IT)
                    .header(AUTHORIZATION, TOKEN_ADMIN)
                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldThrowExceptionWhenDeactivateAdminUser() throws Exception {
            mockMvc.perform(patch(URL_ADMIN + "/deactivate/" + ID_ADMIN))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.CHANGE_STATUS.getMessage()));
        }
    }

    @Nested
    class DeleteUserPathEndpointTest {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldDeleteUser() throws Exception {
            mockMvc.perform(delete(URL_ADMIN + "/delete/" + ID_SUBSCRIBER_FOR_IT)
                    .header(AUTHORIZATION, TOKEN_ADMIN)
                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        void shouldThrowExceptionWhenDeactivateAdminUser() throws Exception {
            mockMvc.perform(delete(URL_ADMIN + "/delete/" + ID_ADMIN))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.CHANGE_STATUS.getMessage()));
        }
    }
}