package com.austin.financetracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.austin.financetracker.dto.CategoryDTO;
import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.TransactionType;
import com.austin.financetracker.repository.CategoryRepository;
import com.austin.financetracker.repository.TransactionRepository;

import jakarta.transaction.Transactional;

/**
 * Service class for managing {@link Category} entities.
 * <p>
 * Provides business logic for creating, updating, deleting, and retrieving
 * categories, as well as searching by name and creating default categories.
 * </p>
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructs a {@code CategoryService} with the specified repositories.
     *
     * @param categoryRepository    the repository used for category persistence
     * @param transactionRepository the repository used for transaction persistence
     */
    public CategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return a list of all {@link Category} entities
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieves a category by its unique ID.
     *
     * @param id the ID of the category
     * @return an {@link Optional} containing the category if found, or empty if not
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * Retrieves categories filtered by {@link TransactionType}.
     *
     * @param type the type of transaction (INCOME or EXPENSE)
     * @return a list of categories of the specified type
     */
    public List<Category> getCategoriesByType(TransactionType type) {
        return categoryRepository.findByType(type);
    }

    /**
     * Creates a new category from the provided {@link CategoryDTO}.
     *
     * @param categoryDTO the data transfer object containing category details
     * @return the saved {@link Category} entity
     * @throws IllegalArgumentException if a category with the same name already
     *                                  exists
     */
    public Category createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name '" + categoryDTO.getName() + "' already exists");
        }
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setType(categoryDTO.getType());
        return categoryRepository.save(category);
    }

    /**
     * Updates an existing category identified by its ID with the provided
     * {@link CategoryDTO} values.
     *
     * @param id                 the ID of the category to update
     * @param updatedCategoryDTO the data transfer object containing updated details
     * @return the updated {@link Category} entity
     * @throws RuntimeException if the category with the given ID does not exist
     */
    public Category updateCategory(Long id, CategoryDTO updatedCategoryDTO) {
        return categoryRepository.findById(id)
                .map(category -> {
                    if (updatedCategoryDTO.getName() != null) {
                        category.setName(updatedCategoryDTO.getName());
                    }
                    if (updatedCategoryDTO.getDescription() != null) {
                        category.setDescription(updatedCategoryDTO.getDescription());
                    }
                    if (updatedCategoryDTO.getType() != null) {
                        category.setType(updatedCategoryDTO.getType());
                    }
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    /**
     * Deletes a category by its unique identifier and removes all related
     * transactions.
     *
     * @param id the ID of the category to delete
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Delete all transactions for this category
        transactionRepository.deleteByCategory(category);

        // Delete the category itself
        categoryRepository.delete(category);
    }

    /**
     * Searches categories by name containing the given text, case-insensitive.
     *
     * @param name the search text
     * @return a list of matching {@link Category} entities
     */
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Retrieves a category by its exact name.
     *
     * @param name the name of the category
     * @return an {@link Optional} containing the category if found, or empty if not
     */
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * Creates a set of default categories for initial application setup.
     * <p>
     * Includes default income and expense categories.
     * </p>
     */
    public void createDefaultCategories() {
        createCategoryIfNotExists("Salary", TransactionType.INCOME);
        createCategoryIfNotExists("Investment", TransactionType.INCOME);
        createCategoryIfNotExists("Food", TransactionType.EXPENSE);
        createCategoryIfNotExists("Transportation", TransactionType.EXPENSE);
        createCategoryIfNotExists("Entertainment", TransactionType.EXPENSE);
        createCategoryIfNotExists("Housing", TransactionType.EXPENSE);
        createCategoryIfNotExists("Healthcare", TransactionType.EXPENSE);
    }

    /**
     * Helper method to create a category if it does not already exist.
     *
     * @param name the name of the category
     * @param type the transaction type of the category
     */
    private void createCategoryIfNotExists(String name, TransactionType type) {
        if (!categoryRepository.existsByName(name)) {
            Category category = new Category(name, "", type);
            categoryRepository.save(category);
        }
    }
}
