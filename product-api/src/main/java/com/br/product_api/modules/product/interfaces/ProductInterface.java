package com.br.product_api.modules.product.interfaces;

import com.br.product_api.modules.product.dto.ProductRequest;
import com.br.product_api.modules.product.dto.ProductResponse;

public interface ProductInterface {

    ProductResponse save(ProductRequest newProduct);
}
