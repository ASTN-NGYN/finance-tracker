package com.austin.financetracker.controller;

import java.math.BigDecimal;
import java.net.URI;
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
@RequestMapping("/api/transactions")
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
     * <p>
     * Returns:
     * <ul>
     * <li>{@code 200 OK} with the list of transactions if any exist</li>
     * <li>{@code 204 No Content} if the user has no transactions</li>
     * </ul>
     *
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return a {@link ResponseEntity} containing a list of
     *         {@link TransactionResponseDTO}
     *         or no content
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(HttpServletRequest request) {
        String userUid = (String) request.getAttribute("uid");
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions(userUid);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }

    /**
     * Retrieves all transactions with their categories for the authenticated user.
     * <p>
     * Optionally filters by {@link TransactionType} if {@code type} is provided.
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid" set by {@link FirebaseAuthenticationFilter}.
     *
     * @param type    optional transaction type to filter by (e.g., INCOME, EXPENSE,
     *                SAVING)
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return a {@link ResponseEntity} containing a list of
     *         {@link TransactionWithCategoryDTO}
     *         (200 OK) or 204 No Content if no transactions exist
     */
    @GetMapping("/with-categories")
    public ResponseEntity<List<TransactionWithCategoryDTO>> getAllTransactionsWithCategories(
            @RequestParam(required = false) TransactionType type,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        List<TransactionWithCategoryDTO> transactions;

        if (type != null) {
            transactions = transactionService.getTransactionsWithCategoriesByType(type, userUid);
        } else {
            transactions = transactionService.getAllTransactionsWithCategories(userUid);
        }

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(transactions);
    }

    /**
     * Returns the total amount of transactions of a given type for the
     * authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param type    the type of transaction (e.g., INCOME, EXPENSE, SAVING)
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return a {@link ResponseEntity} containing the total amount as a
     *         {@link BigDecimal}
     */
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalByType(
            @RequestParam TransactionType type,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        BigDecimal total = transactionService.getTotalByType(type, userUid);

        return ResponseEntity.ok(total);
    }

    /**
     * Retrieves a transaction by ID for the authenticated user.
     * <p>
     * The Firebase UID is obtained from the {@link HttpServletRequest} attribute
     * "uid"
     * set by {@link FirebaseAuthenticationFilter}.
     *
     * @param id      the ID of the transaction
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return {@link ResponseEntity} with the transaction if found, or 404 Not
     *         Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @PathVariable Long id,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");

        TransactionResponseDTO transaction = transactionService.getTransactionById(id, userUid);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(transaction);
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
     * @return a {@link ResponseEntity} containing the list of
     *         {@link TransactionResponseDTO}
     *         and HTTP 200 OK if transactions exist, or 204 No Content if none are
     *         found
     */
    @GetMapping("/category")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByCategory(
            @RequestParam Long categoryId,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByCategory(categoryId, userUid);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }

    /**
     * Retrieves all transactions for the authenticated user within a specified date
     * range.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}.
     *
     * @param startDate the start date (inclusive) for filtering transactions
     * @param endDate   the end date (inclusive) for filtering transactions
     * @param request   the {@link HttpServletRequest} containing the "uid"
     *                  attribute
     * @return a {@link ResponseEntity} containing the list of
     *         {@link TransactionResponseDTO}
     *         and HTTP 200 OK if transactions exist, or 204 No Content if none are
     *         found
     */
    @GetMapping("/range")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByDateRange(startDate, endDate,
                userUid);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }

    /**
     * Creates a new transaction for the currently authenticated user.
     * <p>
     * The user's Firebase UID is extracted from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}. The transaction is saved
     * to the database and returned as a {@link TransactionResponseDTO}.
     * <p>
     * Returns a {@code 201 Created} response with the Location header pointing to
     * the new transaction's URL.
     *
     * @param transactionDTO the transaction data sent by the client
     * @param request        the {@link HttpServletRequest} containing the "uid"
     *                       attribute
     * @return a {@link ResponseEntity} containing the created
     *         {@link TransactionResponseDTO}
     *         and a Location header for the new resource
     */
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionDTO transactionDTO,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        TransactionResponseDTO createdTransaction = transactionService.createTransaction(transactionDTO, userUid);

        return ResponseEntity
                .created(URI.create("/transactions/" + createdTransaction.getId()))
                .body(createdTransaction);
    }

    /**
     * Updates an existing transaction for the currently authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}. The transaction with the
     * specified ID is updated if it exists and belongs to the user.
     *
     * @param id             the ID of the transaction to update
     * @param transactionDTO the updated transaction data
     * @param request        the {@link HttpServletRequest} containing the "uid"
     *                       attribute
     * @return a {@link ResponseEntity} containing the updated
     *         {@link TransactionResponseDTO}
     *         and HTTP 200 OK if successful, or 404 Not Found if the transaction
     *         does not exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO,
            HttpServletRequest request) {

        String userUid = (String) request.getAttribute("uid");
        TransactionResponseDTO updatedTransaction = transactionService.updateTransaction(id, transactionDTO, userUid);

        if (updatedTransaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTransaction);
    }

    /**
     * Deletes a transaction for the currently authenticated user.
     * <p>
     * The user's Firebase UID is obtained from the {@link HttpServletRequest}
     * attribute
     * "uid", set by {@link FirebaseAuthenticationFilter}. Only transactions
     * belonging
     * to this user can be deleted.
     *
     * @param id      the ID of the transaction to delete
     * @param request the {@link HttpServletRequest} containing the "uid" attribute
     * @return a {@link ResponseEntity} with HTTP 204 No Content if deleted, or 404
     *         Not Found
     *         if the transaction does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id, HttpServletRequest request) {
        String userUid = (String) request.getAttribute("uid");
        boolean deleted = transactionService.deleteTransaction(id, userUid);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
