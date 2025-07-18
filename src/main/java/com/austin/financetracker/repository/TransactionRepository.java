package com.austin.financetracker.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.Transaction;
import com.austin.financetracker.entity.TransactionType;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find transactions by type (INCOME or EXPENSE)
    List<Transaction> findByType(TransactionType type);

    // Find transaction by category
    List<Transaction> findByCategory(Category category);

    // Find transactions by date range
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // Find transactions by description containing text
    List<Transaction> findByDescriptionContainingIgnoreCase(String description);

    // Find transactions by type and date range
    List<Transaction> findByTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);

    // Find transactions by category and date range
    List<Transaction> findByCategoryAndDateBetween(Category category, LocalDate startDate, LocalDate endDate);

    // Custom query to get total amount by type
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type")
    BigDecimal getTotalAmountByType(@Param("type") TransactionType type);

    // Custom query to get total amount by type and date range
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByTypeAndDateRange(@Param("type") TransactionType type,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    // Custom query to get total amount by type and date range
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.category = :category")
    BigDecimal getTotalAmountCategory(@Param("category") Category category);

    // Find recent transactions (last N transactions)
    List<Transaction> findTop10ByOrderByDateDesc();

}
