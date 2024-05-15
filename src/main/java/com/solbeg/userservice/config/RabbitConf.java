package com.solbeg.userservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitConf {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Bean
    public TopicExchange getExchangeName() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue getQueueActivation() {
        return new Queue("activation-queue");
    }

    @Bean
    public Queue getQueueInformation() {
        return new Queue("information-queue");
    }

    @Bean
    public Binding bindingActivation() {
        return BindingBuilder.bind(getQueueActivation()).to(getExchangeName())
                .with("activation");
    }

    @Bean
    public Binding bindingInformation() {
        return BindingBuilder.bind(getQueueInformation()).to(getExchangeName())
                .with("information");
    }

    @Bean
    public Jackson2JsonMessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter();

    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory factory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(getMessageConverter());
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5);
        retryTemplate.setRetryPolicy(retryPolicy);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        return rabbitTemplate;
    }
}