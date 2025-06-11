package com.mybankingapp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Import LocalDateTime

public class Transaction {
    private Long id;
    private User fromUser; // User object representing the sender
    private User toUser;   // User object representing the recipient
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private TransactionType type;

    // Constructor for creating a new transaction (e.g., in controllers before saving to DB)
    public Transaction(User fromUser, User toUser, BigDecimal amount, TransactionType type) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.type = type;
        this.transactionDate = LocalDateTime.now(); // Set current time by default
    }

    // Constructor for retrieving a transaction from the database
    public Transaction(Long id, User fromUser, User toUser, BigDecimal amount, LocalDateTime transactionDate, TransactionType type) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.type = type;
    }

    // Getters and Setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFromUser() { // This is the missing method!
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() { // This is also needed
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public BigDecimal getAmount() { // This is also needed
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() { // This is also needed
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getType() { // This is also needed
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    // Helper methods for TableView (assuming usernames are available)
    public String getFromUserUsername() {
        return fromUser != null ? fromUser.getUsername() : "N/A";
    }

    public String getToUserUsername() {
        return toUser != null ? toUser.getUsername() : "N/A";
    }

    public String getTypeName() {
        return type != null ? type.getName() : "N/A";
    }

    // Override toString for easy debugging in Main.java
    @Override
    public String toString() {
        String fromUsername = (fromUser != null) ? fromUser.getUsername() : "N/A";
        String toUsername = (toUser != null) ? toUser.getUsername() : "N/A";
        String typeName = (type != null) ? type.getName() : "N/A";
        return "Transaction{" +
                "id=" + id +
                ", fromUser='" + fromUsername + '\'' +
                ", toUser='" + toUsername + '\'' +
                ", amount=" + amount +
                ", date=" + transactionDate +
                ", type='" + typeName + '\'' +
                '}';
    }
}