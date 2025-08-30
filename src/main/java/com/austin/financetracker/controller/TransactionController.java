package com.austin.financetracker.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
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
     * Retrieves all transactions or filters them by transaction type if provided.
     *
     * @param type optional transaction type to filter by (e.g., INCOME, EXPENSE)
     * @return a list of {@link TransactionResponseDTO} objects
     */
    @GetMapping
    public List<TransactionResponseDTO> getAllTransactions(@RequestParam(required = false) TransactionType type) {
        if (type != null) {
            return transactionService.getTransactionsByType(type);
        }
        return transactionService.getAllTransactions();
    }

    /**
     * Retrieves all transactions with their associated categories,
     * optionally filtered by transaction type.
     *
     * @param type optional transaction type to filter by
     * @return a list of {@link TransactionWithCategoryDTO} objects
     */
    @GetMapping("/with-categories")
    public List<TransactionWithCategoryDTO> getAllTransactionsWithCategories(
            @RequestParam(required = false) TransactionType type) {
        if (type != null) {
            return transactionService.getTransactionsWithCategoriesByType(type);
        }
        return transactionService.getAllTransactionsWithCategories();
    }

    /**
     * Calculates the total income across all transactions.
     *
     * @return the total income as a {@link BigDecimal}
     */
    @GetMapping("/total-income")
    public BigDecimal getTotalIncome() {
        return transactionService.getTotalIncome();
    }

    /**
     * Calculates the total expenses across all transactions.
     *
     * @return the total expenses as a {@link BigDecimal}
     */
    @GetMapping("/total-expenses")
    public BigDecimal getTotalExpenses() {
        return transactionService.getTotalExpenses();
    }

    /**
     * Calculates the total savings (income - expenses).
     *
     * @return the total savings as a {@link BigDecimal}
     */
    @GetMapping("/total-savings")
    public BigDecimal getTotalSavings() {
        return transactionService.getTotalSavings();
    }

    /**
     * Retrieves a transaction by its unique identifier.
     *
     * @param id the ID of the transaction to retrieve
     * @return the {@link TransactionResponseDTO} if found
     */
    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    /**
     * Retrieves all transactions belonging to a specific category.
     *
     * @param categoryId the ID of the category
     * @return a list of {@link TransactionResponseDTO} objects
     */
    @GetMapping("/category")
    public List<TransactionResponseDTO> getTransactionsByCategory(@RequestParam Long categoryId) {
        return transactionService.getTransactionsByCategory(categoryId);
    }

    /**
     * Retrieves all transactions that occurred within a specified date range.
     *
     * @param startDate the start date of the range (inclusive)
     * @param endDate   the end date of the range (inclusive)
     * @return a list of {@link TransactionResponseDTO} objects
     */
    @GetMapping("/range")
    public List<TransactionResponseDTO> getTransactionsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return transactionService.getTransactionsByDateRange(startDate, endDate);
    }

    /**
     * Creates a new transaction.
     *
     * @param transactionDTO the data for the new transaction
     * @return the created {@link TransactionResponseDTO}
     */
    @PostMapping
    public TransactionResponseDTO createTransaction(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    /**
     * Creates a predefined set of default transactions.
     *
     * @return a {@link ResponseEntity} containing a success message
     */
    @PostMapping("/default-transactions")
    public ResponseEntity<String> createDefaultTransaction() {
        transactionService.createDefaultTransactions();
        return ResponseEntity.ok("Default transactions created successfully");
    }

    /**
     * Updates an existing transaction with the given data.
     *
     * @param id             the ID of the transaction to update
     * @param transactionDTO the updated transaction data
     * @return the updated {@link TransactionResponseDTO}
     */
    @PutMapping("/{id}")
    public TransactionResponseDTO updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        return transactionService.updateTransaction(id, transactionDTO);
    }

    /**
     * Deletes a transaction by its unique identifier.
     *
     * @param id the ID of the transaction to delete
     */
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}
