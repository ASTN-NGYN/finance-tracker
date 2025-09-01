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
import com.austin.financetracker.repository.CategoryRepository;
import com.austin.financetracker.repository.TransactionRepository;

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

    /**
     * Constructs a {@code TransactionService} with the specified repositories.
     *
     * @param transactionRepository the repository used for transaction persistence
     * @param categoryRepository    the repository used for category persistence
     */
    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all transactions as {@link TransactionResponseDTO}.
     * 
     * @return a list of all transactions
     */
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Computes the total income of all transactions.
     * 
     * @return total income, or zero if none
     */
    public BigDecimal getTotalIncome() {
        BigDecimal total = transactionRepository.getTotalAmountByType(TransactionType.INCOME);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Computes the total expenses of all transactions.
     * 
     * @return total expenses, or zero if none
     */
    public BigDecimal getTotalExpenses() {
        BigDecimal total = transactionRepository.getTotalAmountByType(TransactionType.EXPENSE);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Computes the total savings of all transactions.
     * 
     * @return total savings, or zero if none
     */
    public BigDecimal getTotalSavings() {
        BigDecimal total = transactionRepository.getTotalAmountByType(TransactionType.SAVING);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Retrieves a transaction by ID.
     * 
     * @param id the transaction ID
     * @return transaction as {@link TransactionResponseDTO}
     * @throws RuntimeException if transaction is not found
     */
    public TransactionResponseDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        return convertToResponseDTO(transaction);
    }

    /**
     * Retrieves transactions filtered by category ID.
     * 
     * @param categoryId the category ID
     * @return list of transactions for the category
     * @throws RuntimeException if category is not found
     */
    public List<TransactionResponseDTO> getTransactionsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        return transactionRepository.findByCategory(category).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all transactions with their associated categories.
     * 
     * @return list of {@link TransactionWithCategoryDTO}
     */
    public List<TransactionWithCategoryDTO> getAllTransactionsWithCategories() {
        return transactionRepository.findAll().stream()
                .map(TransactionWithCategoryDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transactions filtered by {@link TransactionType}.
     * 
     * @param type the transaction type
     * @return list of transactions for the type
     */
    public List<TransactionResponseDTO> getTransactionsByType(TransactionType type) {
        List<Transaction> transactions = transactionRepository.findByType(type);
        return transactions.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves transactions with categories filtered by type.
     * 
     * @param type the transaction type
     * @return list of {@link TransactionWithCategoryDTO} for the type
     */
    public List<TransactionWithCategoryDTO> getTransactionsWithCategoriesByType(TransactionType type) {
        return transactionRepository.findByType(type)
                .stream()
                .map(TransactionWithCategoryDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves transactions within a specified date range.
     * 
     * @param startDate start of date range (inclusive)
     * @param endDate   end of date range (inclusive)
     * @return list of transactions within the range
     * @throws RuntimeException if startDate is after endDate
     */
    public List<TransactionResponseDTO> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        List<Transaction> transactions = transactionRepository.findByDateBetween(startDate, endDate);
        return transactions.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    /**
     * Creates a new transaction from {@link TransactionDTO}.
     * 
     * @param transactionDTO the transaction data
     * @return created transaction as {@link TransactionResponseDTO}
     * @throws RuntimeException if category is not found
     */
    public TransactionResponseDTO createTransaction(TransactionDTO transactionDTO) {
        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(
                        () -> new RuntimeException("Category not found with id: " + transactionDTO.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setDate(transactionDTO.getDate());
        transaction.setType(category.getType());
        transaction.setCategory(category);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToResponseDTO(savedTransaction);
    }

    /**
     * Updates an existing transaction by ID using {@link TransactionDTO}.
     * 
     * @param id                    the transaction ID
     * @param updatedTransactionDTO the updated transaction data
     * @return updated transaction as {@link TransactionResponseDTO}
     * @throws RuntimeException if transaction or category is not found
     */
    public TransactionResponseDTO updateTransaction(Long id, TransactionDTO updatedTransactionDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

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
     * Deletes a transaction by its ID.
     * 
     * @param id the transaction ID
     * @throws RuntimeException if transaction is not found
     */
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }

    /**
     * Converts a {@link Transaction} entity to {@link TransactionResponseDTO}.
     * 
     * @param transaction the transaction entity
     * @return the response DTO
     */
    private TransactionResponseDTO convertToResponseDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getCategory() != null ? transaction.getCategory().getId() : null);
    }

    /**
     * Creates default transactions for testing purposes.
     * Includes salary, groceries, gas, rent, and entertainment transactions.
     * Any errors during creation are printed to the console.
     */
    public void createDefaultTransactions() {

        TransactionDTO salary = new TransactionDTO();
        salary.setAmount(new BigDecimal("3000.00"));
        salary.setDescription("Monthly Salary");
        salary.setDate(LocalDate.now().minusDays(5));
        salary.setCategoryId(1L);

        TransactionDTO groceries = new TransactionDTO();
        groceries.setAmount(new BigDecimal("150.75"));
        groceries.setDescription("Grocery shopping");
        groceries.setDate(LocalDate.now().minusDays(2));
        groceries.setCategoryId(3L);

        TransactionDTO gas = new TransactionDTO();
        gas.setAmount(new BigDecimal("45.20"));
        gas.setDescription("Gas station");
        gas.setDate(LocalDate.now().minusDays(3));
        gas.setCategoryId(4L);

        TransactionDTO rent = new TransactionDTO();
        rent.setAmount(new BigDecimal("1200.00"));
        rent.setDescription("Monthly rent");
        rent.setDate(LocalDate.now().minusDays(15));
        rent.setCategoryId(6L);

        TransactionDTO entertainment = new TransactionDTO();
        entertainment.setAmount(new BigDecimal("89.99"));
        entertainment.setDescription("Netflix subscription");
        entertainment.setDate(LocalDate.now().minusDays(7));
        entertainment.setCategoryId(5L);

        try {
            createTransaction(salary);
            createTransaction(groceries);
            createTransaction(gas);
            createTransaction(rent);
            createTransaction(entertainment);
            System.out.println("Default transactions created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating default transactions: " + e.getMessage());
        }
    }
}
