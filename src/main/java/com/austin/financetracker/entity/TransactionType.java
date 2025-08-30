package com.austin.financetracker.entity;

/**
 * Represents the type of a financial transaction in the system.
 * <p>
 * Transactions can be categorized as:
 * <ul>
 *   <li>{@link #INCOME} – Money earned or received (e.g., salary, gift, refund).</li>
 *   <li>{@link #EXPENSE} – Money spent (e.g., groceries, rent, utilities).</li>
 *   <li>{@link #SAVING} – Money set aside for savings or investments.</li>
 * </ul>
 * </p>
 *
 * This enum is typically used by the {@code Transaction} entity
 * to classify and process transactions correctly.
 */
public enum TransactionType {
    INCOME,
    EXPENSE,
    SAVING
}
