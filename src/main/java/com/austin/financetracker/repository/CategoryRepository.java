package com.austin.financetracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.TransactionType;

/**
 * Repository interface for accessing {@link Category} entities in the database.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations,
 * as well as custom finder methods for querying categories by name and type.
 * </p>
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds a category by its exact name.
     *
     * @param name the name of the category
     * @return an {@link Optional} containing the category if found, or empty if not
     */
    Optional<Category> findByName(String name);

    /**
     * Finds all categories that match the given {@link TransactionType}.
     *
     * @param type the transaction type (e.g., INCOME, EXPENSE)
     * @return a list of categories with the specified type
     */
    List<Category> findByType(TransactionType type);

    /**
     * Finds all categories whose names contain the given text, ignoring case.
     *
     * @param name the text to search for within category names
     * @return a list of matching categories
     */
    List<Category> findByNameContainingIgnoreCase(String name);

    /**
     * Checks if a category with the given name exists.
     *
     * @param name the name to check
     * @return {@code true} if a category with the name exists, {@code false} otherwise
     */
    boolean existsByName(String name);
}