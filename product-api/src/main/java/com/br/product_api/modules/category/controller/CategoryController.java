package com.br.product_api.modules.category.controller;

import com.br.product_api.modules.category.dto.CategoryRequest;
import com.br.product_api.modules.category.dto.CategoryResponse;
import com.br.product_api.modules.category.interfaces.CategoryInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/category")
public class CategoryController {

    private final CategoryInterface categoryService;

    public CategoryController(CategoryInterface categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> save(@RequestBody(required = false) CategoryRequest category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findById(id));
    }

    @GetMapping("/description/{parameter}")
    public ResponseEntity<List<CategoryResponse>> findByDescription(@PathVariable String parameter) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findByDescription(parameter));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryResponse> update(@RequestBody(required = false) CategoryRequest category, @PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(category, id));
    }

}
