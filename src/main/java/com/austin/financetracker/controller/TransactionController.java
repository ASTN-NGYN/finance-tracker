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
import com.austin.financetracker.security.FirebaseAuthenticationFilter;
import com.austin.financetracker.service.TransactionService;

import jakarta.servlet.http.HttpServletRequest;

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
     * Retrieves all transactions for the authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return a list of {@link TransactionResponseDTO} for the authenticated user
     */
    @GetMapping
    public List<TransactionResponseDTO> getAllTransactions(HttpServletRequest request) {
        String userUid = (String) request.getAttribute("uid");
        return transactionService.getAllTransactions(userUid);
    }

    /**
     * Retrieves all transactions with their categories for the authenticated user.
     * <p>
     * Optionally filters by {@link TransactionType} if {@code type} is provided.
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param type    optional transaction type to filter by (e.g., INCOME, EXPENSE,
     *                SAVING)
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return a list of {@link TransactionWithCategoryDTO} for the authenticated
     *         user
     */
    @GetMapping("/with-categories")
    public List<TransactionWithCategoryDTO> getAllTransactionsWithCategories(
            @RequestParam(required = false) TransactionType type, HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");

        if (type != null) {
            return transactionService.getTransactionsWithCategoriesByType(type, userUid);
        }
        return transactionService.getAllTransactionsWithCategories(userUid);
    }

    /**
     * Returns the total amount of transactions of a given type for the
     * authenticated user.
     *
     * @param type    the type of transaction (e.g., INCOME, EXPENSE, SAVING)
     * @param request the HTTP request containing the user's UID set by the
     *                authentication filter
     * @return the total amount as a {@link BigDecimal}
     */
    @GetMapping("/total")
    public BigDecimal getTotalByType(@RequestParam TransactionType type, HttpServletRequest request) {
        String userUid = (String) request.getAttribute("uid");
        return transactionService.getTotalByType(type, userUid);
    }

    /**
     * Retrieves a specific transaction by its ID for the authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param id      the ID of the transaction to retrieve
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return the {@link TransactionResponseDTO} for the specified transaction and
     *         user
     */
    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(@PathVariable Long id, HttpServletRequest request) {
        String userUid = (String) request.getAttribute("uid");
        return transactionService.getTransactionById(id, userUid);
    }

    /**
     * Retrieves all transactions for the authenticated user belonging to a specific
     * category.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param categoryId the ID of the category to filter transactions by
     * @param request    the {@link HttpServletRequest} containing the "uid"
     *                   attribute
     * @return a list of {@link TransactionResponseDTO} for the specified category
     *         and user
     */
    @GetMapping("/category")
    public List<TransactionResponseDTO> getTransactionsByCategory(
            @RequestParam Long categoryId,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        return transactionService.getTransactionsByCategory(categoryId, userUid);
    }

    /**
     * Retrieves all transactions for the authenticated user within a specific date
     * range.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param startDate the start date of the range (inclusive)
     * @param endDate   the end date of the range (inclusive)
     * @param request   the {@link HttpServletRequest} containing the "uid"
     *                  attribute
     * @return a list of {@link TransactionResponseDTO} for the specified date range
     *         and user
     */
    @GetMapping("/range")
    public List<TransactionResponseDTO> getTransactionsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        return transactionService.getTransactionsByDateRange(startDate, endDate, userUid);
    }

    /**
     * Creates a new transaction for the authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param transactionDTO the transaction details from the request body
     * @param request        the {@link HttpServletRequest} containing the "uid"
     *                       attribute
     * @return the created {@link TransactionResponseDTO} for the authenticated user
     */
    @PostMapping
    public TransactionResponseDTO createTransaction(@RequestBody TransactionDTO transactionDTO,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        return transactionService.createTransaction(transactionDTO, userUid);
    }

    /**
     * Updates an existing transaction for the authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param id             the ID of the transaction to update
     * @param transactionDTO the updated transaction details from the request body
     * @param request        the {@link HttpServletRequest} containing the "uid"
     *                       attribute
     * @return the updated {@link TransactionResponseDTO} for the authenticated user
     */
    @PutMapping("/{id}")
    public TransactionResponseDTO updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        return transactionService.updateTransaction(id, transactionDTO, userUid);
    }

    /**
     * Deletes a transaction for the authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param id      the ID of the transaction to delete
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     */
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id, HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        transactionService.deleteTransaction(id, userUid);
    }
}
