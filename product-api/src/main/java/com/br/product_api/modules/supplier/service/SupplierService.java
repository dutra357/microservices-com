package com.br.product_api.modules.supplier.service;

import com.br.product_api.config.ValidationException;
import com.br.product_api.modules.supplier.dto.SupplierRequest;
import com.br.product_api.modules.supplier.dto.SupplierResponse;
import com.br.product_api.modules.supplier.interfaces.SupplierInterface;
import com.br.product_api.modules.supplier.model.Supplier;
import com.br.product_api.modules.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public SupplierResponse findById(Integer id) {
        var supplier = repository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given id."));
        return new SupplierResponse(supplier.getId(), supplier.getName());
    }

    @Override
    public Supplier findSupplierById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given id."));
    }

    @Override
    public List<SupplierResponse> findByName(String name) {
        var suppliers = repository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(supplier-> new SupplierResponse(supplier.getId(), supplier.getName()))
                .toList();
        return suppliers;
    }

    @Override
    public List<SupplierResponse> findAll() {
        var suppliers = repository.findAll()
                .stream()
                .map(supplier -> new SupplierResponse(supplier.getId(), supplier.getName()))
                .toList();
        return suppliers;
    }

    private void validateSupplierRequest(SupplierRequest category) {
        if (isEmpty(category.name())) {
            throw new ValidationException("The supplier name was not informed.");
        }
    }
}
