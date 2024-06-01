package com.solbeg.userservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.solbeg.userservice.util.Constants.ACTIVATION_QUEUE_NAME;
import static com.solbeg.userservice.util.Constants.ARG_EXCHANGE_DLQ;
import static com.solbeg.userservice.util.Constants.ARG_LENGTH_QUEUE;
import static com.solbeg.userservice.util.Constants.ARG_ROUTING_KEY_DLQ;
import static com.solbeg.userservice.util.Constants.DLQ_EXCHANGE;
import static com.solbeg.userservice.util.Constants.DLQ_NAME;
import static com.solbeg.userservice.util.Constants.DLQ_ROUTING_KEY;
import static com.solbeg.userservice.util.Constants.INFORMATION_QUEUE_NAME;
import static com.solbeg.userservice.util.Constants.ROUTING_KEY_ACTIVATION;
import static com.solbeg.userservice.util.Constants.ROUTING_KEY_INFORMATION;

@Slf4j
@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.username}")
    private String username;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.virtualHost}")
    private String virtualHost;

    @Bean
    public TopicExchange getExchangeName() {
        return new TopicExchange(exchange);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLQ_EXCHANGE);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_NAME)
                .withArgument(ARG_LENGTH_QUEUE, 10)
                .build();
    }

    @Bean
    public Queue getQueueActivation() {
        return QueueBuilder.durable(ACTIVATION_QUEUE_NAME)
                .withArgument(ARG_EXCHANGE_DLQ, DLQ_EXCHANGE)
                .withArgument(ARG_ROUTING_KEY_DLQ, DLQ_ROUTING_KEY)
                .withArgument(ARG_LENGTH_QUEUE, 10)
                .build();
    }

    @Bean
    public Queue getQueueInformation() {
        return QueueBuilder.durable(INFORMATION_QUEUE_NAME)
                .withArgument(ARG_EXCHANGE_DLQ, DLQ_EXCHANGE)
                .withArgument(ARG_ROUTING_KEY_DLQ, DLQ_ROUTING_KEY)
                .withArgument(ARG_LENGTH_QUEUE, 10)
                .build();
    }

    @Bean
    public Binding bindingActivation() {
        return BindingBuilder.bind(getQueueActivation()).to(getExchangeName())
                .with(ROUTING_KEY_ACTIVATION);
    }

    @Bean
    public Binding bindingInformation() {
        return BindingBuilder.bind(getQueueInformation()).to(getExchangeName())
                .with(ROUTING_KEY_INFORMATION);
    }

    @Bean
    public Binding bindingDLQ() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(getMessageConverter());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("Message was delivered successfully!");
            } else {
                log.info("Message delivery failed: " + cause);
            }
        });
        return rabbitTemplate;
    }
}