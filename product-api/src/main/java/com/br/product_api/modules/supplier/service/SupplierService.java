package com.br.product_api.modules.supplier.service;

import com.br.product_api.config.exception.ValidationException;
import com.br.product_api.modules.product.service.ProductService;
import com.br.product_api.modules.supplier.dto.SupplierRequest;
import com.br.product_api.modules.supplier.dto.SupplierResponse;
import com.br.product_api.modules.supplier.interfaces.SupplierInterface;
import com.br.product_api.modules.supplier.model.Supplier;
import com.br.product_api.modules.supplier.repository.SupplierRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService implements SupplierInterface {

    public final SupplierRepository repository;
    public final ProductService productService;

    public SupplierService(SupplierRepository repository, @Lazy ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    @Override
    public SupplierResponse save(SupplierRequest request) {
        validateSupplierRequest(request);

        var newSupplier = new Supplier(request.name());
        repository.save(newSupplier);
        return new SupplierResponse(newSupplier.getId(), newSupplier.getName());
    }

    @Override
    public SupplierResponse update(SupplierRequest request, Integer id) {
        Supplier supplierUpdate = repository.findById(id).orElseThrow(
                () -> new ValidationException("Supplier not found for the given ID.")
        );

        if (request.name() != supplierUpdate.getName()) {
            supplierUpdate.setName(request.name());
        }

        repository.save(supplierUpdate);
        return new SupplierResponse(supplierUpdate.getId(), supplierUpdate.getName());
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

    @Override
    public void delete(Integer id) {
        if (productService.existsBySupplierId(id)) {
            throw new ValidationException("The supplier has already been assigned to a product");
        } else {
            repository.delete(repository.findById(id).get());
        }
    }

    private void validateSupplierRequest(SupplierRequest category) {
        if (isEmpty(category.name())) {
            throw new ValidationException("The supplier name was not informed.");
        }
    }
}
