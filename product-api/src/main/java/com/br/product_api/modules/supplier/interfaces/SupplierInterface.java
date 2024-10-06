package com.br.product_api.modules.supplier.interfaces;

import com.br.product_api.modules.supplier.dto.SupplierRequest;
import com.br.product_api.modules.supplier.dto.SupplierResponse;
import com.br.product_api.modules.supplier.model.Supplier;

import java.util.List;

public interface SupplierInterface {

    SupplierResponse save(SupplierRequest request);

    SupplierResponse findById(Integer id);

    Supplier findSupplierById(Integer id);

    List<SupplierResponse> findByName(String description);

    List<SupplierResponse> findAll();

}