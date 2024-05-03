package com.solbeg.config;

import com.solbeg.aspect.LoggingAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnMissingBean(LoggingAspect.class)
public class LoggingConfig {

    @PostConstruct
    void init() {
        log.info("LoggingConfig initialized");
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
