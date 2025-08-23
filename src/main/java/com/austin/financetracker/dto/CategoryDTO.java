package com.austin.financetracker.dto;

import com.austin.financetracker.entity.TransactionType;

public class CategoryDTO {

    private Long id;
    private String name;
    private String description;
    private TransactionType type;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name, String description, TransactionType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    // Getters and Setters()
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
