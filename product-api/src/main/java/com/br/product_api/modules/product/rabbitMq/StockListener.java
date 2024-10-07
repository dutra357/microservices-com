package com.br.product_api.modules.product.rabbitMq;

import com.br.product_api.modules.product.dto.StockDTO;
import com.br.product_api.modules.product.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StockListener {

    private final ProductService productService;

    public StockListener(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiveStockQueue (StockDTO product) {
        productService.updateProductStock(product);
    }

}
