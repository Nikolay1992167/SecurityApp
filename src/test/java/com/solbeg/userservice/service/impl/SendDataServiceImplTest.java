package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.enums.EmailType;
import com.solbeg.userservice.exception.SendDataException;
import com.solbeg.userservice.service.UserTokenService;
import com.solbeg.userservice.util.testdata.UserTestData;
import com.solbeg.userservice.util.testdata.UserTokenTestData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
class SendDataServiceImplTest {

    @InjectMocks
    private SendDataServiceImpl sendingDataService;

    @Mock
    private UserTokenService userTokenService;

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        RestClient restClient = RestClient.create(mockWebServer.url("/").toString());
        sendingDataService = new SendDataServiceImpl(restClient, userTokenService);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    class SendRequestForActivationUser {

        @Test
        void shouldCheckSendMessageToMailServiceAndReturnStatus200() {
            // given
            User user = UserTestData.getJournalist();
            UserToken userToken = UserTokenTestData.getUserToken();
            mockWebServer.enqueue(new MockResponse().setResponseCode(200));
            when(userTokenService.createActivationToken(user.getId()))
                    .thenReturn(userToken);
            // when
            sendingDataService.sendRequestForActivationUser(user);

            // then
            assertThat(mockWebServer.getRequestCount()).isEqualTo(1);
        }

        @Test
        void shouldThrowSendDataExceptionWhenNotSendEmailRequest() {
            // given
            User user = UserTestData.getJournalist();
            UserToken userToken = UserTokenTestData.getUserToken();
            mockWebServer.enqueue(new MockResponse().setResponseCode(400));
            when(userTokenService.createActivationToken(user.getId()))
                    .thenReturn(userToken);

            // when, then
            assertThatThrownBy(() -> sendingDataService.sendRequestForActivationUser(user))
                    .isExactlyInstanceOf(SendDataException.class);
        }
    }

    @Nested
    class SendInformationWithWelcomeEmail {

        @Test
        void shouldCheckSendMessageInformationToMailServiceAndReturnStatus200() {
            // given
            User user = UserTestData.getJournalist();
            mockWebServer.enqueue(new MockResponse().setResponseCode(200));

            // when
            sendingDataService.sendInformation(user, EmailType.USER_WELCOME_EMAIL);

            // then
            assertThat(mockWebServer.getRequestCount()).isEqualTo(1);
        }

        @Test
        void shouldThrowSendDataExceptionWhenNotSendEmailRequest() {
            // given
            User user = UserTestData.getJournalist();
            mockWebServer.enqueue(new MockResponse().setResponseCode(400));

            // when, then
            assertThatThrownBy(() -> sendingDataService.sendInformation(user, EmailType.USER_WELCOME_EMAIL))
                    .isExactlyInstanceOf(SendDataException.class);
        }
    }

    @Nested
    class SendInformationWithMessageAboutExpiredToken {

        @Test
        void shouldCheckSendMessageInformationToMailServiceAndReturnStatus200() {
            // given
            User user = UserTestData.getJournalist();
            mockWebServer.enqueue(new MockResponse().setResponseCode(200));

            // when
            sendingDataService.sendInformation(user, EmailType.USER_TOKEN_EXPIRATION);

            // then
            assertThat(mockWebServer.getRequestCount()).isEqualTo(1);
        }
    }
}