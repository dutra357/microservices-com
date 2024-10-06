package com.br.product_api.modules.category.interfaces;

import com.br.product_api.modules.category.dto.CategoryRequest;
import com.br.product_api.modules.category.dto.CategoryResponse;
import com.br.product_api.modules.category.model.Category;

import java.util.Optional;

public interface CategoryInterface {

    CategoryResponse save(CategoryRequest request);

    Category findById(Integer id);
}
