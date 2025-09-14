package com.austin.financetracker.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a user in the Finance Tracker system.
 * <p>
 * This entity maps to the "users" table in the database. Each user is uniquely
 * identified by their Firebase UID. Additional user information such as email
 * is also stored.
 * </p>
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * The unique identifier of the user, provided by Firebase.
     * Serves as the primary key in the "users" table.
     */
    @Id
    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    /**
     * The email address of the user.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String email;

    /**
     * The list of categories associated with this user.
     * <p>
     * This list is managed by JPA and uses cascading remove and orphan removal.
     * When the user is deleted, all associated categories are also deleted.
     * </p>
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Category> categories = new ArrayList<>();

    /**
     * The list of transactions associated with this user.
     * <p>
     * This list is managed by JPA and uses cascading remove and orphan removal.
     * When the user is deleted, all associated transactions are also deleted.
     * </p>
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Transaction> transactions = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public User() {
    }

    /**
     * Returns the Firebase UID of the user.
     *
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets the Firebase UID of the user.
     *
     * @param uid the unique identifier to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the list of categories associated with this user.
     *
     * @return list of {@link Category} entities
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Returns the list of transactions associated with this user.
     *
     * @return list of {@link Transaction} entities
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
