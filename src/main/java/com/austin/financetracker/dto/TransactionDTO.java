package com.austin.financetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for transferring transaction data
 * between layers, such as from the client to the service layer.
 * <p>
 * Contains only the necessary information to create or update a {@code Transaction},
 * without including entity-specific persistence logic.
 * </p>
 */
public class TransactionDTO {

    /**
     * The monetary amount of the transaction.
     */
    private BigDecimal amount;

    /**
     * A brief description of the transaction.
     */
    private String description;

    /**
     * The date the transaction occurred.
     */
    private LocalDate date;

    /**
     * The ID of the category this transaction belongs to.
     */
    private Long categoryId;

    /**
     * Default constructor.
     */
    public TransactionDTO() {
    }

    /**
     * Returns the monetary amount of the transaction.
     *
     * @return the transaction amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the monetary amount of the transaction.
     *
     * @param amount the amount to assign
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Returns the description of the transaction.
     *
     * @return the transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the transaction.
     *
     * @param description the description to assign
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the date the transaction occurred.
     *
     * @return the transaction date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the transaction.
     *
     * @param date the date to assign
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the category ID this transaction belongs to.
     *
     * @return the category ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category ID this transaction belongs to.
     *
     * @param categoryId the category ID to assign
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}