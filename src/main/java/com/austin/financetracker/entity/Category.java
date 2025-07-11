package com.austin.financetracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity     // Tells SB this class represents a database table
@Table(name = "categories")     // Table name
public class Category {
    
    @Id     // Marks the following filed as a primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Auto-increments the id
    private Long id;    // id field

    @Column(nullable = false, unique = true)    // Column where name is required and it must be unique
    private String name;
    private String description;

    public Category() {}    // Default constructor required by JPA

    public Category(String name, String description) {      // Constructor for creating new categories
        this.name = name;
        this.description = description;
    }

    // Getters and setters to modify field
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
}
