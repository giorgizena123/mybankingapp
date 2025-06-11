package com.mybankingapp.controller;

import com.mybankingapp.MainApp;
import com.mybankingapp.dao.TransactionDao;
import com.mybankingapp.dao.TransactionTypeDao;
import com.mybankingapp.dao.UserDao;
import com.mybankingapp.model.Transaction;
import com.mybankingapp.model.TransactionType;
import com.mybankingapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime; // Make sure this is imported
import java.util.ResourceBundle;

public class WithdrawController implements Initializable {

    @FXML
    private TextField amountField;
    @FXML
    private Label messageLabel;

    private MainApp mainApp;
    private User currentUser;
    private UserDao userDao;
    private TransactionDao transactionDao;
    private TransactionTypeDao transactionTypeDao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userDao = new UserDao();
        transactionDao = new TransactionDao();
        transactionTypeDao = new TransactionTypeDao();
        messageLabel.setText(""); // Clear message label on init
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void handleWithdrawButton(ActionEvent event) { // Correct method name
        BigDecimal amountBigDecimal;

        if (amountField.getText().isEmpty()) {
            messageLabel.setText("გთხოვთ შეიყვანოთ თანხა.");
            return;
        }

        try {
            amountBigDecimal = new BigDecimal(amountField.getText());

            if (amountBigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
                messageLabel.setText("თანხა დადებითი უნდა იყოს.");
                return;
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("არასწორი თანხა. გთხოვთ შეიყვანოთ რიცხვი.");
            return;
        }

        if (currentUser == null) {
            messageLabel.setText("მომხმარებელი ვერ მოიძებნა.");
            return;
        }

        // შეამოწმეთ საკმარისი თანხა მონაცემთა ბაზაში ოპერაციამდე
        if (currentUser.getMoney().compareTo(amountBigDecimal) < 0) {
            messageLabel.setText("არასაკმარისი თანხა.");
            return;
        }

        try {
            // მომხმარებლის ბალანსის განახლება მონაცემთა ბაზაში
            currentUser.setMoney(currentUser.getMoney().subtract(amountBigDecimal));
            boolean success = userDao.update(currentUser); // Call userDao.update()

            if (success) {
                // ტრანზაქციის ჩაწერა
                TransactionType withdrawType = transactionTypeDao.getTypeByName("withdraw");
                if (withdrawType == null) {
                    throw new SQLException("ტრანზაქციის ტიპი 'withdraw' ვერ მოიძებნა მონაცემთა ბაზაში. გთხოვთ დარწმუნდით, რომ 'types' ცხრილი შევსებულია.");
                }
                // გამოტანისას 'toUser' null-ია (თანხა გადის სისტემიდან)
                Transaction withdrawTransaction = new Transaction(currentUser, null, amountBigDecimal, withdrawType);
                transactionDao.recordTransaction(withdrawTransaction);

                messageLabel.setText("თანხის გამოტანა წარმატებით დასრულდა!");
                showAlert(Alert.AlertType.INFORMATION, "წარმატება", "თანხა წარმატებით გამოვიდა!");

                // მომხმარებლის მონაცემების განახლება და მთავარ გვერდზე დაბრუნება
                User updatedUser = userDao.findByUsername(currentUser.getUsername()); // Fetch updated user details
                if (mainApp != null && updatedUser != null) {
                    mainApp.showHomePage(updatedUser);
                }
            } else {
                messageLabel.setText("თანხის გამოტანა ვერ მოხერხდა. სცადეთ ხელახლა.");
            }
        } catch (SQLException e) {
            messageLabel.setText("მონაცემთა ბაზის შეცდომა თანხის გამოტანისას: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ბაზის შეცდომა", "შეცდომა მონაცემთა ბაზასთან: " + e.getMessage());
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
}