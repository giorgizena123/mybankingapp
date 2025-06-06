package com.mybankingapp;

import com.mybankingapp.dao.TransactionDao;
import com.mybankingapp.dao.TransactionTypeDao;
import com.mybankingapp.dao.UserDao;
import com.mybankingapp.model.Transaction;
import com.mybankingapp.model.TransactionType;
import com.mybankingapp.model.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        TransactionTypeDao transactionTypeDao = new TransactionTypeDao();
        TransactionDao transactionDao = new TransactionDao();

        try {

            System.out.println("--- მომხმარებლების შექმნა ---");
            User user1 = new User(0, "გიორგი", "გიორგაძე", "giorgi_g", "pass123", "GE12TB1234567890123456", 1000.0);
            User user2 = new User(0, "მარიამი", "მარიამიძე", "mariam_m", "pass456", "GE98BB0987654321098765", 500.0);

            user1 = userDao.create(user1);
            user2 = userDao.create(user2);
            System.out.println("შექმნილი მომხმარებელი 1: " + user1);
            System.out.println("შექმნილი მომხმარებელი 2: " + user2);


            System.out.println("\n--- მომხმარებლის მოძებნა username-ით ---");
            User foundUserByUsername = userDao.findByUsername("giorgi_g");
            System.out.println("მოძებნილი მომხმარებელი (username): " + foundUserByUsername);


            System.out.println("\n--- მომხმარებლის მოძებნა IBAN-ით ---");
            User foundUserByIBAN = userDao.findByIBAN("GE98BB0987654321098765");
            System.out.println("მოძებნილი მომხმარებელი (IBAN): " + foundUserByIBAN);


            System.out.println("\n--- მომხმარებლის განახლება ---");
            user1.setMoney(1200.0);
            userDao.update(user1);
            User updatedUser1 = userDao.findByUsername("giorgi_g");
            System.out.println("განახლებული მომხმარებელი 1: " + updatedUser1);


            System.out.println("\n--- ყველა მომხმარებელი ---");
            List<User> allUsers = userDao.findAll();
            allUsers.forEach(System.out::println);


            System.out.println("\n--- ტრანზაქციის ტიპები ---");
            TransactionType transferType = transactionTypeDao.findByTransactionTypeName("TRANSFER");
            TransactionType depositType = transactionTypeDao.findByTransactionTypeName("DEPOSIT");
            System.out.println("გადარიცხვის ტიპი: " + transferType);
            System.out.println("დეპოზიტის ტიპი: " + depositType);


            System.out.println("\n--- ტრანზაქციების შექმნა ---");
            if (foundUserByUsername != null && foundUserByIBAN != null && transferType != null) {
                Transaction transaction1 = new Transaction(0, foundUserByUsername, foundUserByIBAN, 100.0, new Timestamp(System.currentTimeMillis()), transferType);
                transaction1 = transactionDao.create(transaction1);
                System.out.println("შექმნილი ტრანზაქცია 1: " + transaction1);


                Transaction transaction2 = new Transaction(0, user2, user2, 50.0, new Timestamp(System.currentTimeMillis()), depositType);
                transaction2 = transactionDao.create(transaction2);
                System.out.println("შექმნილი ტრანზაქცია 2 (დეპოზიტი): " + transaction2);

            } else {
                System.out.println("ვერ მოხერხდა ტრანზაქციის შექმნა, მომხმარებელი ან ტრანზაქციის ტიპი ვერ მოიძებნა.");
            }


            System.out.println("\n--- ტრანზაქციები გამგზავნის IBAN-ით (" + user1.getIban() + ") ---");
            List<Transaction> senderTransactions = transactionDao.findBySenderIBAN(user1.getIban());
            senderTransactions.forEach(System.out::println);


            System.out.println("\n--- ტრანზაქციები მიმღების IBAN-ით (" + user2.getIban() + ") ---");
            List<Transaction> receiverTransactions = transactionDao.findByReceiverIBAN(user2.getIban());
            receiverTransactions.forEach(System.out::println);


        } catch (SQLException e) {
            System.err.println("მონაცემთა ბაზასთან შეცდომა მოხდა: " + e.getMessage());
            e.printStackTrace();
        }
    }
}