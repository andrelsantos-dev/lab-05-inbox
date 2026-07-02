package com.alssant.asclepio;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    @SuppressWarnings("resource")//Life cycle managed by TestContainers
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"))
                // For local purposes only
                .withCommand(
                        "postgres",
                        "-c", "fsync=off",                // Turns off disk synchronization; drastically speeds up writes
                        "-c", "synchronous_commit=off",   // Does not wait for WAL disk writes before returning success
                        "-c", "full_page_writes=off",     // Disables page recovery protection (unnecessary for testing)
                        "-c", "shared_buffers=256MB",     // Increases dedicated RAM cache for database pages
                        "-c", "work_mem=32MB"             // Allocates more RAM for sorting and complex queries/joins
                );
    }

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return new RabbitMQContainer(DockerImageName.parse("rabbitmq:4.3-management-alpine"));
    }

}
