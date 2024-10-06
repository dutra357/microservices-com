package com.br.product_api.modules.product.controller;

import com.br.product_api.modules.product.dto.ProductRequest;
import com.br.product_api.modules.product.dto.ProductResponse;
import com.br.product_api.modules.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    public final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> save(@RequestBody(required = false) ProductRequest product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }
}
