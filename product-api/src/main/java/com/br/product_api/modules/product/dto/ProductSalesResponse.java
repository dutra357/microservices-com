package com.br.product_api.modules.product.dto;

import com.br.product_api.modules.category.dto.CategoryResponse;
import com.br.product_api.modules.supplier.dto.SupplierResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ProductSalesResponse(Integer productId, String name, Integer quantityAvailable, LocalDateTime createdAt, SupplierResponse supplierResponse, CategoryResponse categoryResponse, List<String> sales) {
}
