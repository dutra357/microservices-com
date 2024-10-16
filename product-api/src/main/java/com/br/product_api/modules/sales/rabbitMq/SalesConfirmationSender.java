package com.br.product_api.modules.sales.rabbitMq;

import com.br.product_api.modules.sales.dto.SalesConfirmationDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SalesConfirmationSender {

    private final RabbitTemplate rabbitTemplate;
    public SalesConfirmationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationKey;

    public void sendSalesConfirmation(SalesConfirmationDTO message) {
        try {
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationKey, message);
        } catch (Exception e) {
            System.out.println("Error while try send sales confirmation, message: " + e);
        }
    }
}
