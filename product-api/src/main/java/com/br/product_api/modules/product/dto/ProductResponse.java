package com.br.product_api.modules.product.dto;

import com.br.product_api.modules.category.dto.CategoryResponse;
import com.br.product_api.modules.supplier.dto.SupplierResponse;

import java.time.LocalDateTime;

public record ProductResponse(Integer id, String name, Integer quantity, LocalDateTime createAt, SupplierResponse supplier, CategoryResponse category) {
}
