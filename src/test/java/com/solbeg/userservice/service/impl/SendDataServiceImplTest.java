package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.EmailRequest;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.entity.User_;
import com.solbeg.userservice.enums.EmailType;
import com.solbeg.userservice.enums.error_response.ErrorMessage;
import com.solbeg.userservice.exception.SendDataException;
import com.solbeg.userservice.util.testdata.EmailRequestTestData;
import com.solbeg.userservice.util.testdata.UserTestData;
import com.solbeg.userservice.util.testdata.UserTokenTestData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.solbeg.userservice.util.initdata.InitData.BASE_URL;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_USERTOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SendDataServiceImplTest {

    @InjectMocks
    private SendDataServiceImpl sendingDataService;

    private MockWebServer mockWebServer;

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        WebClient webClient = WebClient.create(mockWebServer.url("/").toString());
        sendingDataService = new SendDataServiceImpl(webClient);
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    class SendRequestToMailService {

        @Test
        void shouldCheckSendEmailRequestToMailServiceAndReturnStatus200() {
            // given
            EmailRequest emailRequest = EmailRequestTestData.builder()
                    .build()
                    .getEmailRequest();
            mockWebServer.enqueue(new MockResponse().setResponseCode(200));

            // when
            sendingDataService.sendRequestToMailService(emailRequest);

            // then
            assertThat(mockWebServer.getRequestCount()).isEqualTo(1);
        }

        @Test
        void shouldThrowSendDataExceptionWhenNotSendEmailRequest() {
            // given
            EmailRequest emailRequest = EmailRequestTestData.builder()
                    .build()
                    .getEmailRequest();
            mockWebServer.enqueue(new MockResponse().setResponseCode(409));

            // when, then
            assertThatThrownBy(() -> sendingDataService.sendRequestToMailService(emailRequest))
                    .isExactlyInstanceOf(SendDataException.class)
                    .hasMessageContaining(ErrorMessage.ERROR_SEND_DATA.getMessage());
        }
    }


    @Test
    void shouldReturnExpectedActivationData() {
        // given
        User user = UserTestData.builder()
                .build()
                .getJournalist();
        String tokenUser = TOKEN_USERTOKEN;

        // when
        Map<String, String> actual = sendingDataService.getActivationData(user, tokenUser);

        // then
        assertThat(actual.get(User_.FIRST_NAME)).isEqualTo(user.getFirstName());
        assertThat(actual.get(User_.LAST_NAME)).isEqualTo(user.getLastName());
        assertThat(actual.get("activationLink")).isEqualTo("http://localhost:8081/api/v1/users/activation?userToken=" + tokenUser);
    }

    @Test
    void shouldReturnExpectedWelcomeMessageData() {
        // given
        User user = UserTestData.builder()
                .build()
                .getJournalist();

        // when
        Map<String, String> actual = sendingDataService.getWelcomeMessageData(user);

        // then
        assertThat(actual.get(User_.FIRST_NAME)).isEqualTo(user.getFirstName());
        assertThat(actual.get(User_.LAST_NAME)).isEqualTo(user.getLastName());
    }

    @Test
    void shouldReturnExpectedEmailRequestForActivationMessage() {
        // given
        User user = UserTestData.builder()
                .build()
                .getJournalist();
        UserToken userToken = UserTokenTestData.builder()
                .build()
                .getUserToken();
        EmailRequest expected = EmailRequestTestData.builder()
                .withEmailType(EmailType.USER_ACTIVATE)
                .withData(new HashMap<>() {
                    {
                        put(User_.FIRST_NAME, user.getFirstName());
                        put(User_.LAST_NAME, user.getLastName());
                        put("activationLink", BASE_URL + userToken.getToken());
                    }
                })
                .build()
                .getEmailRequest();

        // when
        EmailRequest actual = sendingDataService.getEmailRequest(user, userToken);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldReturnExpectedEmailRequestForWelcomeMessage() {
        // given
        User user = UserTestData.builder()
                .build()
                .getJournalist();
        EmailRequest expected = EmailRequestTestData.builder()
                .withEmailType(EmailType.USER_WELCOME_EMAIL)
                .withData(new HashMap<>() {
                    {
                        put(User_.FIRST_NAME, user.getFirstName());
                        put(User_.LAST_NAME, user.getLastName());
                    }
                })
                .build()
                .getEmailRequest();

        // when
        EmailRequest actual = sendingDataService.getEmailRequest(user, EmailType.USER_WELCOME_EMAIL);

        // then
        assertThat(actual).isEqualTo(expected);

    }
}