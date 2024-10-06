package com.br.product_api.modules.supplier.interfaces;

import com.br.product_api.modules.supplier.dto.SupplierRequest;
import com.br.product_api.modules.supplier.dto.SupplierResponse;
import com.br.product_api.modules.supplier.model.Supplier;

import java.util.Optional;

public interface SupplierInterface {

    SupplierResponse save(SupplierRequest request);

    Supplier findById(Integer id);

}