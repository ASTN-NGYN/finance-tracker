package com.austin.financetracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.TransactionType;

/*
 * Spring Data JPA Repository interface. A special interface that gives you 
 * automatic database access to Category entities.
 * 
 * CategoryRepository is the app's gateway to the database for Category objects.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> { // Extends, gives you access to CRUD
                                                                            // operations

    // Find category by name (useful for lookups), optional means it might or might
    // not be empty
    Optional<Category> findByName(String name);

    // Find all categories by transaction type (income vs expense categories)
    List<Category> findByType(TransactionType type);

    // Find categories by name containing text (for search functionality)
    List<Category> findByNameContainingIgnoreCase(String name);

    // Check if category exists by name
    boolean existsByName(String name);
}
