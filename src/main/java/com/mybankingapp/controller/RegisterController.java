package com.mybankingapp.controller;
import java.math.BigDecimal;
import com.mybankingapp.MainApp;
import com.mybankingapp.model.User;
import com.mybankingapp.dao.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField initialBalanceField;
    @FXML
    private Label messageLabel;

    private MainApp mainApp;
    private UserDao userDao;

    @FXML
    public void initialize() {
        userDao = new UserDao();
        initialBalanceField.setText("0.00");
    }


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String iban = ibanField.getText();
        double initialBalance;


        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || iban.isEmpty() || initialBalanceField.getText().isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        try {
            initialBalance = Double.parseDouble(initialBalanceField.getText());
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid initial balance. Please enter a number.");
            return;
        }

        try {

            User newUser = new User(0, firstName, lastName, username, password, iban, BigDecimal.valueOf(initialBalance));
            userDao.create(newUser);
            messageLabel.setText("Registration successful!");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful! You can now log in.");


            if (mainApp != null) {
                mainApp.showLoginScene();
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("users_username_key")) {
                messageLabel.setText("Username already exists. Please choose a different one.");
            } else if (e.getMessage().contains("users_iban_key")) {
                messageLabel.setText("IBAN already exists. Please use a different one.");
            } else {
                messageLabel.setText("Database error during registration: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBackButton(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLoginScene();
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleLoginLink(ActionEvent actionEvent) {

    }
}