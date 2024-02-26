package com.solbeg.userservice.util;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class PostgresSqlContainerInitializer {

    private final static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.3");

    @DynamicPropertySource
    static void setUrl(DynamicPropertyRegistry registry) {
        container.start();
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}