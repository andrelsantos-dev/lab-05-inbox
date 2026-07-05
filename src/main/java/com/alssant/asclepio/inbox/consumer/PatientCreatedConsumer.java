package com.alssant.asclepio.inbox.consumer;

import com.alssant.asclepio.patient.messaging.EventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PatientCreatedConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PatientCreatedConsumer.class);

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void onReceive(EventEnvelope envelope) {
        logger.info("Received event: [{}]", envelope.metadata().aggregateId());
    }
}
