package com.br.product_api.modules.category.interfaces;

import com.br.product_api.modules.category.dto.CategoryRequest;
import com.br.product_api.modules.category.dto.CategoryResponse;

public interface CategoryInterface {

    CategoryResponse save(CategoryRequest request);

}
