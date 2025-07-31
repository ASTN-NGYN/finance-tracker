package com.austin.financetracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.austin.financetracker.dto.CategoryDTO;
import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.TransactionType;
import com.austin.financetracker.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET /categories or GET /categories?type=EXPENSE (gets by type)
    @GetMapping
    public List<Category> getAllCategories(@RequestParam(required = false) TransactionType type) {
        if (type != null) {
            return categoryService.getCategoriesByType(type);
        }
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    // POST /categories
    @PostMapping
    public Category createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    // POST /categories/default-categories
    @PostMapping("/default-categories")
    public ResponseEntity<String> createDefaultCategories() {
        categoryService.createDefaultCategories();
        return ResponseEntity.ok("Default categories created sucessfully");
    }

    // PUT /categories/{id}
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    // DELETE /categories/{id}
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
