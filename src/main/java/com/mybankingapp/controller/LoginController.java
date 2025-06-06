package com.mybankingapp.controller;
import com.mybankingapp.MainApp;
import com.mybankingapp.dao.UserDao;
import com.mybankingapp.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private UserDao userDao = new UserDao();

    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("გთხოვთ, შეავსოთ ყველა ველი.");
            return;
        }

        try {
            User user = userDao.findByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                statusLabel.setText("წარმატებული ავტორიზაცია!");
                MainApp.showHomePage(user);
            } else {
                statusLabel.setText("მომხმარებელი ან პაროლი არასწორია.");
            }
        } catch (SQLException e) {
            statusLabel.setText("მონაცემთა ბაზის შეცდომა: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            statusLabel.setText("აპლიკაციის შეცდომა: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterLink() {
        try {
            MainApp.showRegisterScene();
        } catch (IOException e) {
            statusLabel.setText("აპლიკაციის შეცდომა: " + e.getMessage());
            e.printStackTrace();
        }
    }
}