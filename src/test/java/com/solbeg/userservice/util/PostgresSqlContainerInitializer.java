package com.solbeg.userservice.util;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@TestPropertySource("classpath:application-test.yml")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class PostgresSqlContainerInitializer {

    private final static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.3");

    @DynamicPropertySource
    static void setUrl(DynamicPropertyRegistry registry) {
        container.start();
        try (Connection connection = container.createConnection("");
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS security");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}