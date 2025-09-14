package com.austin.financetracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.austin.financetracker.dto.CategoryDTO;
import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.TransactionType;
import com.austin.financetracker.entity.User;
import com.austin.financetracker.repository.CategoryRepository;
import com.austin.financetracker.repository.TransactionRepository;
import com.austin.financetracker.repository.UserRepository;

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
    private final UserRepository userRepository;

    /**
     * Constructs a {@code CategoryService} with the specified repositories.
     *
     * @param categoryRepository    the repository used for category persistence
     * @param transactionRepository the repository used for transaction persistence
     */
    public CategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository,
            UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<Category> getAllCategories(String userUid) {
        return categoryRepository.findAllByUser_Uid(userUid);
    }

    /**
     * Retrieves a {@link User} entity by its unique identifier (UID).
     * <p>
     * This method queries the {@link UserRepository} for a user with the given UID.
     * If no user is found, a {@link RuntimeException} is thrown.
     * </p>
     *
     * @param userUid the unique identifier of the user to retrieve
     * @return the {@link User} entity associated with the given UID
     * @throws RuntimeException if no user is found with the provided UID
     */
    public User getUserByUid(String userUid) {
        return userRepository.findByUserUid(userUid)
                .orElseThrow(() -> new RuntimeException("User not found with uid: " + userUid));
    }

    /**
     * Retrieves a category by its unique ID, ensuring it belongs to the specified
     * user.
     * <p>
     * This method prevents users from accessing categories that do not belong to
     * them
     * by requiring the user's UID along with the category ID.
     * </p>
     *
     * @param id      the unique identifier of the category
     * @param userUid the UID of the user who owns the category
     * @return an {@link Optional} containing the {@link Category} if found and
     *         owned by the user,
     *         or empty if no such category exists
     */
    public Optional<Category> getCategoryById(Long id, String userUid) {
        return categoryRepository.findByIdAndUser_Uid(id, userUid);
    }

    /**
     * Retrieves all categories of a specific {@link TransactionType} for a given
     * user.
     * <p>
     * Only categories owned by the user identified by {@code uid} will be returned.
     * </p>
     *
     * @param type    the type of transaction (e.g., INCOME or EXPENSE)
     * @param userUid the UID of the user who owns the categories
     * @return a list of {@link Category} entities matching the specified type and
     *         user
     */
    public List<Category> getCategoriesByType(TransactionType type, String userUid) {
        return categoryRepository.findByTypeAndUser_Uid(type, userUid);
    }

    /**
     * Creates a new category for a specific user from the provided
     * {@link CategoryDTO}.
     * <p>
     * This method ensures that the category is linked to the user identified by
     * {@code userUid}
     * and that no duplicate category names exist for that user.
     * </p>
     *
     * @param categoryDTO the data transfer object containing category details
     * @param userUid     the UID of the user creating the category
     * @return the saved {@link Category} entity
     * @throws IllegalArgumentException if a category with the same name already
     *                                  exists for this user
     * @throws RuntimeException         if the user with the given UID does not
     *                                  exist
     */
    public Category createCategory(CategoryDTO categoryDTO, String userUid) {
        User user = userRepository.findByUserUid(userUid)
                .orElseThrow(() -> new RuntimeException("User not found with UID: " + userUid));

        if (categoryRepository.existsByNameAndUser_Uid(categoryDTO.getName(), userUid)) {
            throw new IllegalArgumentException(
                    "Category with name '" + categoryDTO.getName() + "' already exists for this user");
        }

        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setType(categoryDTO.getType());
        category.setUser(user);

        return categoryRepository.save(category);
    }

    /**
     * Updates an existing category identified by its ID with the provided
     * {@link CategoryDTO} values,
     * ensuring the category belongs to the specified user.
     *
     * @param id                 the ID of the category to update
     * @param updatedCategoryDTO the data transfer object containing updated details
     * @param userUid            the UID of the user who owns the category
     * @return the updated {@link Category} entity
     * @throws RuntimeException if the category does not exist or does not belong to
     *                          the user
     */
    public Category updateCategory(Long id, CategoryDTO updatedCategoryDTO, String userUid) {
        return categoryRepository.findByIdAndUser_Uid(id, userUid)
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
                .orElseThrow(() -> new RuntimeException(
                        "Category not found with id: " + id + " for user: " + userUid));
    }

    /**
     * Deletes a category identified by its ID, ensuring it belongs to the specified
     * user.
     * <p>
     * This method first checks that the category exists and is owned by the user
     * with the given UID.
     * If the category is found, it is deleted. Transactions associated with the
     * category
     * should be handled via cascade rules or separately to avoid constraint
     * violations.
     * </p>
     *
     * @param id      the ID of the category to delete
     * @param userUid the UID of the user who owns the category
     * @throws RuntimeException if no category with the specified ID exists for the
     *                          user
     */
    @Transactional
    public void deleteCategory(Long id, String userUid) {
        Category category = categoryRepository.findByIdAndUser_Uid(id, userUid)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        transactionRepository.deleteByCategory(category);

        categoryRepository.delete(category);
    }

    public List<Category> searchCategoriesByName(String name, String userUid) {
        return categoryRepository.findByNameContainingIgnoreCaseAndUser_Uid(name, userUid);
    }

    /**
     * Retrieves a category by its exact name for a specific user.
     * <p>
     * This method ensures that only categories belonging to the user identified by
     * {@code userUid}
     * are returned.
     * </p>
     *
     * @param name    the exact name of the category
     * @param userUid the UID of the user who owns the category
     * @return an {@link Optional} containing the {@link Category} if found for the
     *         user,
     *         or empty if no matching category exists
     */
    public Optional<Category> getCategoryByName(String name, String userUid) {
        return categoryRepository.findByNameAndUser_Uid(name, userUid);
    }
}
