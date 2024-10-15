package com.br.product_api.modules.product.rabbitMq;

import com.br.product_api.modules.product.dto.StockDTO;
import com.br.product_api.modules.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StockListener {

    private static final Logger log = LoggerFactory.getLogger(StockListener.class);
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public StockListener(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiveStockQueue (StockDTO product) throws JsonProcessingException {
        log.info("Receiving message with data: {} and TransactionId: {}",
                objectMapper.writeValueAsString(product),
                product.transactionid());
        productService.updateProductStock(product);
    }

}
