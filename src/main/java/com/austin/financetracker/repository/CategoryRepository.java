package com.austin.financetracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.TransactionType;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Find category by name (useful for lookups)
    Optional<Category> findByName(String name);

    // Find all categories by transaction type (income vs expense categories)
    List<Category> findByType(TransactionType type);

    // Find categories by name containing text (for search functionality)
    List<Category> findByNameContainingIgnoreCase(String name);

    // Check if category exists by name
    boolean existsByName(String name);
}
