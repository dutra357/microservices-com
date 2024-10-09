package com.br.product_api.modules.product.controller;

import com.br.product_api.config.exception.SuccessResponse;
import com.br.product_api.modules.product.dto.ProductRequest;
import com.br.product_api.modules.product.dto.ProductResponse;
import com.br.product_api.modules.product.dto.ProductSalesResponse;
import com.br.product_api.modules.product.dto.VerifyStockQuantity;
import com.br.product_api.modules.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findById(id));
    }

    @GetMapping("/supplier/{id}")
    public ResponseEntity<List<ProductResponse>> findBySupplierId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findBySupplierId(id));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductResponse>> findByCategoryId(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findByCategoryId(id));
    }

    @GetMapping("/name/{parameter}")
    public ResponseEntity<List<ProductResponse>> findProductByName(@PathVariable String parameter) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductByName(parameter));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductResponse> update(@RequestBody(required = false) ProductRequest product, @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(product, id));
    }

    @GetMapping("{productId}/sales")
    public ResponseEntity<ProductSalesResponse> findProductSales(@PathVariable Integer productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductSales(productId));
    }

    @PostMapping("/check-stock")
    public ResponseEntity<SuccessResponse> verifyStock(@RequestBody VerifyStockQuantity request) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.verifyStock(request));
    }
}
