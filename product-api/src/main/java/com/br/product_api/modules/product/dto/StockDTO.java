package com.br.product_api.modules.product.dto;

import java.util.List;

public record StockDTO(String salesId, List<ProductQuantityDTO> products) {
}
