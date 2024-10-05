package com.br.product_api.modules.supplier.interfaces;

import com.br.product_api.modules.supplier.dto.SupplierRequest;
import com.br.product_api.modules.supplier.dto.SupplierResponse;

public interface SupplierInterface {

    SupplierResponse save(SupplierRequest request);

}