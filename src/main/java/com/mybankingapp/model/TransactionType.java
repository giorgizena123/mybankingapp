package com.mybankingapp.model;

public class TransactionType {
    private Long id; // <-- IMPORTANT: This MUST be Long, not int!
    private String name;

    // Constructor for creating a new transaction type
    public TransactionType(Long id, String name) { // <-- IMPORTANT: This parameter MUST be Long, not int!
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { // <-- This MUST return Long
        return id;
    }

    public void setId(Long id) { // <-- This MUST accept Long
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}