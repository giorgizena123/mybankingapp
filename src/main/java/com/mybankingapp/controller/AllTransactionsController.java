package com.mybankingapp.controller;
import java.math.BigDecimal;
import com.mybankingapp.MainApp;
import com.mybankingapp.model.User;
import com.mybankingapp.model.Transaction;
import com.mybankingapp.dao.TransactionDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;

public class AllTransactionsController {

    @FXML
    private Label messageLabel;
    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, Integer> idColumn;
    @FXML
    private TableColumn<Transaction, String> fromColumn;
    @FXML
    private TableColumn<Transaction, String> toColumn;
    @FXML
    private TableColumn<Transaction, BigDecimal> amountColumn;
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    @FXML
    private TableColumn<Transaction, LocalDateTime> dateColumn;

    private MainApp mainApp;
    private User currentUser;
    private TransactionDao transactionDao;


    @FXML
    public void initialize() {
        transactionDao = new TransactionDao();


        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("fromUserUsername"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("toUserUsername"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

    }


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    public void setUser(User user) {
        this.currentUser = user;
        if (currentUser != null) {
            loadTransactions();
        }
    }


    private void loadTransactions() {
        try {

            List<Transaction> transactions = transactionDao.getTransactionsByUser(currentUser.getUsername());
            ObservableList<Transaction> observableTransactions = FXCollections.observableArrayList(transactions);
            transactionsTable.setItems(observableTransactions);

            if (transactions.isEmpty()) {
                messageLabel.setText("No transactions found for this account.");
            } else {
                messageLabel.setText("");
            }

        } catch (SQLException e) {
            messageLabel.setText("Error loading transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBackButton(ActionEvent event) {
        if (mainApp != null && currentUser != null) {
            mainApp.showHomePage(currentUser);
        }
    }

    public void handleBackToHome(ActionEvent actionEvent) {

    }
}