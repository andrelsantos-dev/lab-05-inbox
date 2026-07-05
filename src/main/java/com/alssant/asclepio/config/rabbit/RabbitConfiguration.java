package com.alssant.asclepio.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitConfiguration {
    private final RabbitProperties properties;

    public RabbitConfiguration(RabbitProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(properties.exchange());
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
                .durable(properties.queue())
                .build();
    }

    @Bean
    public Binding bindingPatient(
            Queue patientQueue,
            TopicExchange patientExchange) {
        return BindingBuilder
                .bind(patientQueue)
                .to(patientExchange)
                .with(properties.routingKey());
    }


    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
