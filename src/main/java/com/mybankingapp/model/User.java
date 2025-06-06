package com.mybankingapp.model;

import java.math.BigDecimal;

public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String iban;
    private BigDecimal money;


    public User(long id, String firstName, String lastName, String username, String password, String iban, BigDecimal money) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.iban = iban;
        this.money = money;
    }


    public User(String firstName, String lastName, String username, String password, String iban, BigDecimal money) {

        this(0, firstName, lastName, username, password, iban, money);
    }


    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIban() {
        return iban;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", iban='" + iban + '\'' +
                ", money=" + money +
                '}';
    }
}
