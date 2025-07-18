package com.austin.financetracker.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)        // Stores enum as text in database("INCOME" or "EXPENSE")
    @Column(nullable = false)
    private TransactionType type;

    // Many transactions can belong to one category
    @ManyToOne(fetch = FetchType.LAZY)                      // LAZY: Don't load category data unless specifically requested (only load what you need)
    @JoinColumn(name = "category_id", nullable = false)     // Creates foreign key "category_id" points to the id filed in categories table, nullable=false means every transaction must have a category
    private Category category;

    public Transaction() {

    }

    public Transaction(BigDecimal amount, String description, LocalDate date,
                        TransactionType type, Category category) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Transaction{" +
        "id=" + id + ", amount=" + amount +
        ", description='" + description + '\'' +
        ", date=" + date +
        ", type=" + type +
        ", category=" + category.getName() +
        '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;       // Checks if both references point to the same object in memory
        if (obj == null || getClass() != obj.getClass()) return false;  // check if the object you're comparing is null? And is the class of object different? getClass() ensures we're only comparing Transaction to another Transaction.
        Transaction that = (Transaction) obj;   // If pass first two checks, cast the obj to a Transaction; lets us access its fields (like that.id) for comparison
        return id != null && id.equals(that.id);    // The actual equality check, if id not null and this.id.equals(that.id) is true
    }
    /*  Why use equals() instead of ==
     * Because id is an object(Long, not long), so == compares memory addresses. equals() checks value equality, which is what we want.
     */

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;  // condition ? expression_if_true : expression_if_false;
        // if id is not null, return the hash code of the id. Otherwise, return 0.
    }

}
