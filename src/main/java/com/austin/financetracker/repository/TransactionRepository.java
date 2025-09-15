package com.austin.financetracker.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.Transaction;
import com.austin.financetracker.entity.TransactionType;

/**
 * Repository interface for accessing {@link Transaction} entities in the
 * database.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations,
 * as well as custom finder and aggregate methods for transactions.
 * </p>
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Retrieves all transactions associated with a specific user.
     *
     * @param userUid the UID of the user whose transactions are to be retrieved
     * @return a list of {@link Transaction} entities belonging to the specified
     *         user
     */
    List<Transaction> findByUser_Uid(String userUid);

    /**
     * Finds a transaction by its ID and the UID of the user who owns it.
     *
     * @param id      the ID of the transaction
     * @param userUid the UID of the user
     * @return an {@link Optional} containing the transaction if found
     */
    Optional<Transaction> findByIdAndUser_Uid(Long id, String userUid);

    /**
     * Finds transactions by type and associated user's UID.
     *
     * @param type    the transaction type
     * @param userUid the UID of the user who owns the transactions
     * @return list of {@link Transaction} entities
     */
    List<Transaction> findByTypeAndUser_Uid(TransactionType type, String userUid);

    /**
     * Finds all transactions of a user belonging to a specific {@link Category}.
     *
     * @param category the category to filter by
     * @param userUid  the UID of the user to filter by
     * @return a list of transactions in the specified category of a user
     */
    List<Transaction> findByCategoryAndUser_Uid(Category category, String userUid);

    /**
     * Finds all transactions for a specific user that occurred within the given
     * date range.
     *
     * @param startDate the start of the date range (inclusive)
     * @param endDate   the end of the date range (inclusive)
     * @param userUid   the UID of the user whose transactions are being retrieved
     * @return a list of {@link Transaction} entities matching the date range and
     *         user
     */
    List<Transaction> findByDateBetweenAndUser_Uid(LocalDate startDate, LocalDate endDate, String userUid);

    /**
     * Finds all transactions whose descriptions contain the specified text
     * (case-insensitive).
     *
     * @param description the text to search for
     * @return a list of matching transactions
     */
    List<Transaction> findByDescriptionContainingIgnoreCase(String description);

    /**
     * Finds all transactions of a specific type within a given date range.
     *
     * @param type      the transaction type
     * @param startDate the start date (inclusive)
     * @param endDate   the end date (inclusive)
     * @return a list of transactions matching the type and date range
     */
    List<Transaction> findByTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);

    /**
     * Finds all transactions of a specific category within a given date range.
     *
     * @param category  the category to filter by
     * @param startDate the start date (inclusive)
     * @param endDate   the end date (inclusive)
     * @return a list of transactions matching the category and date range
     */
    List<Transaction> findByCategoryAndDateBetween(Category category, LocalDate startDate, LocalDate endDate);

    /**
     * Returns the total amount of all transactions of a user by a specific type.
     *
     * @param type    the transaction type
     * @param userUid the user Uid
     * @return the sum of amounts for the given type of a user
     */
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.user.uid = :userUid")
    BigDecimal getTotalAmountByTypeAndUser(@Param("type") TransactionType type, @Param("userUid") String userUid);

    /**
     * Returns the total amount of all transactions of a specific type within a date
     * range.
     *
     * @param type      the transaction type
     * @param startDate the start date (inclusive)
     * @param endDate   the end date (inclusive)
     * @return the sum of amounts for the given type and date range
     */
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByTypeAndDateRange(@Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Returns the total amount of all transactions in a specific category.
     *
     * @param category the category to filter by
     * @return the sum of amounts for the given category
     */
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.category = :category")
    BigDecimal getTotalAmountCategory(@Param("category") Category category);

    /**
     * Finds the most recent 10 transactions, ordered by date descending.
     *
     * @return a list of the 10 most recent transactions
     */
    List<Transaction> findTop10ByOrderByDateDesc();

    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.category = :category")
    void deleteByCategory(@Param("category") Category category);

}