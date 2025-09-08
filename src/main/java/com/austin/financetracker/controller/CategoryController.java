package com.austin.financetracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
 * as well as fetching categories by transaction type.
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
     * Retrieves all categories for a specific user, optionally filtering them by
     * transaction type.
     * <p>
     * If {@code type} is provided, only categories of that transaction type for the
     * given user
     * are returned. Otherwise, all categories belonging to the user are returned.
     * </p>
     *
     * @param type    optional transaction type to filter categories (e.g., INCOME,
     *                EXPENSE)
     * @param userUid the UID of the user whose categories are being retrieved
     * @return a list of {@link CategoryDTO} objects matching the criteria for the
     *         specified user
     */
    @GetMapping
    public List<CategoryDTO> getCategories(@RequestParam(required = false) TransactionType type, String userUid) {
        List<Category> categories;
        if (type != null) {
            categories = categoryService.getCategoriesByType(type, userUid);
        } else {
            categories = categoryService.getAllCategories(userUid);
        }
        return categories.stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName(), c.getDescription(), c.getType()))
                .toList();
    }

    /**
     * Retrieves a category by its unique ID for a specific user.
     * <p>
     * This method ensures that the category returned belongs to the user identified
     * by {@code userUid}. If no such category exists for that user, an empty
     * {@link Optional} is returned.
     * </p>
     *
     * @param id      the ID of the category to retrieve
     * @param userUid the UID of the user who owns the category
     * @return an {@link Optional} containing the {@link Category} if found for the
     *         user,
     *         otherwise empty
     */
    @GetMapping("/{id}")
    public Optional<Category> getCategoryById(@PathVariable Long id, String userUid) {
        return categoryService.getCategoryById(id, userUid);
    }

    /**
     * Creates a new category for a specific user based on the provided data.
     * <p>
     * The category will be associated with the user identified by {@code userUid}.
     * </p>
     *
     * @param categoryDTO the data transfer object containing the details of the new
     *                    category
     * @param userUid     the UID of the user who will own the category
     * @return the created {@link Category} entity
     */
    @PostMapping
    public Category createCategory(@RequestBody CategoryDTO categoryDTO, String userUid) {
        return categoryService.createCategory(categoryDTO, userUid);
    }

    /**
     * Updates an existing category for a specific user.
     * <p>
     * Only the category belonging to the user identified by {@code userUid} will be
     * updated.
     * If the category with the given {@code id} does not exist for that user, a
     * {@link RuntimeException} will be thrown.
     * </p>
     *
     * @param id          the ID of the category to update
     * @param categoryDTO the data transfer object containing updated category
     *                    details
     * @param userUid     the UID of the user who owns the category
     * @return the updated {@link Category} entity
     */
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO, String userUid) {
        return categoryService.updateCategory(id, categoryDTO, userUid);
    }

    /**
     * Deletes a category belonging to a specific user.
     * <p>
     * Only the category owned by the user identified by {@code userUid} will be
     * deleted.
     * If the category with the given {@code id} does not exist for that user, a
     * {@link RuntimeException} will be thrown.
     * </p>
     *
     * @param id      the ID of the category to delete
     * @param userUid the UID of the user who owns the category
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id, String userUid) {
        categoryService.deleteCategory(id, userUid);
    }
}
