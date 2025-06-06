package com.mybankingapp.controller;

import com.mybankingapp.MainApp;
import com.mybankingapp.dao.UserDao;
import com.mybankingapp.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;

import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink registerLink;

    private MainApp mainApp;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter both username and password.");
            return;
        }

        UserDao userDao = new UserDao();
        try {
            User user = userDao.findByUsernameAndPassword(username, password);

            if (user != null) {

                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getFirstName() + "!");
                if (mainApp != null) {
                    mainApp.showHomePage(user);
                }
            } else {

                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while trying to log in: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleRegisterLink() {
        if (mainApp != null) {
            mainApp.showRegisterScene();
        } else {
            System.err.println("MainApp is null. Cannot show register scene.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}