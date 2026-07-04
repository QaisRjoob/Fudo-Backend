package com.fudo.shared.messaging;

import com.fudo.shared.events.IntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(IntegrationEvent event) {
        log.debug("Publishing {} [{}] to exchange {}", event.getEventType(), event.getEventId(),
                FudoEventsExchange.NAME);
        rabbitTemplate.convertAndSend(FudoEventsExchange.NAME, event.getEventType(), event);
    }
}
