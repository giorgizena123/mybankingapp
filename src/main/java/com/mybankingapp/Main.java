package com.mybankingapp;

import com.mybankingapp.dao.TransactionDao;
import com.mybankingapp.dao.TransactionTypeDao;
import com.mybankingapp.dao.UserDao;
import com.mybankingapp.model.Transaction;
import com.mybankingapp.model.TransactionType;
import com.mybankingapp.model.User;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        TransactionTypeDao transactionTypeDao = new TransactionTypeDao();
        TransactionDao transactionDao = new TransactionDao();

        try {
            System.out.println("--- 1. მომხმარებლების შექმნა ---");

            User user1 = new User("გიორგი", "მეტონიძე", "giorgi_m", "pass123", "GE1234567890123456789012", new BigDecimal("1000.00"));
            user1 = userDao.create(user1);
            System.out.println("შექმნილი მომხმარებელი 1: " + user1);

            User user2 = new User("ანა", "ლომიძე", "ana_l", "pass456", "GE9876543210987654321098", new BigDecimal("500.00"));
            user2 = userDao.create(user2);
            System.out.println("შექმნილი მომხმარებელი 2: " + user2);

            User user3 = new User("ლაშა", "ბერიძე", "lasha_b", "pass789", "GE1122334455667788990011", new BigDecimal("200.00"));
            user3 = userDao.create(user3);
            System.out.println("შექმნილი მომხმარებელი 3: " + user3);


            System.out.println("\n--- 2. მომხმარებლის მოძებნა username-ით ---");
            User foundUserByUsername = userDao.findByUsername("giorgi_m");
            if (foundUserByUsername != null) {
                System.out.println("მომხმარებელი (username-ით): " + foundUserByUsername);
            } else {
                System.out.println("მომხმარებელი 'giorgi_m' ვერ მოიძებნა.");
            }

            System.out.println("\n--- 3. მომხმარებლის მოძებნა IBAN-ით ---");
            User foundUserByIBAN = userDao.findByIBAN("GE9876543210987654321098");
            if (foundUserByIBAN != null) {
                System.out.println("მომხმარებელი (IBAN-ით): " + foundUserByIBAN);
            } else {
                System.out.println("მომხმარებელი 'GE9876543210987654321098' ვერ მოიძებნა.");
            }

            System.out.println("\n--- 4. ყველა მომხმარებელი ---");
            List<User> allUsers = userDao.findAll();
            allUsers.forEach(System.out::println);


            System.out.println("\n--- 5. ტრანზაქციების შექმნა ---");
            TransactionType depositType = transactionTypeDao.findByTransactionTypeName("deposit");
            TransactionType withdrawType = transactionTypeDao.findByTransactionTypeName("withdrawal");
            TransactionType transferType = transactionTypeDao.findByTransactionTypeName("transfer");

            if (depositType == null || withdrawType == null || transferType == null) {
                System.err.println("Error: Transaction types not found in database. Please check SQL script.");
                return;
            }

            System.out.println("\n--- დეპოზიტი ---");

            Transaction transaction1 = new Transaction(foundUserByUsername, foundUserByUsername, new BigDecimal("100.00"), depositType);
            transaction1 = transactionDao.create(transaction1);
            System.out.println("შექმნილი ტრანზაქცია (დეპოზიტი): " + transaction1);
            foundUserByUsername.setMoney(foundUserByUsername.getMoney().add(transaction1.getAmount()));
            userDao.update(foundUserByUsername);
            System.out.println("გიორგის ბალანსი დეპოზიტის შემდეგ: " + userDao.findByUsername("giorgi_m").getMoney());


            System.out.println("\n--- გადარიცხვა ---");
            if (foundUserByUsername.getMoney().compareTo(new BigDecimal("150.00")) >= 0) {

                Transaction transaction2 = new Transaction(foundUserByUsername, foundUserByIBAN, new BigDecimal("150.00"), transferType);
                transaction2 = transactionDao.create(transaction2);
                System.out.println("შექმნილი ტრანზაქცია (გადარიცხვა): " + transaction2);
                foundUserByUsername.setMoney(foundUserByUsername.getMoney().subtract(transaction2.getAmount()));
                userDao.update(foundUserByUsername);

                foundUserByIBAN.setMoney(foundUserByIBAN.getMoney().add(transaction2.getAmount()));
                userDao.update(foundUserByIBAN);

                System.out.println("გიორგის ბალანსი გადარიცხვის შემდეგ: " + userDao.findByUsername("giorgi_m").getMoney());
                System.out.println("ანას ბალანსი გადარიცხვის შემდეგ: " + userDao.findByUsername("ana_l").getMoney());

            } else {
                System.out.println("გიორგის არ აქვს საკმარისი თანხა გადასარიცხად.");
            }

            System.out.println("\n--- განაღდება ---");
            if (foundUserByIBAN.getMoney().compareTo(new BigDecimal("50.00")) >= 0) {

                Transaction transaction3 = new Transaction(foundUserByIBAN, foundUserByIBAN, new BigDecimal("50.00"), withdrawType); // Line 98
                transaction3 = transactionDao.create(transaction3);
                System.out.println("შექმნილი ტრანზაქცია (განაღდება): " + transaction3);
                foundUserByIBAN.setMoney(foundUserByIBAN.getMoney().subtract(transaction3.getAmount()));
                userDao.update(foundUserByIBAN);
                System.out.println("ანას ბალანსი განაღდების შემდეგ: " + userDao.findByUsername("ana_l").getMoney());
            } else {
                System.out.println("ანას არ აქვს საკმარისი თანხა განაღდებისთვის.");
            }


            System.out.println("\n--- 6. ტრანზაქციები (გამგზავნის IBAN-ით) ---");

            List<Transaction> senderTransactions = transactionDao.getTransactionsByUser(foundUserByUsername.getUsername());
            System.out.println("გიორგის ტრანზაქციები:");
            senderTransactions.forEach(System.out::println);

            System.out.println("\n--- 7. ტრანზაქციები (მიმღების IBAN-ით) ---");
            List<Transaction> receiverTransactions = transactionDao.getTransactionsByUser(foundUserByIBAN.getUsername());
            System.out.println("ანას ტრანზაქციები:");
            receiverTransactions.forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}