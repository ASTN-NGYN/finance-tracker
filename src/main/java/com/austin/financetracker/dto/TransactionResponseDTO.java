package com.austin.financetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.austin.financetracker.entity.TransactionType;

/**
 * Data Transfer Object (DTO) for sending transaction data from the service
 * layer
 * to the client or controller.
 * <p>
 * Includes all relevant transaction information, including amount, description,
 * date, type, and associated category ID.
 * </p>
 */
public class TransactionResponseDTO {

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
     * The type of transaction (e.g., INCOME, EXPENSE, SAVING).
     */
    private TransactionType type;

    /**
     * The ID of the category this transaction belongs to.
     */
    private Long categoryId;

    /**
     * The UID of the user who owns this entity.
     * <p>
     * This value corresponds to the UID from the {@link User} entity,
     * typically provided by Firebase authentication. It is used to
     * associate transactions or categories with a specific user.
     * </p>
     */
    private String userUid;

    /**
     * Constructs a new {@code TransactionResponseDTO} with the specified details.
     *
     * @param amount      the transaction amount
     * @param description the transaction description
     * @param date        the transaction date
     * @param type        the transaction type
     * @param categoryId  the ID of the category associated with this transaction
     */
    public TransactionResponseDTO(BigDecimal amount, String description, LocalDate date, TransactionType type,
            Long categoryId) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.categoryId = categoryId;
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
     * Returns the type of the transaction.
     *
     * @return the {@link TransactionType}
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Sets the type of the transaction.
     *
     * @param type the {@link TransactionType} to assign
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

    /**
     * Returns the ID of the category this transaction belongs to.
     *
     * @return the category ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the ID of the category this transaction belongs to.
     *
     * @param categoryId the category ID to assign
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Returns the UID of the user who owns this entity.
     *
     * @return the user UID
     */
    public String getUserUid() {
        return userUid;
    }

    /**
     * Sets the UID of the user who owns this entity.
     *
     * @param userUid the user UID to set
     */
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}