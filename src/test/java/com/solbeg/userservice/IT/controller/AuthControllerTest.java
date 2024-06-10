package com.solbeg.userservice.IT.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.userservice.dto.request.EmailRequest;
import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.request.RefreshTokenRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.security.jwt.JwtTokenProvider;
import com.solbeg.userservice.service.impl.SendDataServiceImpl;
import com.solbeg.userservice.util.PostgresSqlContainerInitializer;
import com.solbeg.userservice.util.testdata.JwtTestData;
import com.solbeg.userservice.util.testdata.UserTestData;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static com.solbeg.userservice.util.Constants.EMAIL_ERROR;
import static com.solbeg.userservice.util.Constants.SIZE_NAME_ERROR;
import static com.solbeg.userservice.util.Constants.SIZE_PASSWORD_ERROR;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.EMAIL_NOT_EXIST;
import static com.solbeg.userservice.util.initdata.InitData.ID_ADMIN;
import static com.solbeg.userservice.util.initdata.InitData.INCORRECT_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.URL_AUTH;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class AuthControllerTest extends PostgresSqlContainerInitializer {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final SendDataServiceImpl sendDataService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Nested
    class AuthenticatePostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            JwtRequest request = JwtTestData.getJwtRequestForIT();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.accessToken").isString(),
                            jsonPath("$.refreshToken").isString());
        }

        @Test
        void shouldReturnThrowExceptionAndStatus400WhenEmailIncorrect() throws Exception {
            // given
            JwtRequest request = JwtTestData.getJwtRequestWithIncorrectEmail();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message",
                            containsString(EMAIL_ERROR)));
        }

        @Test
        void shouldReturnThrowExceptionAndStatus400WhenEmailNotExist() throws Exception {
            // given
            JwtRequest request = JwtTestData.getJwtRequestWithEmailNotExist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message")
                            .value(ErrorMessage.USER_NOT_EXIST.getMessage() + EMAIL_NOT_EXIST));
        }

        @Test
        void shouldReturnThrowExceptionAndStatus400WhenEnteredDataIncorrect() throws Exception {
            // given
            JwtRequest request = JwtTestData.getJwtRequestWithIncorrectEnteredDate();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message",
                            containsString(EMAIL_ERROR)))
                    .andExpect(jsonPath("$.error_message",
                            containsString(SIZE_PASSWORD_ERROR)));
        }
    }

    @Nested
    class RegisterJournalistPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestJournalist();
            doNothing().when(rabbitTemplate)
                    .convertAndSend(anyString(), anyString(), any(EmailRequest.class));
            ReflectionTestUtils.setField(sendDataService, "rabbitTemplate", rabbitTemplate);

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/journalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated()
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidFirstName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestJournalistWithIncorrectFirstName();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/journalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message",
                            containsString(SIZE_NAME_ERROR)));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidLastName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestJournalistWithIncorrectLastName();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/journalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message",
                            containsString(SIZE_NAME_ERROR)));
        }
    }

    @Nested
    class RegisterSubscriberPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestSubscriber();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/subscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated()
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidFirstName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestSubscriberWithIncorrectFirstName();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/subscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message",
                            containsString(SIZE_NAME_ERROR)));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidLastName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.getRegisterRequestSubscriberWithIncorrectLastName();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/register/subscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message", containsString(SIZE_NAME_ERROR)));
        }
    }

    @Nested
    class RefreshTokenPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            String token = jwtTokenProvider.createRefreshToken(ID_ADMIN, EMAIL_ADMIN);
            RefreshTokenRequest request = new RefreshTokenRequest(token);

            // when, then
            mockMvc.perform(post(URL_AUTH + "/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isOk()
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidRefreshToken() throws Exception {
            // given
            RefreshTokenRequest request = new RefreshTokenRequest(INCORRECT_TOKEN);

            // when, then
            mockMvc.perform(post(URL_AUTH + "/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}