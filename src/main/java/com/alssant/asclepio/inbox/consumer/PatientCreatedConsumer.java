package com.alssant.asclepio.inbox.consumer;

import com.alssant.asclepio.inbox.service.PatientCreatedProcessor;
import com.alssant.asclepio.patient.messaging.EventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PatientCreatedConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PatientCreatedConsumer.class);

    private final PatientCreatedProcessor processor;

    public PatientCreatedConsumer(PatientCreatedProcessor processor) {
        this.processor = processor;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void onReceive(EventEnvelope envelope) {
        logger.info("Received event: [{}]", envelope.metadata().aggregateId());
        this.processor.process(envelope);
    }
}
