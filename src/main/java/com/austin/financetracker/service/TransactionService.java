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

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    
    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    // Get all transactions
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalIncome() {
        BigDecimal total = transactionRepository.getTotalAmountByType(TransactionType.INCOME);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpenses() {
        BigDecimal total = transactionRepository.getTotalAmountByType(TransactionType.EXPENSE);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalSavings() {
        BigDecimal total = transactionRepository.getTotalAmountByType(TransactionType.SAVING);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Get transaction by id
    public TransactionResponseDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        return convertToResponseDTO(transaction);
    }

    // Get transaction by category
    public List<TransactionResponseDTO> getTransactionsByCategory(Long categoryId) {
         Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        
        return transactionRepository.findByCategory(category).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionWithCategoryDTO> getAllTransactionsWithCategories() {
    return transactionRepository.findAll().stream()
            .map(TransactionWithCategoryDTO::new)
            .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getTransactionsByType(TransactionType type) {
        List<Transaction> transactions = transactionRepository.findByType(type);
        return transactions.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    public List<TransactionWithCategoryDTO> getTransactionsWithCategoriesByType(TransactionType type) {
    return transactionRepository.findByType(type)
            .stream()
            .map(TransactionWithCategoryDTO::new)
            .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
        throw new RuntimeException("Start date cannot be after end date");
        }
        List<Transaction> transactions = transactionRepository.findByDateBetween(startDate, endDate);
        return transactions.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    // Create new transaction
    public TransactionResponseDTO createTransaction(TransactionDTO transactionDTO) {
        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + transactionDTO.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setDate(transactionDTO.getDate());
        transaction.setType(category.getType());
        transaction.setCategory(category);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToResponseDTO(savedTransaction);
    }

    // Update transaction
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
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + updatedTransactionDTO.getCategoryId()));

            transaction.setCategory(category);
            transaction.setType(category.getType());
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToResponseDTO(updatedTransaction);
    }

    // Delete transaction
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }

    // Helper method to convert Transaction entity to TransactionDTO
    private TransactionResponseDTO convertToResponseDTO(Transaction transaction) {
        return new TransactionResponseDTO(
            transaction.getAmount(),
            transaction.getDescription(),
            transaction.getDate(),
            transaction.getType(),  // type is now part of the response DTO
            transaction.getCategory() != null ? transaction.getCategory().getId() : null
        );
    }


    // Create default transactions for testing
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
            System.out.println("Error creating default transactions" + e.getMessage());
        }
    }
}
