package com.br.product_api.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class rabbitConfig {

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingKey.product-stock}")
    private String productStockKey;

    @Value("${app-config.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationKey;

    @Value("${app-config.rabbit.queue.product-stock}")
    private String productSockMq;

    @Value("${app-config.rabbit.queue.sales-confirmation}")
    private String salesConfirmationMq;


    @Bean
    public TopicExchange productTopicExchange() {
        return new TopicExchange(productTopicExchange);
    }

    @Bean
    public Queue productSockMq() {
        return new Queue(productSockMq, true);
    }

    @Bean
    public Queue salesConfirmationMq() {
        return new Queue(salesConfirmationMq, true);
    }

    @Bean
    public Binding productSockMqBinding(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(productSockMq())
                .to(topicExchange)
                .with(productStockKey);
    }

    @Bean
    public Binding salesConfirmationMqBinding(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(salesConfirmationMq())
                .to(topicExchange)
                .with(salesConfirmationKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
