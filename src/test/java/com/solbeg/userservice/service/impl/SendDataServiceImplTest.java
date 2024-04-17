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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.solbeg.userservice.util.initdata.InitData.BASE_URL;
import static com.solbeg.userservice.util.initdata.InitData.TOKEN_USERTOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SendDataServiceImplTest {

    @InjectMocks
    private SendDataServiceImpl sendingDataService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersMock;

    @Mock
    private WebClient.RequestBodySpec requestBodyMock;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriMock;

    @Mock
    private WebClient.ResponseSpec responseMock;

    @Nested
    class SendRequestToMailService {

        @Test
        void shouldSendRequestToMailService() {
            // given
            EmailRequest emailRequest = new EmailRequest();

            when(webClient.post())
                    .thenReturn(requestBodyUriMock);
            when(requestBodyUriMock.uri(anyString()))
                    .thenReturn(requestBodyMock);
            when(requestBodyMock.header(anyString(), anyString()))
                    .thenReturn(requestBodyMock);
            when(requestBodyMock.body(any(), any(Class.class)))
                    .thenReturn(requestHeadersMock);
            when(requestHeadersMock.retrieve())
                    .thenReturn(responseMock);
            when(responseMock.bodyToMono(String.class))
                    .thenReturn(Mono.just("Success"));

            // when
            sendingDataService.sendRequestToMailService(emailRequest);

            // then
            verify(webClient, times(1)).post();
            verify(requestBodyUriMock, times(1)).uri(anyString());
            verify(requestBodyMock, times(1)).header(anyString(), anyString());
            verify(requestBodyMock, times(1)).body(any(), any(Class.class));
            verify(requestHeadersMock, times(1)).retrieve();
            verify(responseMock, times(1)).bodyToMono(String.class);
        }

        @Test
        void shouldThrowSendDataExceptionWhenNotSendEmailRequest() {
            // given
            EmailRequest emailRequest = EmailRequestTestData.builder()
                    .build()
                    .getEmailRequest();

            when(webClient.post())
                    .thenReturn(requestBodyUriMock);
            when(requestBodyUriMock.uri(anyString()))
                    .thenReturn(requestBodyMock);
            when(requestBodyMock.header(anyString(), anyString()))
                    .thenReturn(requestBodyMock);
            when(requestBodyMock.body(any(), any(Class.class)))
                    .thenReturn(requestHeadersMock);
            when(requestHeadersMock.retrieve())
                    .thenReturn(responseMock);
            when(responseMock.bodyToMono(String.class))
                    .thenReturn(Mono.error(new WebClientResponseException("Error", 500, "Internal Server Error", HttpHeaders.EMPTY, null, null)));

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
        assertThat(actual.get("activationLink")).isEqualTo("http://localhost:8081/api/v1/admin/activation?userToken=" + tokenUser);
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