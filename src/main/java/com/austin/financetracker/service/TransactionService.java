package com.austin.financetracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.austin.financetracker.dto.TransactionDTO;
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
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get transaction by id
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        return convertToDTO(transaction);
    }

    // Get transaction by category
    public List<TransactionDTO> getTransactionsByCategory(Long categoryId) {
         Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        
        return transactionRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Create new transaction
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + transactionDTO.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setDate(transactionDTO.getDate());
        transaction.setType(transactionDTO.getType());
        transaction.setCategory(category);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }

    // Update transaction
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {
        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + transactionDTO.getCategoryId()));

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        // Update fields
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setDate(transactionDTO.getDate());
        transaction.setType(transactionDTO.getType());
        transaction.setCategory(category);

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToDTO(updatedTransaction);
    }

    // Delete transaction
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }

    // Helper method to convert Transaction entity to TransactionDTO
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setDate(transaction.getDate());
        dto.setType(transaction.getType());

        if (transaction.getCategory() != null) {
            dto.setCategoryId(transaction.getCategory().getId());
        }

        return dto;
    }

    // Create default transactions for testing
    public void createDefaultTransactions() {
        
        TransactionDTO salary = new TransactionDTO();
        salary.setAmount(new BigDecimal("3000.00"));
        salary.setDescription("Monthly Salary");
        salary.setDate(LocalDate.now().minusDays(5));
        salary.setType(TransactionType.INCOME);
        salary.setCategoryId(1L);

        TransactionDTO groceries = new TransactionDTO();
        groceries.setAmount(new BigDecimal("150.75"));
        groceries.setDescription("Grocery shopping");
        groceries.setDate(LocalDate.now().minusDays(2));
        groceries.setType(TransactionType.EXPENSE);
        groceries.setCategoryId(3L);

        TransactionDTO gas = new TransactionDTO();
        gas.setAmount(new BigDecimal("45.20"));
        gas.setDescription("Gas station");
        gas.setDate(LocalDate.now().minusDays(3));
        gas.setType(TransactionType.EXPENSE);
        gas.setCategoryId(4L);

        TransactionDTO rent = new TransactionDTO();
        rent.setAmount(new BigDecimal("1200.00"));
        rent.setDescription("Monthly rent");
        rent.setDate(LocalDate.now().minusDays(15));
        rent.setType(TransactionType.EXPENSE);
        rent.setCategoryId(6L);

        TransactionDTO entertainment = new TransactionDTO();
        entertainment.setAmount(new BigDecimal("89.99"));
        entertainment.setDescription("Netflix subscription");
        entertainment.setDate(LocalDate.now().minusDays(7));
        entertainment.setType(TransactionType.EXPENSE);
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
