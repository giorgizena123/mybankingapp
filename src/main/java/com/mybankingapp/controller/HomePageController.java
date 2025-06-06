package com.mybankingapp.controller;
import com.mybankingapp.MainApp;
import com.mybankingapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class HomePageController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label ibanLabel;

    private MainApp mainApp;
    private User currentUser;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    public void setUser(User user) {
        this.currentUser = user;
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getFirstName() + "!");
            balanceLabel.setText(String.format("Balance: %.2f GEL", currentUser.getMoney()));
            ibanLabel.setText("IBAN: " + currentUser.getIban());

        }
    }


    @FXML
    private void handleTransferButton(ActionEvent event) {
        if (mainApp != null && currentUser != null) {
            mainApp.showTransferScene(currentUser);
        }
    }


    @FXML
    private void handleLogoutButton(ActionEvent event) {
        if (mainApp != null) {

            mainApp.showLoginScene();
        }
    }


    public void handleDepositButton(ActionEvent actionEvent) {
    }

    public void handleWithdrawButton(ActionEvent actionEvent) {
    }

    public void handleAllTransactionsButton(ActionEvent actionEvent) {

    }
}