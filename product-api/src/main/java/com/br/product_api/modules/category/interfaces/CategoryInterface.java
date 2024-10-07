package com.br.product_api.modules.category.interfaces;

import com.br.product_api.modules.category.dto.CategoryRequest;
import com.br.product_api.modules.category.dto.CategoryResponse;
import com.br.product_api.modules.category.model.Category;

import java.util.List;

public interface CategoryInterface {

    CategoryResponse save(CategoryRequest request);

    CategoryResponse findById(Integer id);

    Category findCategoryById(Integer id);

    List<CategoryResponse> findByDescription(String description);

    List<CategoryResponse> findAll();

    void delete (Integer id);

    CategoryResponse update(CategoryRequest request, Integer id);
}
