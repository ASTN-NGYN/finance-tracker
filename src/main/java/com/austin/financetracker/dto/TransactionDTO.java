package com.austin.financetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.austin.financetracker.entity.TransactionType;

public class TransactionDTO {

    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private TransactionType type;
    private Long categoryId;

    public TransactionDTO() {
    }

    // Getters and setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
