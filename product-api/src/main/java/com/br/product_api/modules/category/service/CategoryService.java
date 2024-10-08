package com.br.product_api.modules.category.service;

import com.br.product_api.config.exception.ValidationException;
import com.br.product_api.modules.category.dto.CategoryRequest;
import com.br.product_api.modules.category.dto.CategoryResponse;
import com.br.product_api.modules.category.interfaces.CategoryInterface;
import com.br.product_api.modules.category.model.Category;
import com.br.product_api.modules.category.repository.CategoryRepository;
import com.br.product_api.modules.product.service.ProductService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService implements CategoryInterface {

    public final CategoryRepository repository;
    public final ProductService productService;

    public CategoryService(CategoryRepository repository, @Lazy ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    @Override
    public CategoryResponse save(CategoryRequest request) {
        validateCategoryRequest(request);

        var newCategory = new Category(request.description());
        repository.save(newCategory);
        return new CategoryResponse(newCategory.getId(), newCategory.getDescription());
    }

    @Override
    public CategoryResponse findById(Integer id) {
        var category = repository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given id."));
        return new CategoryResponse(category.getId(), category.getDescription());
    }

    public Category findCategoryById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given id."));
    }

    @Override
    public List<CategoryResponse> findByDescription(String description) {

        var categories = repository.findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getDescription()))
                .toList();
        return categories;
    }

    @Override
    public List<CategoryResponse> findAll() {
        var response = repository.findAll()
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getDescription()))
                .toList();
        return response;
    }

    @Override
    public void delete(Integer id) {
        if (productService.existsByCategoryId(id)) {
            throw new ValidationException("The category has already been assigned to a product");
        } else {
            repository.delete(repository.findById(id).get());
        }
    }

    @Override
    public CategoryResponse update(CategoryRequest request, Integer id) {
        Category categoryUpdate = repository.findById(id).orElseThrow(
                () -> new ValidationException("Product not found for this ID.")
        );

        if (request.description() != categoryUpdate.getDescription()) {
            categoryUpdate.setDescription(request.description());
        }

        repository.save(categoryUpdate);
        return new CategoryResponse(categoryUpdate.getId(), categoryUpdate.getDescription());
    }

    private void validateId (Integer id) {
        if (isEmpty(id)) {
            throw new ValidationException("The product id must be informed.");
        }
    }

    private void validateCategoryRequest(CategoryRequest category) {
        if (isEmpty(category.description())) {
            throw new ValidationException("The category description was not informed.");
        }
    }
}
