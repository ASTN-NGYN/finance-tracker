package com.austin.financetracker.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

import com.austin.financetracker.dto.TransactionDTO;
import com.austin.financetracker.dto.TransactionResponseDTO;
import com.austin.financetracker.dto.TransactionWithCategoryDTO;
import com.austin.financetracker.entity.TransactionType;
import com.austin.financetracker.service.TransactionService;

/**
 * REST controller for managing financial transactions.
 * <p>
 * Provides endpoints for creating, retrieving, updating, and deleting
 * transactions,
 * as well as retrieving aggregated financial data such as income, expenses, and
 * savings.
 * </p>
 */
@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Constructs a new {@code TransactionController} with the given service.
     *
     * @param transactionService the service used for transaction operations
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Retrieves all transactions for a specific user, or filters them by
     * transaction type if provided.
     * <p>
     * If {@code type} is provided, only transactions of that type for the given
     * user are returned.
     * Otherwise, all transactions belonging to the user are returned.
     * </p>
     *
     * @param type    optional transaction type to filter by (e.g., INCOME, EXPENSE)
     * @param userUid the UID of the user whose transactions are being retrieved
     * @return a list of {@link TransactionResponseDTO} objects for the specified
     *         user
     */
    @GetMapping
    public List<TransactionResponseDTO> getAllTransactions(@RequestParam(required = false) TransactionType type,
            @RequestParam String userUid) {
        if (type != null) {
            return transactionService.getTransactionsByType(type, userUid);
        }
        return transactionService.getAllTransactions(userUid);
    }

    /**
     * Retrieves all transactions along with their associated categories for a
     * specific user,
     * optionally filtered by transaction type.
     * <p>
     * If {@code type} is provided, only transactions of that type for the specified
     * user
     * are returned. Otherwise, all transactions for the user are returned.
     * </p>
     *
     * @param type    optional transaction type to filter transactions (e.g.,
     *                INCOME, EXPENSE, SAVING)
     * @param userUid the UID of the user whose transactions are being retrieved
     * @return a list of {@link TransactionWithCategoryDTO} objects matching the
     *         criteria for the specified user
     */
    @GetMapping("/with-categories")
    public List<TransactionWithCategoryDTO> getAllTransactionsWithCategories(
            @RequestParam(required = false) TransactionType type,
            @RequestParam String userUid) {
        if (type != null) {
            return transactionService.getTransactionsWithCategoriesByType(type, userUid);
        }
        return transactionService.getAllTransactionsWithCategories(userUid);
    }

    /**
     * Retrieves the total amount of transactions for a specific type (e.g., INCOME,
     * EXPENSE, SAVING)
     * for a given user.
     *
     * @param type    the type of transaction
     * @param userUid the UID of the user
     * @return the total amount as a {@link BigDecimal}, or zero if no transactions
     */
    @GetMapping("/total")
    public BigDecimal getTotalByType(
            @RequestParam TransactionType type,
            @RequestParam String userUid) {

        return transactionService.getTotalByType(type, userUid);
    }

    /**
     * Retrieves a transaction by its unique identifier for a specific user.
     * <p>
     * This ensures that only the transaction belonging to the user identified
     * by {@code userUid} is returned. If no such transaction exists, a
     * {@link RuntimeException} is thrown.
     * </p>
     *
     * @param id      the ID of the transaction to retrieve
     * @param userUid the UID of the user who owns the transaction
     * @return the {@link TransactionResponseDTO} corresponding to the transaction
     * @throws RuntimeException if the transaction is not found for the specified
     *                          user
     */
    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(@PathVariable Long id, @RequestParam String userUid) {
        return transactionService.getTransactionById(id, userUid);
    }

    /**
     * Retrieves all transactions belonging to a specific category for a given user.
     * <p>
     * Only transactions that belong to the user identified by {@code userUid} and
     * are associated with the category {@code categoryId} are returned.
     * </p>
     *
     * @param categoryId the ID of the category
     * @param userUid    the UID of the user who owns the transactions
     * @return a list of {@link TransactionResponseDTO} objects corresponding to
     *         the transactions for the specified category and user
     */
    @GetMapping("/category")
    public List<TransactionResponseDTO> getTransactionsByCategory(@RequestParam Long categoryId,
            @RequestParam String userUid) {
        return transactionService.getTransactionsByCategory(categoryId, userUid);
    }

    /**
     * Retrieves all transactions for a specific user that occurred within a
     * specified date range.
     * <p>
     * Only transactions belonging to the user identified by {@code userUid} and
     * with dates between {@code startDate} and {@code endDate} (inclusive) are
     * returned.
     * </p>
     *
     * @param startDate the start date of the range (inclusive)
     * @param endDate   the end date of the range (inclusive)
     * @param userUid   the UID of the user whose transactions are being retrieved
     * @return a list of {@link TransactionResponseDTO} objects matching the
     *         criteria
     * @throws RuntimeException if {@code startDate} is after {@code endDate}
     */
    @GetMapping("/range")
    public List<TransactionResponseDTO> getTransactionsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String userUid) {
        return transactionService.getTransactionsByDateRange(startDate, endDate, userUid);
    }

    /**
     * Creates a new transaction for a specific user.
     * <p>
     * The transaction will be associated with the user identified by
     * {@code userUid}.
     * The category referenced in {@code transactionDTO} must exist and belong to
     * the user.
     * </p>
     *
     * @param transactionDTO the data transfer object containing the transaction
     *                       details
     * @param userUid        the UID of the user who will own the transaction
     * @return the created {@link TransactionResponseDTO}
     * @throws RuntimeException if the specified category is not found
     */
    @PostMapping
    public TransactionResponseDTO createTransaction(@RequestBody TransactionDTO transactionDTO,
            @RequestParam String userUid) {
        return transactionService.createTransaction(transactionDTO, userUid);
    }

    /**
     * Updates an existing transaction for a specific user.
     * <p>
     * Only the transaction belonging to the user identified by {@code userUid} will
     * be updated.
     * If the transaction with the given {@code id} does not exist for that user, a
     * {@link RuntimeException} is thrown.
     * The transaction's category must exist if its ID is being updated.
     * </p>
     *
     * @param id             the ID of the transaction to update
     * @param transactionDTO the data transfer object containing updated transaction
     *                       details
     * @param userUid        the UID of the user who owns the transaction
     * @return the updated {@link TransactionResponseDTO}
     * @throws RuntimeException if the transaction or the specified category is not
     *                          found
     */
    @PutMapping("/{id}")
    public TransactionResponseDTO updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO,
            @RequestParam String userUid) {
        return transactionService.updateTransaction(id, transactionDTO, userUid);
    }

    /**
     * Deletes a transaction belonging to a specific user.
     * <p>
     * Only the transaction owned by the user identified by {@code userUid} will be
     * deleted.
     * If no such transaction exists for the given {@code id} and {@code userUid}, a
     * {@link RuntimeException} is thrown.
     * </p>
     *
     * @param id      the ID of the transaction to delete
     * @param userUid the UID of the user who owns the transaction
     * @throws RuntimeException if the transaction is not found for the given user
     */
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id, @RequestParam String userUid) {
        transactionService.deleteTransaction(id, userUid);
    }
}
