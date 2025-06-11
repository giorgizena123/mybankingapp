package com.mybankingapp;

import com.mybankingapp.controller.HomePageController;
import com.mybankingapp.controller.LoginController;
import com.mybankingapp.controller.RegisterController;
import com.mybankingapp.controller.TransferController;
import com.mybankingapp.controller.DepositController;
import com.mybankingapp.controller.WithdrawController;
import com.mybankingapp.controller.AllTransactionsController;
import com.mybankingapp.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // Keep this for now, though custom modal is preferred
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    private Stage primaryStage;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("My Banking App");
        showLoginScene(); // Start with the login scene
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void showLoginScene() {
        try {
            // CORRECTED PATH: /com/mybankingapp/view/Login.fxml
            URL fxmlLocation = getClass().getResource("/com/mybankingapp/view/Login.fxml");
            if (fxmlLocation == null) {
                System.err.println("Error: Login.fxml not found! Check the path: /com/mybankingapp/view/Login.fxml");
                showAlert(Alert.AlertType.ERROR, "Loading Error", "Login screen FXML file not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            if (loginController != null) {
                loginController.setMainApp(this);
            } else {
                System.err.println("Error: LoginController not found for Login.fxml.");
                showAlert(Alert.AlertType.ERROR, "Controller Error", "LoginController not found.");
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load Login screen: " + e.getMessage());
        }
    }

    public void showRegisterScene() {
        try {
            // CORRECTED PATH: /com/mybankingapp/view/Register.fxml
            URL fxmlLocation = getClass().getResource("/com/mybankingapp/view/Register.fxml");
            if (fxmlLocation == null) {
                System.err.println("Error: Register.fxml not found! Check the path: /com/mybankingapp/view/Register.fxml");
                showAlert(Alert.AlertType.ERROR, "Loading Error", "Register screen FXML file not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mybankingapp/view/Register.fxml"));
            Parent root = loader.load();

            RegisterController controller = loader.getController();
            if (controller != null) {
                controller.setMainApp(this);
            } else {
                System.err.println("Error: RegisterController not found for Register.fxml.");
                showAlert(Alert.AlertType.ERROR, "Controller Error", "RegisterController not found.");
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load Register screen: " + e.getMessage());
        }
    }


    public void showHomePage(User user) {
        this.currentUser = user;
        try {
            // CORRECTED PATH: /com/mybankingapp/view/HomePage.fxml
            URL fxmlLocation = getClass().getResource("/com/mybankingapp/view/HomePage.fxml");
            if (fxmlLocation == null) {
                System.err.println("Error: HomePage.fxml not found! Check the path: /com/mybankingapp/view/HomePage.fxml");
                showAlert(Alert.AlertType.ERROR, "Loading Error", "Home Page FXML file not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            HomePageController controller = loader.getController();
            if (controller != null) {
                controller.setMainApp(this);
                controller.setUser(user);
            } else {
                System.err.println("Error: HomePageController not found for HomePage.fxml.");
                showAlert(Alert.AlertType.ERROR, "Controller Error", "HomePageController not found.");
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("My Banking App - Home");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load Home Page: " + e.getMessage());
        }
    }


    public void showTransferScene(User user) {
        try {
            // CORRECTED PATH: /com/mybankingapp/view/Transfer.fxml
            URL fxmlLocation = getClass().getResource("/com/mybankingapp/view/Transfer.fxml");
            if (fxmlLocation == null) {
                System.err.println("Error: Transfer.fxml not found! Check the path: /com/mybankingapp/view/Transfer.fxml");
                showAlert(Alert.AlertType.ERROR, "Loading Error", "Transfer screen FXML file not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            TransferController controller = loader.getController();
            if (controller != null) {
                controller.setMainApp(this);
                controller.setUser(user);
            } else {
                System.err.println("Error: TransferController not found for Transfer.fxml.");
                showAlert(Alert.AlertType.ERROR, "Controller Error", "TransferController not found.");
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("My Banking App - Transfer");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load Transfer screen: " + e.getMessage());
        }
    }


    public void showDepositScene(User user) {
        try {
            // CORRECTED PATH: /com/mybankingapp/view/Deposit.fxml
            URL fxmlLocation = getClass().getResource("/com/mybankingapp/view/Deposit.fxml");
            if (fxmlLocation == null) {
                System.err.println("Error: Deposit.fxml not found! Check the path: /com/mybankingapp/view/Deposit.fxml");
                showAlert(Alert.AlertType.ERROR, "Loading Error", "Deposit screen FXML file not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            DepositController controller = loader.getController();
            if (controller != null) {
                controller.setMainApp(this);
                controller.setUser(user);
            } else {
                System.err.println("Error: DepositController not found for Deposit.fxml.");
                showAlert(Alert.AlertType.ERROR, "Controller Error", "DepositController not found.");
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("My Banking App - Deposit");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load Deposit screen: " + e.getMessage());
        }
    }


    public void showWithdrawScene(User user) {
        try {
            // CORRECTED PATH: /com/mybankingapp/view/Withdraw.fxml
            URL fxmlLocation = getClass().getResource("/com/mybankingapp/view/Withdraw.fxml");
            if (fxmlLocation == null) {
                System.err.println("Error: Withdraw.fxml not found! Check the path: /com/mybankingapp/view/Withdraw.fxml");
                showAlert(Alert.AlertType.ERROR, "Loading Error", "Withdrawal screen FXML file not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            WithdrawController controller = loader.getController();
            if (controller != null) {
                controller.setMainApp(this);
                controller.setUser(user);
            } else {
                System.err.println("Error: WithdrawController not found for Withdraw.fxml.");
                showAlert(Alert.AlertType.ERROR, "Controller Error", "WithdrawController not found.");
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("My Banking App - Withdraw");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load Withdrawal screen: " + e.getMessage());
        }
    }


    public void showAllTransactionsScene(User user) {
        try {
            // CORRECTED PATH: /com/mybankingapp/view/AllTransactions.fxml
            URL fxmlLocation = getClass().getResource("/com/mybankingapp/view/AllTransactions.fxml");
            if (fxmlLocation == null) {
                System.err.println("Error: AllTransactions.fxml not found! Check the path: /com/mybankingapp/view/AllTransactions.fxml");
                showAlert(Alert.AlertType.ERROR, "Loading Error", "All Transactions FXML file not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            AllTransactionsController controller = loader.getController();
            if (controller != null) {
                controller.setMainApp(this);
                controller.setUser(user);
            } else {
                System.err.println("Error: AllTransactionsController not found for AllTransactions.fxml.");
                showAlert(Alert.AlertType.ERROR, "Controller Error", "AllTransactionsController not found.");
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("My Banking App - All Transactions");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load All Transactions screen: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}