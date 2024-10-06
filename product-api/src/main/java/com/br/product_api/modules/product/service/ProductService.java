package com.br.product_api.modules.product.service;

import com.br.product_api.config.ValidationException;
import com.br.product_api.modules.category.dto.CategoryResponse;
import com.br.product_api.modules.category.service.CategoryService;
import com.br.product_api.modules.product.dto.ProductRequest;
import com.br.product_api.modules.product.dto.ProductResponse;
import com.br.product_api.modules.product.interfaces.ProductInterface;
import com.br.product_api.modules.product.model.Product;
import com.br.product_api.modules.product.repository.ProductRepository;
import com.br.product_api.modules.supplier.dto.SupplierResponse;
import com.br.product_api.modules.supplier.service.SupplierService;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService implements ProductInterface {

    private final ProductRepository repository;
    private final SupplierService supplierService;
    private final CategoryService categoryService;

    public ProductService(ProductRepository repository, SupplierService supplierService, CategoryService categoryService) {
        this.repository = repository;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        validateProductRequest(request);

        var category = categoryService.findById(request.categoryId());
        var supplier = supplierService.findById(request.supplierId());
        var newProduct = new Product(request.name(), supplier, category, request.quantity());

        repository.save(newProduct);
        return new ProductResponse(newProduct.getId(), newProduct.getName(),
                request.quantity(), newProduct.getCreateAt(), new SupplierResponse(supplier.getId(),
                supplier.getName()), new CategoryResponse(category.getId(), category.getDescription()));
    }

    private void validateProductRequest(ProductRequest product) {
        if (isEmpty(product.name())) {
            throw new ValidationException("A product name was not informed.");
        }

        if (isEmpty(product.quantity())) {
            throw new ValidationException("The quantity should be informed.");
        }

        if (product.quantity() < 0) {
            throw new ValidationException("The quantity should not be less to zero.");
        }

        if (isEmpty(product.categoryId())) {
            throw new ValidationException("A category ID was not informed.");
        }

        if (isEmpty(product.supplierId())) {
            throw new ValidationException("A supplier ID was not informed.");
        }
    }
}
