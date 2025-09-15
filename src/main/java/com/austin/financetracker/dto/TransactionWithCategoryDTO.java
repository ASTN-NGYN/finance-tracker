package com.austin.financetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.austin.financetracker.entity.Transaction;
import com.austin.financetracker.entity.TransactionType;

/**
 * Data Transfer Object (DTO) for transferring transaction data along with
 * the name of its associated category.
 * <p>
 * Used when returning transaction information to clients that requires
 * both transaction details and the category name.
 * </p>
 */
public class TransactionWithCategoryDTO {

    /**
     * The unique identifier of the transaction.
     */
    private Long id;

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
     * The name of the category this transaction belongs to.
     */
    private String categoryName;

    /**
     * Default constructor.
     */
    public TransactionWithCategoryDTO() {
    }

    /**
     * Constructs a {@code TransactionWithCategoryDTO} from a {@link Transaction}
     * entity.
     *
     * @param transaction the transaction entity to extract data from
     */
    public TransactionWithCategoryDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.categoryName = transaction.getCategory().getName();
    }

    /**
     * Returns the unique identifier of the transaction.
     *
     * @return the transaction ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the transaction.
     *
     * @param id the ID to assign
     */
    public void setId(Long id) {
        this.id = id;
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
     * Returns the name of the category this transaction belongs to.
     *
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the name of the category this transaction belongs to.
     *
     * @param categoryName the category name to assign
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}