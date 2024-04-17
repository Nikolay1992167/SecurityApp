package com.solbeg.userservice.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WebClientConfigTest {

    @InjectMocks
    private WebClientConfig webClientConfig;

    @Test
    void shouldReturnExpectedInstanceOfWebclient() {
        // given
        Class<WebClient> expectedClass = WebClient.class;

        // when
        WebClient actualClass = webClientConfig.webClient();

        // then
        assertThat(actualClass).isInstanceOf(expectedClass);
    }
}