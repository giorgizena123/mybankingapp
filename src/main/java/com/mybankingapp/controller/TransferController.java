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
import java.util.ResourceBundle;

public class TransferController implements Initializable {

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
    private void handleTransferButton(ActionEvent event) {
        String toUsername = toUserUsernameField.getText();
        BigDecimal amountBigDecimal;


        if (toUsername.isEmpty() || amountField.getText().isEmpty()) {
            messageLabel.setText("გთხოვთ შეავსოთ ყველა ველი.");
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
            User receiver = userDao.findByUsername(toUsername);
            if (receiver == null) {
                messageLabel.setText("მიმღების მომხმარებლის სახელი ვერ მოიძებნა.");
                return;
            }

            if (currentUser.getUsername().equals(receiver.getUsername())) {
                messageLabel.setText("ვერ გადარიცხავთ საკუთარ თავს.");
                return;
            }

            // თანხის გამოკლება მიმდინარე მომხმარებლისგან
            currentUser.setMoney(currentUser.getMoney().subtract(amountBigDecimal));
            userDao.update(currentUser);

            // თანხის დამატება მიმღებს
            receiver.setMoney(receiver.getMoney().add(amountBigDecimal));
            userDao.update(receiver);

            // ტრანზაქციის ჩაწერა
            TransactionType transferType = transactionTypeDao.getTypeByName("transfer");
            if (transferType == null) {
                throw new SQLException("ტრანზაქციის ტიპი 'transfer' ვერ მოიძებნა მონაცემთა ბაზაში. გთხოვთ დარწმუნდით, რომ 'types' ცხრილი შევსებულია.");
            }
            Transaction transferTransaction = new Transaction(currentUser, receiver, amountBigDecimal, transferType);
            transactionDao.recordTransaction(transferTransaction);


            messageLabel.setText("გადარიცხვა წარმატებით დასრულდა!");
            showAlert(Alert.AlertType.INFORMATION, "წარმატება", "გადარიცხვა წარმატებით დასრულდა!");

            // მიმდინარე მომხმარებლის მონაცემების განახლება წარმატებული გადარიცხვის შემდეგ
            User updatedCurrentUser = userDao.findByUsername(currentUser.getUsername());
            if (mainApp != null && updatedCurrentUser != null) {
                mainApp.showHomePage(updatedCurrentUser); // მთავარ გვერდზე დაბრუნება განახლებული ბალანსით
            }

        } catch (SQLException e) {
            messageLabel.setText("მონაცემთა ბაზის შეცდომა გადარიცხვისას: " + e.getMessage());
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