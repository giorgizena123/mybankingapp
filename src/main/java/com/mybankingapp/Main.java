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
            System.out.println("--- 1. მომხმარებლების რეგისტრაცია ---");


            User user1 = new User("giorgi_m", "pass123", "გიორგი", "მეტონიძე");
            boolean registered1 = userDao.registerUser(user1);
            if (registered1) {

                System.out.println("შექმნილი მომხმარებელი 1: " + user1.getUsername() + " (IBAN: " + user1.getIban() + ", Money: " + user1.getMoney() + ")");
            } else {
                System.out.println("მომხმარებელი 'giorgi_m' რეგისტრაცია ვერ მოხერხდა (შესაძლოა უკვე არსებობს).");
            }

            User user2 = new User("ana_l", "pass456", "ანა", "ლომიძე");
            boolean registered2 = userDao.registerUser(user2);
            if (registered2) {
                System.out.println("შექმნილი მომხმარებელი 2: " + user2.getUsername() + " (IBAN: " + user2.getIban() + ", Money: " + user2.getMoney() + ")");
            } else {
                System.out.println("მომხმარებელი 'ana_l' რეგისტრაცია ვერ მოხერხდა (შესაძლოა უკვე არსებობს).");
            }

            User user3 = new User("lasha_b", "pass789", "ლაშა", "ბერიძე");
            boolean registered3 = userDao.registerUser(user3);
            if (registered3) {
                System.out.println("შექმნილი მომხმარებელი 3: " + user3.getUsername() + " (IBAN: " + user3.getIban() + ", Money: " + user3.getMoney() + ")");
            } else {
                System.out.println("მომხმარებელი 'lasha_b' რეგისტრაცია ვერ მოხერხდა (შესაძლოა უკვე არსებობს).");
            }


            User giorgi = userDao.findByUsername("giorgi_m");
            User ana = userDao.findByUsername("ana_l");
            User lasha = userDao.findByUsername("lasha_b");


            System.out.println("\n--- 2. მომხმარებლის მოძებნა username-ით ---");
            if (giorgi != null) {
                System.out.println("მომხმარებელი (username-ით): " + giorgi.getFirstName() + " " + giorgi.getLastName() + ", ბალანსი: " + giorgi.getMoney() + ", IBAN: " + giorgi.getIban());
            } else {
                System.out.println("მომხმარებელი 'giorgi_m' ვერ მოიძებნა.");
            }

            System.out.println("\n--- 3. მომხმარებლის მოძებნა IBAN-ით ---");
            if (ana != null && ana.getIban() != null) {
                User foundUserByIBAN = userDao.findByIBAN(ana.getIban());
                if (foundUserByIBAN != null) {
                    System.out.println("მომხმარებელი (IBAN-ით): " + foundUserByIBAN.getFirstName() + " " + foundUserByIBAN.getLastName() + ", ბალანსი: " + foundUserByIBAN.getMoney() + ", IBAN: " + foundUserByIBAN.getIban());
                } else {
                    System.out.println("მომხმარებელი '" + ana.getIban() + "' ვერ მოიძებნა.");
                }
            } else {
                System.out.println("ანა მომხმარებელი ვერ მოიძებნა ან IBAN არ აქვს, რეგისტრაცია შესაძლოა არ დასრულებულა.");
            }


            System.out.println("\n--- 4. ყველა მომხმარებელი ---");
            List<User> allUsers = userDao.findAllUsers();
            if (allUsers != null && !allUsers.isEmpty()) {
                allUsers.forEach(u -> System.out.println("ID: " + u.getId() + ", Username: " + u.getUsername() + ", Name: " + u.getFirstName() + " " + u.getLastName() + ", Money: " + u.getMoney() + ", IBAN: " + u.getIban()));
            } else {
                System.out.println("მომხმარებლები ვერ მოიძებნა.");
            }


            System.out.println("\n--- 5. ტრანზაქციების შექმნა ---");

            TransactionType depositType = transactionTypeDao.getTypeByName("deposit");
            TransactionType withdrawType = transactionTypeDao.getTypeByName("withdraw");
            TransactionType transferType = transactionTypeDao.getTypeByName("transfer");

            if (depositType == null || withdrawType == null || transferType == null) {
                System.err.println("Error: Transaction types not found in database. Please ensure your `types` table is populated correctly (e.g., via SQL script).");
                return;
            }


            if (giorgi == null || ana == null || lasha == null) {
                System.err.println("Error: One or more users could not be fetched from the database. Cannot perform transactions.");
                return;
            }

            System.out.println("\n--- დეპოზიტი (გიორგის) ---");

            Transaction depositTx = new Transaction(null, giorgi, new BigDecimal("100.00"), depositType);
            boolean depositSuccess = transactionDao.recordTransaction(depositTx);
            if (depositSuccess) {
                System.out.println("შექმნილი ტრანზაქცია (დეპოზიტი ID: " + depositTx.getId() + ", Amount: " + depositTx.getAmount() + ")");
                giorgi.setMoney(giorgi.getMoney().add(depositTx.getAmount()));
                userDao.update(giorgi);
                giorgi = userDao.findByUsername("giorgi_m");
                System.out.println("გიორგის ბალანსი დეპოზიტის შემდეგ: " + giorgi.getMoney());
            } else {
                System.out.println("დეპოზიტი ვერ მოხერხდა.");
            }

            System.out.println("\n--- გადარიცხვა (გიორგიდან ანას) ---");
            BigDecimal transferAmount = new BigDecimal("150.00");
            if (giorgi.getMoney().compareTo(transferAmount) >= 0) {
                Transaction transferTx = new Transaction(giorgi, ana, transferAmount, transferType);
                boolean transferSuccess = transactionDao.recordTransaction(transferTx);
                if (transferSuccess) {
                    System.out.println("შექმნილი ტრანზაქცია (გადარიცხვა ID: " + transferTx.getId() + ", Amount: " + transferTx.getAmount() + ")");
                    giorgi.setMoney(giorgi.getMoney().subtract(transferAmount));
                    userDao.update(giorgi);

                    ana.setMoney(ana.getMoney().add(transferAmount));
                    userDao.update(ana);
                    giorgi = userDao.findByUsername("giorgi_m");
                    ana = userDao.findByUsername("ana_l");
                    System.out.println("გიორგის ბალანსი გადარიცხვის შემდეგ: " + giorgi.getMoney());
                    System.out.println("ანას ბალანსი გადარიცხვის შემდეგ: " + ana.getMoney());
                } else {
                    System.out.println("გადარიცხვა ვერ მოხერხდა.");
                }
            } else {
                System.out.println("გიორგის არ აქვს საკმარისი თანხა გადასარიცხად (მიმდინარე ბალანსი: " + giorgi.getMoney() + ").");
            }

            System.out.println("\n--- განაღდება (ანას) ---");
            BigDecimal withdrawAmount = new BigDecimal("50.00");
            if (ana.getMoney().compareTo(withdrawAmount) >= 0) {

                Transaction withdrawTx = new Transaction(ana, null, withdrawAmount, withdrawType);
                boolean withdrawSuccess = transactionDao.recordTransaction(withdrawTx);
                if (withdrawSuccess) {
                    System.out.println("შექმნილი ტრანზაქცია (განაღდება ID: " + withdrawTx.getId() + ", Amount: " + withdrawTx.getAmount() + ")");
                    ana.setMoney(ana.getMoney().subtract(withdrawAmount));
                    userDao.update(ana);
                    ana = userDao.findByUsername("ana_l");
                    System.out.println("ანას ბალანსი განაღდების შემდეგ: " + ana.getMoney());
                } else {
                    System.out.println("განაღდება ვერ მოხერხდა.");
                }
            } else {
                System.out.println("ანას არ აქვს საკმარისი თანხა განაღდებისთვის (მიმდინარე ბალანსი: " + ana.getMoney() + ").");
            }


            System.out.println("\n--- 6. ტრანზაქციები მომხმარებლისთვის (გიორგის) ---");

            List<Transaction> giorgiTransactions = transactionDao.getTransactionsForUser(giorgi.getUsername());
            if (giorgiTransactions.isEmpty()) {
                System.out.println("გიორგისთვის ტრანზაქციები ვერ მოიძებნა.");
            } else {
                giorgiTransactions.forEach(tx -> {
                    String from = (tx.getFromUser() != null) ? tx.getFromUser().getUsername() : "N/A";
                    String to = (tx.getToUser() != null) ? tx.getToUser().getUsername() : "N/A";
                    System.out.println("ID: " + tx.getId() + ", From: " + from + ", To: " + to +
                            ", Amount: " + tx.getAmount() + ", Type: " + tx.getType().getName() +
                            ", Date: " + tx.getTransactionDate());
                });
            }

            System.out.println("\n--- 7. ტრანზაქციები მომხმარებლისთვის (ანას) ---");
            List<Transaction> anaTransactions = transactionDao.getTransactionsForUser(ana.getUsername());
            if (anaTransactions.isEmpty()) {
                System.out.println("ანასთვის ტრანზაქციები ვერ მოიძებნა.");
            } else {
                anaTransactions.forEach(tx -> {
                    String from = (tx.getFromUser() != null) ? tx.getFromUser().getUsername() : "N/A";
                    String to = (tx.getToUser() != null) ? tx.getToUser().getUsername() : "N/A";
                    System.out.println("ID: " + tx.getId() + ", From: " + from + ", To: " + to +
                            ", Amount: " + tx.getAmount() + ", Type: " + tx.getType().getName() +
                            ", Date: " + tx.getTransactionDate());
                });
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}