package com.austin.financetracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.austin.financetracker.dto.TransactionDTO;
import com.austin.financetracker.dto.TransactionResponseDTO;
import com.austin.financetracker.dto.TransactionWithCategoryDTO;
import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.Transaction;
import com.austin.financetracker.entity.TransactionType;
import com.austin.financetracker.entity.User;
import com.austin.financetracker.repository.CategoryRepository;
import com.austin.financetracker.repository.TransactionRepository;
import com.austin.financetracker.repository.UserRepository;

/**
 * Service class for managing {@link Transaction} entities.
 * Provides methods for creating, updating, deleting, retrieving, and
 * filtering transactions. Also calculates totals and handles default
 * transactions for testing.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a {@code TransactionService} with the specified repositories.
     *
     * @param transactionRepository the repository used for transaction persistence
     * @param categoryRepository    the repository used for category persistence
     */
    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all transactions for a given user as
     * {@link TransactionResponseDTO}.
     *
     * @param userUid the unique identifier of the user
     * @return a list of transactions belonging to the specified user
     */
    public List<TransactionResponseDTO> getAllTransactions(String userUid) {
        return transactionRepository.findByUser_UserUid(userUid).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Computes the total amount of transactions for a given type and user.
     *
     * @param type    the {@link TransactionType} (e.g., INCOME, EXPENSE, SAVING)
     * @param userUid the UID of the user whose transactions should be summed
     * @return the total amount, or {@link BigDecimal#ZERO} if none
     */
    public BigDecimal getTotalByType(TransactionType type, String userUid) {
        BigDecimal total = transactionRepository.getTotalAmountByTypeAndUser(type, userUid);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Retrieves a transaction by its unique ID for a specific user.
     * <p>
     * Ensures that the transaction exists in the database. If no transaction with
     * the given ID is found, a {@link RuntimeException} is thrown.
     * </p>
     *
     * @param id      the ID of the transaction to retrieve
     * @param userUid the UID of the user who owns the transaction
     * @return a {@link TransactionResponseDTO} representing the transaction
     * @throws RuntimeException if the transaction with the specified ID does not
     *                          exist
     */
    public TransactionResponseDTO getTransactionById(Long id, String userUid) {
        Transaction transaction = transactionRepository.findByIdAndUser_UserUid(id, userUid)
                .orElseThrow(
                        () -> new RuntimeException("Transaction not found with id: " + id + "for user: " + userUid));
        return convertToResponseDTO(transaction);
    }

    /**
     * Retrieves all transactions belonging to a specific category for a specific
     * user.
     *
     * @param categoryId the ID of the category
     * @param userUid    the UID of the user
     * @return a list of {@link TransactionResponseDTO} for the given category and
     *         user
     * @throws RuntimeException if the category does not exist for the user
     */
    public List<TransactionResponseDTO> getTransactionsByCategory(Long categoryId, String userUid) {
        Category category = categoryRepository.findByIdAndUser_Uid(categoryId, userUid)
                .orElseThrow(() -> new RuntimeException(
                        "Category not found with id: " + categoryId + "for user: " + userUid));

        return transactionRepository.findByCategoryAndUser_UserUid(category, userUid).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all transactions for a specific user, including their associated
     * categories.
     *
     * @param userUid the UID of the user whose transactions are being retrieved
     * @return a list of {@link TransactionWithCategoryDTO} belonging to the user
     */
    public List<TransactionWithCategoryDTO> getAllTransactionsWithCategories(String userUid) {
        return transactionRepository.findByUser_UserUid(userUid).stream()
                .map(TransactionWithCategoryDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transactions for a specific user, filtered by
     * {@link TransactionType}.
     *
     * @param type    the transaction type (e.g., INCOME, EXPENSE, SAVING)
     * @param userUid the UID of the user whose transactions are being retrieved
     * @return a list of {@link TransactionResponseDTO} objects for the specified
     *         type and user
     */
    public List<TransactionResponseDTO> getTransactionsByType(TransactionType type, String userUid) {
        List<Transaction> transactions = transactionRepository.findByTypeAndUser_UserUid(type, userUid);
        return transactions.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves transactions with their associated categories for a specific user,
     * filtered by {@link TransactionType}.
     *
     * @param type    the transaction type (e.g., INCOME, EXPENSE, SAVING)
     * @param userUid the UID of the user whose transactions are being retrieved
     * @return a list of {@link TransactionWithCategoryDTO} objects for the
     *         specified type and user
     */
    public List<TransactionWithCategoryDTO> getTransactionsWithCategoriesByType(TransactionType type, String userUid) {
        return transactionRepository.findByTypeAndUser_UserUid(type, userUid)
                .stream()
                .map(TransactionWithCategoryDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transactions for a specific user within a specified date range.
     *
     * @param startDate start of date range (inclusive)
     * @param endDate   end of date range (inclusive)
     * @param userUid   the UID of the user whose transactions are being retrieved
     * @return list of {@link TransactionResponseDTO} within the date range for the
     *         user
     * @throws RuntimeException if {@code startDate} is after {@code endDate}
     */
    public List<TransactionResponseDTO> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate,
            String userUid) {
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        List<Transaction> transactions = transactionRepository.findByDateBetweenAndUser_UserUid(startDate, endDate,
                userUid);
        return transactions.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    /**
     * Creates a new transaction for a specific user based on the provided
     * {@link TransactionDTO}.
     * <p>
     * The transaction is associated with the category specified in
     * {@code transactionDTO}
     * and the user identified by {@code userUid}. If the category or user does not
     * exist,
     * a {@link RuntimeException} is thrown.
     * </p>
     *
     * @param transactionDTO the data transfer object containing transaction details
     * @param userUid        the UID of the user who owns this transaction
     * @return the created transaction as a {@link TransactionResponseDTO}
     * @throws RuntimeException if the specified category or user is not found
     */
    public TransactionResponseDTO createTransaction(TransactionDTO transactionDTO, String userUid) {
        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(
                        () -> new RuntimeException("Category not found with id: " + transactionDTO.getCategoryId()));

        User user = userRepository.findByUserUid(userUid)
                .orElseThrow(() -> new RuntimeException("User not found with uid: " + userUid));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setDate(transactionDTO.getDate());
        transaction.setType(category.getType());
        transaction.setCategory(category);
        transaction.setUser(user);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToResponseDTO(savedTransaction);
    }

    /**
     * Updates an existing transaction for a specific user.
     * <p>
     * Only the transaction belonging to the user identified by {@code userUid} will
     * be updated.
     * Fields in {@link TransactionDTO} that are {@code null} are ignored.
     * </p>
     *
     * @param id                    the ID of the transaction to update
     * @param updatedTransactionDTO the transaction data with updated values
     * @param userUid               the UID of the user who owns the transaction
     * @return the updated {@link TransactionResponseDTO}
     * @throws RuntimeException if the transaction or the category does not exist
     *                          for the user
     */
    public TransactionResponseDTO updateTransaction(Long id, TransactionDTO updatedTransactionDTO, String userUid) {
        Transaction transaction = transactionRepository.findByIdAndUser_UserUid(id, userUid)
                .orElseThrow(
                        () -> new RuntimeException("Transaction not found with id: " + id + " for user: " + userUid));

        if (updatedTransactionDTO.getAmount() != null) {
            transaction.setAmount(updatedTransactionDTO.getAmount());
        }
        if (updatedTransactionDTO.getDescription() != null) {
            transaction.setDescription(updatedTransactionDTO.getDescription());
        }
        if (updatedTransactionDTO.getDate() != null) {
            transaction.setDate(updatedTransactionDTO.getDate());
        }

        if (updatedTransactionDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(updatedTransactionDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException(
                            "Category not found with id: " + updatedTransactionDTO.getCategoryId()));

            transaction.setCategory(category);
            transaction.setType(category.getType());
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToResponseDTO(updatedTransaction);
    }

    /**
     * Deletes a transaction by its ID for a specific user.
     * <p>
     * Only the transaction owned by the user identified by {@code userUid} will be
     * deleted. If no such transaction exists, a {@link RuntimeException} is thrown.
     * </p>
     *
     * @param id      the transaction ID
     * @param userUid the UID of the user who owns the transaction
     * @return {@code true} if the transaction was successfully deleted
     * @throws RuntimeException if the transaction is not found for the user
     */
    public boolean deleteTransaction(Long id, String userUid) {
        Transaction transaction = transactionRepository.findByIdAndUser_UserUid(id, userUid)
                .orElseThrow(() -> new RuntimeException(
                        "Transaction not found with id: " + id + " for user: " + userUid));

        transactionRepository.delete(transaction);
        return true;
    }

    /**
     * Converts a {@link Transaction} entity to {@link TransactionResponseDTO}.
     *
     * @param transaction the transaction entity
     * @return the response DTO
     */
    private TransactionResponseDTO convertToResponseDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getCategory() != null ? transaction.getCategory().getId() : null);
    }
}
