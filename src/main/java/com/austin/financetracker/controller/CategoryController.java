package com.austin.financetracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

/**
 * REST controller for managing {@link Category} entities.
 * <p>
 * Provides endpoints for creating, retrieving, updating, and deleting
 * categories,
 * as well as fetching categories by transaction type and creating default
 * categories.
 * </p>
 */
@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Constructs a new {@code CategoryController} with the given service.
     *
     * @param categoryService the service used for category operations
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Retrieves all categories, or filters them by transaction type if provided.
     *
     * @param type optional transaction type to filter categories (e.g., INCOME,
     *             EXPENSE)
     * @return a list of {@link CategoryDTO} objects matching the criteria
     */
    @GetMapping
    public List<CategoryDTO> getCategories(@RequestParam(required = false) TransactionType type) {
        List<Category> categories;
        if (type != null) {
            categories = categoryService.getCategoriesByType(type);
        } else {
            categories = categoryService.getAllCategories();
        }
        return categories.stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName(), c.getDescription(), c.getType()))
                .toList();
    }

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id the ID of the category to retrieve
     * @return an {@link Optional} containing the category if found, otherwise empty
     */
    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    /**
     * Creates a new category based on the provided data.
     *
     * @param categoryDTO the data for the new category
     * @return the created {@link Category} entity
     */
    @PostMapping
    public Category createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    /**
     * Creates a predefined set of default categories in the system.
     *
     * @return a {@link ResponseEntity} with a success message upon completion
     */
    @PostMapping("/default-categories")
    public ResponseEntity<String> createDefaultCategories() {
        categoryService.createDefaultCategories();
        return ResponseEntity.ok("Default categories created sucessfully");
    }

    /**
     * Updates an existing category with the provided data.
     *
     * @param id          the ID of the category to update
     * @param categoryDTO the updated category data
     * @return the updated {@link Category} entity
     */
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    /**
     * Deletes a category by its unique identifier.
     *
     * @param id the ID of the category to delete
     */
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
