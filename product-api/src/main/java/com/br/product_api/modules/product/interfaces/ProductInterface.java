package com.br.product_api.modules.product.interfaces;

import com.br.product_api.modules.product.dto.ProductRequest;
import com.br.product_api.modules.product.dto.ProductResponse;

import java.util.List;

public interface ProductInterface {

    ProductResponse save(ProductRequest newProduct);

    List<ProductResponse> findBySupplierId (Integer id);

    List<ProductResponse> findByCategoryId (Integer id);

    List<ProductResponse> findProductByName (String name);

    List<ProductResponse> findAll ();

    ProductResponse findById (Integer id);

    void delete (Integer id);

    Boolean existsByCategoryId (Integer id);

    Boolean existsBySupplierId (Integer id);

    ProductResponse update(ProductRequest request, Integer id);
}
