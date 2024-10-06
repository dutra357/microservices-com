package com.br.product_api.modules.product.dto;

import com.br.product_api.modules.category.dto.CategoryRequest;
import com.br.product_api.modules.supplier.dto.SupplierRequest;

public record ProductRequest(String name, Integer quantity, Integer supplierId, Integer categoryId) {
}
