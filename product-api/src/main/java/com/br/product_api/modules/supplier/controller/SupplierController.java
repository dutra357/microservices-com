package com.br.product_api.modules.supplier.controller;

import com.br.product_api.modules.supplier.dto.SupplierRequest;
import com.br.product_api.modules.supplier.dto.SupplierResponse;
import com.br.product_api.modules.supplier.interfaces.SupplierInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/supplier")
public class SupplierController {

    private final SupplierInterface supplierService;

    public SupplierController(SupplierInterface supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> save(@RequestBody(required = false) SupplierRequest category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.save(category));
    }

}
