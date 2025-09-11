package com.austin.financetracker.controller;

import java.net.URI;
import java.util.ArrayList;
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
import com.austin.financetracker.security.FirebaseAuthenticationFilter;
import com.austin.financetracker.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;

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
     * Retrieves categories for the authenticated user, optionally filtered by type.
     *
     * @param type    optional {@link TransactionType} to filter categories
     * @param request the HTTP request containing the "uid" attribute
     * @return {@link ResponseEntity} with list of {@link CategoryDTO}, or 204 if no
     *         categories found
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(
            @RequestParam(required = false) TransactionType type,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        List<Category> categories = (type != null)
                ? categoryService.getCategoriesByType(type, userUid)
                : categoryService.getAllCategories(userUid);

        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CategoryDTO> dtoList = new ArrayList<>();
        for (Category c : categories) {
            dtoList.add(new CategoryDTO(c.getId(), c.getName(), c.getDescription(), c.getType()));
        }

        return ResponseEntity.ok(dtoList);
    }

    /**
     * Retrieves a category by its ID for the authenticated user.
     * 
     * @param id      the ID of the category to retrieve
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return a {@link ResponseEntity} containing the {@link CategoryDTO} if found,
     *         or {@code 404 Not Found} if no matching category exists
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id, HttpServletRequest request) {
        String userUid = (String) request.getAttribute("uid");
        Optional<Category> categoryOpt = categoryService.getCategoryById(id, userUid);

        if (categoryOpt.isPresent()) {
            Category c = categoryOpt.get();
            CategoryDTO dto = new CategoryDTO(c.getId(), c.getName(), c.getDescription(), c.getType());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new category for the authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute "uid", set by {@link FirebaseAuthenticationFilter}.
     * <p>
     * Returns 201 Created with the newly created category in the response body
     * and a Location header pointing to the new resource.
     *
     * @param categoryDTO the category data to create
     * @param request     the {@link HttpServletRequest} containing the "uid"
     *                    attribute
     * @return {@link ResponseEntity} containing the created {@link CategoryDTO} and
     *         Location header
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO,
            HttpServletRequest request) {
        String userUid = (String) request.getAttribute("uid");
        Category createdCategory = categoryService.createCategory(categoryDTO, userUid);

        CategoryDTO responseDTO = new CategoryDTO(createdCategory.getId(), createdCategory.getName(),
                createdCategory.getDescription(), createdCategory.getType());

        return ResponseEntity.created(URI.create("/categories/" + createdCategory.getId())).body(responseDTO);
    }

    /**
     * Updates an existing category for the authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param id          the ID of the category to update
     * @param categoryDTO the updated category data
     * @param request     the {@link HttpServletRequest} containing the "uid"
     *                    attribute
     * @return a {@link ResponseEntity} containing the updated {@link CategoryDTO}
     *         and HTTP 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        Category updatedCategory = categoryService.updateCategory(id, categoryDTO, userUid);

        CategoryDTO responseDTO = new CategoryDTO(
                updatedCategory.getId(),
                updatedCategory.getName(),
                updatedCategory.getDescription(),
                updatedCategory.getType());

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Deletes a category for the authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param id      the ID of the category to delete
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return a {@link ResponseEntity} with HTTP 204 No Content if deletion
     *         succeeds
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, HttpServletRequest request) {
        String userUid = (String) request.getAttribute("uid");
        categoryService.deleteCategory(id, userUid);
        return ResponseEntity.noContent().build();
    }
}
