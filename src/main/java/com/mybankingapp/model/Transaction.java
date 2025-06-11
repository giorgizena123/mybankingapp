package com.mybankingapp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private Long id;
    private User fromUser;
    private User toUser;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private TransactionType type;


    public Transaction(User fromUser, User toUser, BigDecimal amount, TransactionType type) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.type = type;
        this.transactionDate = LocalDateTime.now();
    }


    public Transaction(Long id, User fromUser, User toUser, BigDecimal amount, LocalDateTime transactionDate, TransactionType type) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.type = type;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }


    public String getFromUserUsername() {
        return fromUser != null ? fromUser.getUsername() : "N/A";
    }

    public String getToUserUsername() {
        return toUser != null ? toUser.getUsername() : "N/A";
    }

    public String getTypeName() {
        return type != null ? type.getName() : "N/A";
    }


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