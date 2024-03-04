package com.solbeg.userservice.IT.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.userservice.dto.request.JwtRequest;
import com.solbeg.userservice.dto.request.UserRegisterRequest;
import com.solbeg.userservice.dto.response.JwtResponse;
import com.solbeg.userservice.dto.response.UserRegisterResponse;
import com.solbeg.userservice.service.AuthService;
import com.solbeg.userservice.service.impl.UserServiceImpl;
import com.solbeg.userservice.util.PostgresSqlContainerInitializer;
import com.solbeg.userservice.util.json.UserJsonSupplier;
import com.solbeg.userservice.util.testdata.JwtData;
import com.solbeg.userservice.util.testdata.UserTestData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.solbeg.userservice.util.initdata.InitData.EMAIL_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.FIRST_NAME_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.LAST_NAME_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.PASSWORD_INCORRECT;
import static com.solbeg.userservice.util.initdata.InitData.REFRESH_TOKEN;
import static com.solbeg.userservice.util.initdata.InitData.URL_AUTH;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class AuthControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private AuthService authService;

    @Nested
    class AuthenticatePostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            JwtRequest request = JwtData.builder()
                    .build()
                    .getJwtRequest();
            JwtResponse response = JwtData.builder()
                    .build()
                    .getJwtResponse();

            when(authService.login(request))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(post(URL_AUTH + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(response.getId().toString()),
                            jsonPath("$.email").value(response.getEmail()),
                            jsonPath("$.accessToken").value(response.getAccessToken()),
                            jsonPath("$.refreshToken").value(response.getRefreshToken())
                    );
        }

        @Test
        void shouldReturnThrowExceptionAndStatus400WhenEmailIncorrect() throws Exception {
            // given
            JwtRequest request = JwtData.builder()
                    .withEmail(EMAIL_INCORRECT)
                    .build()
                    .getJwtRequest();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("{email=must be a well-formed email address}"));
        }

//        @Test
//        void shouldReturnThrowExceptionAndStatus400WhenEmailNotExist() throws Exception {
//            // given
//            JwtRequest request = JwtData.builder()
//                    .withEmail(EMAIL_NOT_EXIST)
//                    .build()
//                    .getJwtRequest();
//
//            when(userService.findByUserEmail(request.getEmail()))
//                    .thenThrow(NoSuchUserEmailException.class);
//
//            // when, then
//            mockMvc.perform(post(URL_AUTH + "/authenticate")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.error_message").value("User is not exist with grom@google.com"));
//        }
    }

    @Nested
    class RegisterJournalistPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .build()
                    .getRegisterRequestJournalist();
            UserRegisterResponse response = UserTestData.builder()
                    .build()
                    .getRegisterResponseJournalist();

            when(userService.registerJournalist(request))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(response.getId().toString()),
                            jsonPath("$.firstName").value(response.getFirstName()),
                            jsonPath("$.lastName").value(response.getLastName()),
                            jsonPath("$.password").value(response.getPassword()),
                            jsonPath("$.email").value(response.getEmail()),
                            jsonPath("$.roles").value("JOURNALIST"),
                            jsonPath("$.status").value(response.getStatus().toString())
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidFirstName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withFirstName(FIRST_NAME_INCORRECT)
                    .build()
                    .getRegisterRequestJournalist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("{firstName=size must be between 2 and 40}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidLastName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withLastName(LAST_NAME_INCORRECT)
                    .build()
                    .getRegisterRequestJournalist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("{lastName=size must be between 2 and 50}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidPassword() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withPassword(PASSWORD_INCORRECT)
                    .build()
                    .getRegisterRequestJournalist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("{password=size must be between 3 and 100}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidEmail() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withEmail(EMAIL_INCORRECT)
                    .build()
                    .getRegisterRequestJournalist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("{email=must be a well-formed email address}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidStatus() throws Exception {
            // given
            String request = UserJsonSupplier.getRequestJsonWithNotValidStatusForRegistrationUser();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registerjournalist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("Specify the correct status!"));
        }
    }

    @Nested
    class RegisterSubscriberPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus201() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .build()
                    .getRegisterRequestSubscriber();
            UserRegisterResponse response = UserTestData.builder()
                    .build()
                    .getRegisterResponseSubscriber();

            when(userService.registerSubscriber(request))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpectAll(
                            status().isCreated(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(response.getId().toString()),
                            jsonPath("$.firstName").value(response.getFirstName()),
                            jsonPath("$.lastName").value(response.getLastName()),
                            jsonPath("$.password").value(response.getPassword()),
                            jsonPath("$.email").value(response.getEmail()),
                            jsonPath("$.roles").value("SUBSCRIBER"),
                            jsonPath("$.status").value(response.getStatus().toString())
                    );
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidFirstName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withFirstName(FIRST_NAME_INCORRECT)
                    .build()
                    .getRegisterRequestJournalist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("{firstName=size must be between 2 and 40}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidLastName() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withLastName(LAST_NAME_INCORRECT)
                    .build()
                    .getRegisterRequestJournalist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("{lastName=size must be between 2 and 50}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidPassword() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withPassword(PASSWORD_INCORRECT)
                    .build()
                    .getRegisterRequestJournalist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("{password=size must be between 3 and 100}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidEmail() throws Exception {
            // given
            UserRegisterRequest request = UserTestData.builder()
                    .withEmail(EMAIL_INCORRECT)
                    .build()
                    .getRegisterRequestJournalist();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("{email=must be a well-formed email address}"));
        }

        @Test
        void shouldReturnReturnThrowExceptionAndStatus400WithNotValidStatus() throws Exception {
            // given
            String request = UserJsonSupplier.getRequestJsonWithNotValidStatusForRegistrationUser();

            // when, then
            mockMvc.perform(post(URL_AUTH + "/registersubscriber")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error_message").value("Specify the correct status!"));
        }
    }

    @Nested
    class RefreshTokenPostEndpointTest {

        @Test
        void shouldReturnExpectedJsonAndStatus200() throws Exception {
            // given
            JwtResponse response = JwtData.builder()
                    .build()
                    .getJwtResponse();

            when(authService.refresh(REFRESH_TOKEN))
                    .thenReturn(response);

            // when, then
            mockMvc.perform(post(URL_AUTH + "/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(REFRESH_TOKEN)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.id").value(response.getId().toString()),
                            jsonPath("$.email").value(response.getEmail()),
                            jsonPath("$.accessToken").value(response.getAccessToken()),
                            jsonPath("$.refreshToken").value(response.getRefreshToken())
                    );
        }

//        @Test
//        void shouldReturnReturnThrowExceptionAndStatus401WithNotValidRefreshToken() throws Exception {
//            // given
//            RefreshTokenRequest request = JwtData.builder()
//                    .withRefreshToken(INCORRECT_TOKEN)
//                    .build()
//                    .getRefreshTokenRequest();
//
//            // when, then
//            mockMvc.perform(post(URL_AUTH + "/refresh")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isUnauthorized());
//        }
//
//        @Test
//        void shouldReturnReturnThrowExceptionAndStatus401WithIncorrectJWTSignature() throws Exception {
//            // given
//            RefreshTokenRequest request = JwtData.builder()
//                    .withRefreshToken(TOKEN_INCORRECT_SIGNATURE)
//                    .build()
//                    .getRefreshTokenRequest();
//
//            // when, then
//            mockMvc.perform(post(URL_AUTH + "/refresh")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isUnauthorized());
//        }
    }
}