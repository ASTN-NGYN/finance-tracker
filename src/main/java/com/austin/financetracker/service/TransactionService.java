package com.austin.financetracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.austin.financetracker.entity.Category;
import com.austin.financetracker.entity.Transaction;
import com.austin.financetracker.repository.TransactionRepository;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Get transaction by type
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    // Get transaction by category
    public List<Transaction> getTransactionsByCategory(Category category) {
        return transactionRepository.findByCategory(category);
    }

    // Create new transaction
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // Update transaction
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        return transactionRepository.findById(id)
            .map(transaction -> {
                // update fields
                transaction.setAmount(updatedTransaction.getAmount());
                transaction.setDescription(updatedTransaction.getDescription());
                transaction.setDate(updatedTransaction.getDate());
                transaction.setType(updatedTransaction.getType());
                transaction.setCategory(updatedTransaction.getCategory());
                return transactionRepository.save(transaction);
            })
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    // Delete transaction
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }
}
