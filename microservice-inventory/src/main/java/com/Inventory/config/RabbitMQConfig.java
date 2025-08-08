package com.Inventory.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "reservation.exchange";

    public static final String CREATED_QUEUE = "inventory.reservation.created.queue";
    public static final String CONFIRMED_QUEUE = "inventory.reservation.confirmed.queue";
    public static final String TIME_INCREASED_QUEUE = "inventory.reservation.time.increased.queue";

    public static final String CREATED_ROUTING_KEY = "reservation.created";
    public static final String CONFIRMED_ROUTING_KEY = "reservation.confirmed";
    public static final String TIME_INCREASED_ROUTING_KEY = "reservation.time.increased";

    @Bean
    public TopicExchange reservationExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue reservationCreatedQueue() {
        return new Queue(CREATED_QUEUE);
    }

    @Bean
    public Queue reservationConfirmedQueue() {
        return new Queue(CONFIRMED_QUEUE);
    }

    @Bean
    public Queue reservationTimeIncreasedQueue() {
        return new Queue(TIME_INCREASED_QUEUE);
    }

    @Bean
    public Binding createdBinding() {
        return BindingBuilder
                .bind(reservationCreatedQueue())
                .to(reservationExchange())
                .with(CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding confirmedBinding() {
        return BindingBuilder
                .bind(reservationConfirmedQueue())
                .to(reservationExchange())
                .with(CONFIRMED_ROUTING_KEY);
    }

    @Bean
    public Binding timeIncreasedBinding() {
        return BindingBuilder
                .bind(reservationTimeIncreasedQueue())
                .to(reservationExchange())
                .with(TIME_INCREASED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}