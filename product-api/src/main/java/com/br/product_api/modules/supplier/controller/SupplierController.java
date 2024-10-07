package com.br.product_api.modules.supplier.controller;

import com.br.product_api.modules.supplier.dto.SupplierRequest;
import com.br.product_api.modules.supplier.dto.SupplierResponse;
import com.br.product_api.modules.supplier.interfaces.SupplierInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/supplier")
public class SupplierController {

    private final SupplierInterface supplierService;

    public SupplierController(SupplierInterface supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplierResponse> save(@RequestBody(required = false) SupplierRequest supplier) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.save(supplier));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<SupplierResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.findById(id));
    }

    @GetMapping("/name/{parameter}")
    public ResponseEntity<List<SupplierResponse>> findByName(@PathVariable String parameter) {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.findByName(parameter));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        supplierService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<SupplierResponse> update(@RequestBody(required = false) SupplierRequest supplier, @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.update(supplier, id));
    }

}
