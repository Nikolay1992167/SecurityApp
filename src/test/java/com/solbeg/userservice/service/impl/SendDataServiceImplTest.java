package com.solbeg.userservice.service.impl;

import com.solbeg.userservice.dto.request.EmailRequest;
import com.solbeg.userservice.entity.User;
import com.solbeg.userservice.entity.UserToken;
import com.solbeg.userservice.enums.EmailType;
import com.solbeg.userservice.exception.SendDataException;
import com.solbeg.userservice.service.UserTokenService;
import com.solbeg.userservice.util.testdata.UserTestData;
import com.solbeg.userservice.util.testdata.UserTokenTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SendDataServiceImplTest {

    @InjectMocks
    private SendDataServiceImpl sendDataService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private UserTokenService userTokenService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(sendDataService, "exchange", "exchangeTest");
    }

    @Nested
    class SendRequestForActivationUser {

        @Test
        void shouldCheckSendMessage() {
            // given
            User user = UserTestData.getJournalist();
            UserToken userToken = UserTokenTestData.getUserToken();

            when(userTokenService.createActivationToken(user.getId()))
                    .thenReturn(userToken);
            // when
            sendDataService.sendRequestForActivationUser(user);

            // then
            verify(rabbitTemplate, times(1))
                    .convertAndSend(any(String.class), any(String.class), any(EmailRequest.class));
        }

        @Test
        void shouldThrowException() {
            // given
            User user = UserTestData.getJournalist();
            UserToken userToken = UserTokenTestData.getUserToken();
            when(userTokenService.createActivationToken(user.getId()))
                    .thenReturn(userToken);
            doThrow(new AmqpException("error")).when(rabbitTemplate)
                    .convertAndSend(any(String.class), any(String.class), any(EmailRequest.class));

            // when, then
            assertThatThrownBy(() -> sendDataService.sendRequestForActivationUser(user))
                    .isExactlyInstanceOf(SendDataException.class);
        }
    }

    @Nested
    class SendInformation {

        @Test
        void shouldCheckSendMessageWhenEmailTypeUserWelcome() {
            // given
            User user = UserTestData.getJournalist();

            // when
            sendDataService.sendInformation(user, EmailType.USER_WELCOME_EMAIL);

            // then
            verify(rabbitTemplate, times(1))
                    .convertAndSend(any(String.class), any(String.class), any(EmailRequest.class));
        }

        @Test
        void shouldCheckSendMessageWhenEmailTypeTokenExpiration() {
            // given
            User user = UserTestData.getJournalist();

            // when
            sendDataService.sendInformation(user, EmailType.USER_TOKEN_EXPIRATION);

            // then
            verify(rabbitTemplate, times(1))
                    .convertAndSend(any(String.class), any(String.class), any(EmailRequest.class));
        }

        @Test
        void shouldThrowException() {
            // given
            User user = UserTestData.getJournalist();
            doThrow(new AmqpException("error")).when(rabbitTemplate)
                    .convertAndSend(any(String.class), any(String.class), any(EmailRequest.class));

            // when, then
            assertThatThrownBy(() -> sendDataService.sendInformation(user, EmailType.USER_WELCOME_EMAIL))
                    .isExactlyInstanceOf(SendDataException.class);
        }
    }
}