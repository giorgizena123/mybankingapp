package com.mybankingapp.controller;

import com.mybankingapp.MainApp;
import com.mybankingapp.dao.UserDao;
import com.mybankingapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private Label statusLabel;

    private MainApp mainApp;
    private UserDao userDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDao = new UserDao();
        statusLabel.setText("");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "შეცდომა", "გთხოვთ შეავსოთ ყველა ველი.");
            return;
        }

        try {
            if (userDao.isUsernameTaken(username)) {
                showAlert(Alert.AlertType.ERROR, "რეგისტრაციის შეცდომა", "ეს მომხმარებლის სახელი დაკავებულია.");
                return;
            }

            User newUser = new User(username, password, firstName, lastName);
            if (userDao.registerUser(newUser)) {
                showAlert(Alert.AlertType.INFORMATION, "წარმატება", "რეგისტრაცია წარმატებით დასრულდა! თქვენი IBAN: " + newUser.getIban());
                mainApp.showLoginScene();
            } else {
                showAlert(Alert.AlertType.ERROR, "რეგისტრაციის შეცდომა", "მომხმარებლის რეგისტრაცია ვერ მოხერხდა.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ბაზის შეცდომა", "მონაცემთა ბაზასთან დაკავშირების შეცდომა: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoginLink(ActionEvent event) {
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
}