package com.austin.financetracker.dto;

import com.austin.financetracker.entity.TransactionType;

/**
 * Data Transfer Object (DTO) for
 * {@link com.austin.financetracker.entity.Category}.
 * <p>
 * Used to transfer category data between layers, typically from the service
 * layer
 * to the controller or client. Contains basic category information without
 * including entity-specific persistence logic.
 * </p>
 */
public class CategoryDTO {

    /**
     * The unique identifier of the category.
     */
    private Long id;

    /**
     * The name of the category.
     */
    private String name;

    /**
     * A brief description of the category.
     */
    private String description;

    /**
     * The type of transaction this category belongs to.
     */
    private TransactionType type;

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
     * Default constructor.
     */
    public CategoryDTO() {
    }

    /**
     * Constructs a new {@code CategoryDTO} with the specified details.
     *
     * @param id          the unique identifier of the category
     * @param name        the name of the category
     * @param description a brief description of the category
     * @param type        the {@link TransactionType} of the category
     */
    public CategoryDTO(Long id, String name, String description, TransactionType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    /**
     * Returns the unique identifier of the category.
     *
     * @return the category ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the category.
     *
     * @param id the ID to assign
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the category.
     *
     * @return the category name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category.
     *
     * @param name the name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the category.
     *
     * @return the category description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the category.
     *
     * @param description the description to assign
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the transaction type of the category.
     *
     * @return the {@link TransactionType}
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Sets the transaction type of the category.
     *
     * @param type the {@link TransactionType} to assign
     */
    public void setType(TransactionType type) {
        this.type = type;
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