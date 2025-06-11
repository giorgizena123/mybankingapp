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

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel; // For displaying messages like "Login failed"

    private MainApp mainApp;
    private UserDao userDao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userDao = new UserDao();
        statusLabel.setText(""); // Clear status label on init
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "შეცდომა", "გთხოვთ შეავსოთ ყველა ველი.");
            return;
        }

        try {
            // CORRECTED METHOD CALL: from findByUsernameAndPassword to authenticate
            User user = userDao.authenticate(username, password);
            if (user != null) {
                showAlert(Alert.AlertType.INFORMATION, "წარმატება", "შესვლა წარმატებით დასრულდა!");
                mainApp.showHomePage(user); // Pass the authenticated user to HomePage
            } else {
                showAlert(Alert.AlertType.ERROR, "შესვლა ვერ მოხერხდა", "არასწორი მომხმარებლის სახელი ან პაროლი.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ბაზის შეცდომა", "მონაცემთა ბაზასთან დაკავშირების შეცდომა: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegisterLink(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showRegisterScene();
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