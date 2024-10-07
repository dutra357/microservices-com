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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService implements ProductInterface {

    private final ProductRepository repository;
    private final SupplierService supplierService;
    private final CategoryService categoryService;

    public ProductService(ProductRepository repository,
                          @Lazy SupplierService supplierService, @Lazy CategoryService categoryService) {
        this.repository = repository;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        validateProductRequest(request);

        var category = categoryService.findCategoryById(request.categoryId());
        var supplier = supplierService.findSupplierById(request.supplierId());

        var newProduct = new Product(request.name(), supplier, category, request.quantity());
        return responseBuilder(repository.save(newProduct));
    }

    @Override
    public List<ProductResponse> findBySupplierId(Integer id) {
        var productsBySupplier = repository.findBySupplierId(id);
        return productListResponseBuilder(productsBySupplier);
    }

    @Override
    public List<ProductResponse> findByCategoryId(Integer id) {
        var productsByCategory = repository.findByCategoryId(id);
        return productListResponseBuilder(productsByCategory);
    }

    @Override
    public List<ProductResponse> findProductByName(String name) {
        var productsByName = repository.findByNameIgnoreCaseContaining(name);
        return productListResponseBuilder(productsByName);
    }

    @Override
    public List<ProductResponse> findAll() {
        var allProducts = repository.findAll();
        return productListResponseBuilder(allProducts);
    }

    @Override
    public ProductResponse findById(Integer id) {
        Product product = repository.findById(id)
                    .orElseThrow(() -> new ValidationException("Product not found."));
        return responseBuilder(product);
    }

    @Override
    public void delete (Integer id) {
        repository.delete(repository.findById(id).orElseThrow(
                () -> new ValidationException("Product not found.")
        ));
    }

    @Override
    public Boolean existsByCategoryId(Integer id) {
        return repository.existsByCategoryId(id);
    }

    @Override
    public Boolean existsBySupplierId(Integer id) {
        return repository.existsBySupplierId(id);
    }

    @Override
    public ProductResponse update(ProductRequest request, Integer id) {
        Product productUpdate = repository.findById(id).orElseThrow(
                () -> new ValidationException("Product not found for the given ID.")
        );

        if (request.name() != productUpdate.getName()) {
            productUpdate.setName(request.name());
        }

        if (request.supplierId() != productUpdate.getSupplier().getId()) {
            productUpdate.setSupplier(supplierService.findSupplierById(productUpdate.getSupplier().getId()));
        }

        if (request.categoryId() != productUpdate.getCategory().getId()) {
            productUpdate.setCategory(categoryService.findCategoryById(productUpdate.getCategory().getId()));
        }

        if (request.quantity() != productUpdate.getQuantity()) {
            productUpdate.setQuantity(request.quantity());
        }

        return responseBuilder(repository.save(productUpdate));
    }

    private ProductResponse responseBuilder(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getQuantity(), product.getCreateAt(),
                new SupplierResponse(product.getSupplier().getId(), product.getSupplier().getName()),
                new CategoryResponse(product.getCategory().getId(), product.getCategory().getDescription()));
    }

    private List<ProductResponse> productListResponseBuilder (List<Product> list) {
        return list.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(),
                        product.getQuantity(), product.getCreateAt(),
                        new SupplierResponse(product.getSupplier().getId(), product.getSupplier().getName()),
                        new CategoryResponse(product.getCategory().getId(), product.getCategory().getDescription())))
                .toList();
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
