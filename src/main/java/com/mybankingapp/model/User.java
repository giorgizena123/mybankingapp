package com.mybankingapp.model;

import java.math.BigDecimal; // <-- დაამატეთ ეს import

public class User {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String iban;
    private BigDecimal money; // <-- შეიცვალა double-დან BigDecimal-ზე

    // კონსტრუქტორი რეგისტრაციისთვის
    public User(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.iban = null;
        this.money = BigDecimal.ZERO; // <-- შეიცვალა
    }

    // კონსტრუქტორი მონაცემთა ბაზიდან მომხმარებლის ჩამოსატვირთად
    public User(Long id, String username, String password, String firstName, String lastName, String iban, BigDecimal money) { // <-- შეიცვალა double-დან BigDecimal-ზე
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.iban = iban;
        this.money = money;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public BigDecimal getMoney() { // <-- შეიცვალა double-დან BigDecimal-ზე
        return money;
    }

    public void setMoney(BigDecimal money) { // <-- შეიცვალა double-დან BigDecimal-ზე
        this.money = money;
    }
}