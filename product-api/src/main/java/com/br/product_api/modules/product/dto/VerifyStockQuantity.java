package com.br.product_api.modules.product.dto;

import java.util.List;

public record VerifyStockQuantity(List<ProductQuantityDTO> products) {
}
