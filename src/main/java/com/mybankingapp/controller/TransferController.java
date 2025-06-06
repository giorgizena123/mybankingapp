package com.mybankingapp.controller;
import com.mybankingapp.MainApp;
import com.mybankingapp.model.User;
import com.mybankingapp.dao.TransactionDao;
import com.mybankingapp.dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.math.BigDecimal;

public class TransferController {

    @FXML
    private TextField toUserUsernameField;
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
    private void handleTransferButton(ActionEvent event) {
        String toUsername = toUserUsernameField.getText();
        BigDecimal amountBigDecimal;


        if (toUsername.isEmpty() || amountField.getText().isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }


        try {
            amountBigDecimal = new BigDecimal(amountField.getText());

            if (amountBigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
                messageLabel.setText("Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid amount. Please enter a number.");
            return;
        }


        if (currentUser == null) {
            messageLabel.setText("No active user found for transfer.");
            return;
        }


        if (currentUser.getMoney().compareTo(amountBigDecimal) < 0) {
            messageLabel.setText("Insufficient funds.");
            return;
        }

        try {

            User receiver = userDao.findByUsername(toUsername);
            if (receiver == null) {
                messageLabel.setText("Recipient username not found.");
                return;
            }


            if (currentUser.getUsername().equals(receiver.getUsername())) {
                messageLabel.setText("Cannot transfer to yourself. Use deposit/withdraw.");
                return;
            }


            currentUser.setMoney(currentUser.getMoney().subtract(amountBigDecimal));
            userDao.update(currentUser);


            receiver.setMoney(receiver.getMoney().add(amountBigDecimal));
            userDao.update(receiver);



            messageLabel.setText("Transfer successful!");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Transfer successful!");


            User updatedCurrentUser = userDao.findByUsername(currentUser.getUsername());
            if (mainApp != null && updatedCurrentUser != null) {
                mainApp.showHomePage(updatedCurrentUser);
            }

        } catch (SQLException e) {
            messageLabel.setText("Database error during transfer: " + e.getMessage());
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

    public void handleBackToHome(ActionEvent actionEvent) {

    }

    public void handleSubmitButton(ActionEvent actionEvent) {
    }
}