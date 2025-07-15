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


}
