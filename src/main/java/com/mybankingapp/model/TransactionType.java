package com.mybankingapp.model;

public class TransactionType {
    private int id;
    private String typeName;

    public TransactionType(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }


    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "TransactionType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}