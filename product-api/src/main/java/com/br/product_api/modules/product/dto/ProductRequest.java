package com.br.product_api.modules.product.dto;

public record ProductRequest(String name, Integer quantity, Integer supplierId, Integer categoryId) {
}
