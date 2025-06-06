package com.mybankingapp.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private String fromUserUsername;
    private String toUserUsername;
    private BigDecimal amount;
    private String type;
    private LocalDateTime transactionDate;


    public Transaction(int id, String fromUserUsername, String toUserUsername, BigDecimal amount, String type, LocalDateTime transactionDate) {
        this.id = id;
        this.fromUserUsername = fromUserUsername;
        this.toUserUsername = toUserUsername;
        this.amount = amount;
        this.type = type;
        this.transactionDate = transactionDate;
    }


    public Transaction(User fromUser, User toUser, BigDecimal amount, TransactionType type) {

        this(0,
                fromUser != null ? fromUser.getUsername() : null,
                toUser.getUsername(),
                amount,
                type.getTypeName(),
                LocalDateTime.now());
    }


    public int getId() { return id; }
    public String getFromUserUsername() { return fromUserUsername; }
    public String getToUserUsername() { return toUserUsername; }
    public BigDecimal getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getTransactionDate() { return transactionDate; }


    public void setId(int id) { this.id = id; }
    public void setFromUserUsername(String fromUserUsername) { this.fromUserUsername = fromUserUsername; }
    public void setToUserUsername(String toUserUsername) { this.toUserUsername = toUserUsername; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setType(String type) { this.type = type; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", fromUserUsername='" + fromUserUsername + '\'' +
                ", toUserUsername='" + toUserUsername + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}