package com.fudo.shared.messaging;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Activates only for services that pull in spring-boot-starter-amqp themselves
 * (fudo-shared-kernel declares it as an optional dependency). Declares the single
 * topic exchange every service publishes integration events to, keyed by
 * {@link com.fudo.shared.events.IntegrationEvent#getEventType()} as the routing key.
 */
@AutoConfiguration
@ConditionalOnClass(RabbitTemplate.class)
public class RabbitMessagingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TopicExchange fudoEventsExchange() {
        return new TopicExchange(FudoEventsExchange.NAME, true, false);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jackson2JsonMessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventPublisher eventPublisher(RabbitTemplate rabbitTemplate) {
        return new RabbitEventPublisher(rabbitTemplate);
    }
}
