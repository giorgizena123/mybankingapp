package com.mybankingapp.controller;

import com.mybankingapp.MainApp;
import com.mybankingapp.model.User;
import com.mybankingapp.model.Transaction;
import com.mybankingapp.dao.UserDao;
import com.mybankingapp.dao.TransactionDao;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class WithdrawController {

    @FXML
    private TextField amountField;
    @FXML
    private Label messageLabel;

    private MainApp mainApp;
    private User currentUser;
    private UserDao userDao;
    private TransactionDao transactionDao;

    @FXML
    public void initialize() {
        userDao = new UserDao();
        transactionDao = new TransactionDao();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleWithdrawButton(ActionEvent event) {
        BigDecimal withdrawAmount;

        if (amountField.getText().isEmpty()) {
            messageLabel.setText("Please enter an amount.");
            return;
        }

        try {
            withdrawAmount = new BigDecimal(amountField.getText());
            if (withdrawAmount.compareTo(BigDecimal.ZERO) <= 0) {
                messageLabel.setText("Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid amount. Please enter a number.");
            return;
        }

        if (currentUser == null) {
            messageLabel.setText("No active user found for withdrawal.");
            return;
        }


        if (currentUser.getMoney().compareTo(withdrawAmount) < 0) {
            messageLabel.setText("Insufficient funds.");
            return;
        }

        try {

            currentUser.setMoney(currentUser.getMoney().subtract(withdrawAmount));
            userDao.update(currentUser);

            Transaction newTransaction = new Transaction(
                    0,
                    currentUser.getUsername(),
                    currentUser.getUsername(),
                    withdrawAmount,
                    "WITHDRAW",
                    LocalDateTime.now()
            );
            transactionDao.create(newTransaction);

            messageLabel.setText("Withdrawal successful!");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Withdrawal successful! Your balance has been updated.");


            User updatedCurrentUser = userDao.findByUsername(currentUser.getUsername());
            if (mainApp != null && updatedCurrentUser != null) {
                mainApp.showHomePage(updatedCurrentUser);
            }

        } catch (SQLException e) {
            messageLabel.setText("Database error during withdrawal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        if (mainApp != null && currentUser != null) {
            mainApp.showHomePage(currentUser);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleSubmitButton(ActionEvent actionEvent) {
    }

    public void handleBackToHome(ActionEvent actionEvent) {

    }
}