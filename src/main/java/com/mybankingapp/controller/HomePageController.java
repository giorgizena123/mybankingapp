package com.mybankingapp.controller;

import com.mybankingapp.MainApp;
import com.mybankingapp.dao.TransactionDao;
import com.mybankingapp.dao.UserDao;
import com.mybankingapp.model.Transaction;
import com.mybankingapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label ibanLabel;
    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, Long> transactionIdColumn;
    @FXML
    private TableColumn<Transaction, String> fromUserColumn;
    @FXML
    private TableColumn<Transaction, String> toUserColumn;
    @FXML
    private TableColumn<Transaction, BigDecimal> amountColumn;
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    @FXML
    private TableColumn<Transaction, LocalDateTime> dateColumn;


    private MainApp mainApp;
    private User currentUser;
    private UserDao userDao;
    private TransactionDao transactionDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userDao = new UserDao();
        transactionDao = new TransactionDao();


        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fromUserColumn.setCellValueFactory(new PropertyValueFactory<>("fromUserUsername"));
        toUserColumn.setCellValueFactory(new PropertyValueFactory<>("toUserUsername"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeName"));


        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dateColumn.setCellFactory(column -> new javafx.scene.control.TableCell<Transaction, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setUser(User user) {
        this.currentUser = user;
        updateUserDetails();
        loadLatestTransactions();
    }

    private void updateUserDetails() {
        if (currentUser != null) {
            welcomeLabel.setText("კეთილი იყოს თქვენი მობრძანება, " + currentUser.getFirstName() + "!");
            balanceLabel.setText(currentUser.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " GEL");
            ibanLabel.setText(currentUser.getIban());
        }
    }

    private void loadLatestTransactions() {
        if (currentUser != null) {
            try {
                ObservableList<Transaction> transactions = FXCollections.observableArrayList(
                        transactionDao.getLatestTransactionsForUser(currentUser.getUsername(), 5)
                );
                transactionsTable.setItems(transactions);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "შეცდომა", "ვერ მოხერხდა ტრანზაქციების ჩატვირთვა: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        if (mainApp != null && currentUser != null) {
            mainApp.showDepositScene(currentUser);
        }
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        if (mainApp != null && currentUser != null) {
            mainApp.showWithdrawScene(currentUser);
        }
    }

    @FXML
    private void handleTransfer(ActionEvent event) {
        if (mainApp != null && currentUser != null) {
            mainApp.showTransferScene(currentUser);
        }
    }

    @FXML
    private void handleAllTransactions(ActionEvent event) {
        if (mainApp != null && currentUser != null) {
            mainApp.showAllTransactionsScene(currentUser);
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        if (mainApp != null) {
            currentUser = null;
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