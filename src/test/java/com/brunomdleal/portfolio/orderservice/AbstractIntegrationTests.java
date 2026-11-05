package com.brunomdleal.portfolio.orderservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * This class is responsible to manage the Docker container lifecycle
 * Supplies dynamically the JDBC connection parameters
 * */

// Starts the full Spring ApplicationContext for integration tests extending this base class
@SpringBootTest
// Identifies this class as a container holder class
@Testcontainers
public abstract class AbstractIntegrationTests {

    //  Gives this variable the responsibility of a container
    @Container
    //  Declares and instantiates the database container
    //  Using static ensures there is only one container used for all the tests
    protected static final PostgreSQLContainer<?> CONTAINER =
            //  Specifies the container to use from Docker Hub.
            new PostgreSQLContainer<>("postgres:16-alpine")
                    //  Sets the database name
                    .withDatabaseName("testdb")
                    //  Sets the database superuser
                    .withUsername("testuser")
                    //  Sets the database password
                    .withPassword("testpass");

    // Give this method the responsibility to configure the integration properties
    @DynamicPropertySource
    // (Configuration callback) Add Dynamic property bindings to the registry object
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Supplies Spring Data JPA with the container’s dynamically assigned host and random ephemeral mapped port
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
        // Passes the container username to Spring's datasource configuration.
        registry.add("spring.datasource.username", CONTAINER::getUsername);
        // Passes the container password to Spring's datasource configuration.
        registry.add("spring.datasource.password", CONTAINER::getPassword);
        // Turns Flyway on so migrations automatically execute against the running Docker container on startup.
        registry.add("spring.flyway.enabled", () -> true);
    }

}
