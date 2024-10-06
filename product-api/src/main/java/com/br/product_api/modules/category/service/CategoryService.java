package com.br.product_api.modules.category.service;

import com.br.product_api.config.ValidationException;
import com.br.product_api.modules.category.dto.CategoryRequest;
import com.br.product_api.modules.category.dto.CategoryResponse;
import com.br.product_api.modules.category.interfaces.CategoryInterface;
import com.br.product_api.modules.category.model.Category;
import com.br.product_api.modules.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService implements CategoryInterface {

    public final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryResponse save(CategoryRequest request) {
        validateCategoryRequest(request);

        var newCategory = new Category(request.description());
        repository.save(newCategory);
        return new CategoryResponse(newCategory.getId(), newCategory.getDescription());
    }

    @Override
    public Category findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given id."));
    }

    private void validateCategoryRequest(CategoryRequest category) {
        if (isEmpty(category.description())) {
            throw new ValidationException("The category description was not informed.");
        }
    }
}
