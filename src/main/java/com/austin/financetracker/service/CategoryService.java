package com.austin.financetracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.austin.financetracker.dto.CategoryDTO;
import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.TransactionType;
import com.austin.financetracker.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get category by ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Get categories by type (INCOME or EXPENSE)
    public List<Category> getCategoriesByType(TransactionType type) {
        return categoryRepository.findByType(type);
    }

    // Create new category
    public Category createCategory(CategoryDTO categoryDTO) {
        // Business logic: Check if category already exists
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name '" + categoryDTO.getName() + "' already exists");
        }
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setType(categoryDTO.getType());
        return categoryRepository.save(category);
    }

    // Update existing category
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

    // Delete category
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // Search categories by name
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    // Get category by name
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    // Create default categories (useful for initial setup)
    public void createDefaultCategories() {
        // Income categories
        createCategoryIfNotExists("Salary", TransactionType.INCOME);
        createCategoryIfNotExists("Freelance", TransactionType.INCOME);
        createCategoryIfNotExists("Investment", TransactionType.INCOME);
        // Expense categories
        createCategoryIfNotExists("Food", TransactionType.EXPENSE);
        createCategoryIfNotExists("Transportation", TransactionType.EXPENSE);
        createCategoryIfNotExists("Entertainment", TransactionType.EXPENSE);
        createCategoryIfNotExists("Utilities", TransactionType.EXPENSE);
        createCategoryIfNotExists("Healthcare", TransactionType.EXPENSE);

    }

    private void createCategoryIfNotExists(String name, TransactionType type) {
        if (!categoryRepository.existsByName(name)) {
            Category category = new Category(name, "", type);
            categoryRepository.save(category);
        }
    }
}
