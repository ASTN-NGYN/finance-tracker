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
     * Finds all categories that belong to a specific user.
     *
     * @param userUid the UID of the user
     * @return a list of {@link Category} entities owned by the user
     */
    List<Category> findAllByUser_Uid(String userUid);

    /**
     * Finds a category by its exact name, ensuring it belongs to the specified
     * user.
     *
     * @param name the exact name of the category
     * @param uid  the UID of the user who owns the category
     * @return an {@link Optional} containing the {@link Category} if found and
     *         owned by the user,
     *         or empty if no matching category exists for that user
     */
    Optional<Category> findByNameAndUser_Uid(String name, String uid);

    /**
     * Retrieves a category by its unique ID and user UID.
     *
     * @param id      the ID of the category
     * @param userUid the UID of the owning user
     * @return an {@link Optional} containing the category if found, or empty if not
     */
    Optional<Category> findByIdAndUser_Uid(Long id, String uid);

    /**
     * Finds categories by type and the UID of the owning user.
     *
     * @param type the type of transaction
     * @param uid  the UID of the user who owns the categories
     * @return a list of {@link Category} entities matching the given type and user
     */
    List<Category> findByTypeAndUser_Uid(TransactionType type, String uid);

    /**
     * Finds all categories whose names contain the given text (case-insensitive)
     * and belong to the specified user.
     *
     * @param name    the text to search for within category names
     * @param userUid the UID of the user who owns the categories
     * @return a list of {@link Category} entities matching the search text and user
     */
    List<Category> findByNameContainingIgnoreCaseAndUser_Uid(String name, String userUid);

    /**
     * Checks if a category with the given name exists.
     *
     * @param name the name to check
     * @return {@code true} if a category with the name exists, {@code false}
     *         otherwise
     */
    boolean existsByNameAndUser_Uid(String name, String userUid);

}