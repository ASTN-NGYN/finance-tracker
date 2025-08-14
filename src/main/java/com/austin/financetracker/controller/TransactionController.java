package com.austin.financetracker.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
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
import com.austin.financetracker.entity.TransactionType;
import com.austin.financetracker.service.TransactionService;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // GET /transactions (gets all) or GET /transactions?type=EXPENSE (gets by type)
    @GetMapping
    public List<TransactionDTO> getAllTransactions(@RequestParam(required = false) TransactionType type) {
        if (type != null) {
            return transactionService.getTransactionsByType(type);
        }
        return transactionService.getAllTransactions();
    }

    // GET /transactions/total-income
    @GetMapping("/total-income")
    public BigDecimal getTotalIncome() {
        return transactionService.getTotalIncome();
    }

    // GET /transactions/{id}
    @GetMapping("/{id}")
    public TransactionDTO getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    // GET /transactions/category?categoryId={id} (gets by category)
    @GetMapping("/category")
    public List<TransactionDTO> getTransactionsByCategory(@RequestParam Long categoryId) {
        return transactionService.getTransactionsByCategory(categoryId);
    }

    // GET /transactions/range?startDate=2024-01-01&endDate=2024-01-31
    @GetMapping("/range")
    public List<TransactionDTO> getTransactionsByDateRange(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate) {
            return transactionService.getTransactionsByDateRange(startDate, endDate);
        }

    // POST /transactions
    @PostMapping
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    // POST /transactions/default-transactions
    @PostMapping("/default-transactions")
    public ResponseEntity<String> createDefaultTransaction() {
        transactionService.createDefaultTransactions();
        return ResponseEntity.ok("Default transactions created successfully");
    }

    // PUT /transactions/{id}
    @PutMapping("/{id}")
    public TransactionDTO updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        return transactionService.updateTransaction(id, transactionDTO);
    }

    // DELETE /transactions/{id}
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }
}