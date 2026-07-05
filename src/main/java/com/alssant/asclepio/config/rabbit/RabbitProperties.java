package com.alssant.asclepio.config.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbitmq")
public record RabbitProperties(
        String exchange,
        String queue,
        String routingKey
) {
}
