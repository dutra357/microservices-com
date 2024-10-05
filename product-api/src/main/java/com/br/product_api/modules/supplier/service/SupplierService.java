package com.br.product_api.modules.supplier.service;

import com.br.product_api.config.ValidationException;
import com.br.product_api.modules.supplier.dto.SupplierRequest;
import com.br.product_api.modules.supplier.dto.SupplierResponse;
import com.br.product_api.modules.supplier.interfaces.SupplierInterface;
import com.br.product_api.modules.supplier.model.Supplier;
import com.br.product_api.modules.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService implements SupplierInterface {

    public final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public SupplierResponse save(SupplierRequest request) {
        validateSupplierRequest(request);

        var newSupplier = new Supplier(request.name());
        repository.save(newSupplier);
        return new SupplierResponse(newSupplier.getId(), newSupplier.getName());
    }

    private void validateSupplierRequest(SupplierRequest category) {
        if (isEmpty(category.name())) {
            throw new ValidationException("The supplier name was not informed.");
        }
    }
}
