package com.mybankingapp.dao;

import com.mybankingapp.model.Transaction;
import com.mybankingapp.model.User;
import com.mybankingapp.util.DatabaseConnectionManager;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    public Transaction create(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (from_user_username, to_user_username, amount, type_id, transaction_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, transaction.getFromUserUsername());
            stmt.setString(2, transaction.getToUserUsername());
            stmt.setBigDecimal(3, transaction.getAmount());


            TransactionTypeDao typeDao = new TransactionTypeDao();
            int typeId = typeDao.findByTransactionTypeName(transaction.getType()).getId();
            stmt.setInt(4, typeId);

            stmt.setTimestamp(5, Timestamp.valueOf(transaction.getTransactionDate()));

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setId(generatedKeys.getInt(1));
                }
            }
        }
        return transaction;
    }


    public List<Transaction> getTransactionsByUser(String username) throws SQLException {
        String sql = "SELECT t.id, t.from_user_username, t.to_user_username, t.amount, tt.type_name, t.transaction_date " +
                "FROM transactions t " +
                "JOIN transaction_types tt ON t.type_id = tt.id " +
                "WHERE t.from_user_username = ? OR t.to_user_username = ? " +
                "ORDER BY t.transaction_date DESC";

        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String fromUserUsername = rs.getString("from_user_username");
                    String toUserUsername = rs.getString("to_user_username");
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String typeName = rs.getString("type_name");
                    LocalDateTime transactionDate = rs.getTimestamp("transaction_date").toLocalDateTime();

                    transactions.add(new Transaction(id, fromUserUsername, toUserUsername, amount, typeName, transactionDate));
                }
            }
        }
        return transactions;
    }


    public List<Transaction> findBySenderUsername(String senderUsername) throws SQLException {
        String sql = "SELECT t.id, t.from_user_username, t.to_user_username, t.amount, tt.type_name, t.transaction_date " +
                "FROM transactions t " +
                "JOIN transaction_types tt ON t.type_id = tt.id " +
                "WHERE t.from_user_username = ? " +
                "ORDER BY t.transaction_date DESC";

        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, senderUsername);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String fromUserUsername = rs.getString("from_user_username");
                    String toUserUsername = rs.getString("to_user_username");
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String typeName = rs.getString("type_name");
                    LocalDateTime transactionDate = rs.getTimestamp("transaction_date").toLocalDateTime();

                    transactions.add(new Transaction(id, fromUserUsername, toUserUsername, amount, typeName, transactionDate));
                }
            }
        }
        return transactions;
    }


    public List<Transaction> findByReceiverUsername(String receiverUsername) throws SQLException {
        String sql = "SELECT t.id, t.from_user_username, t.to_user_username, t.amount, tt.type_name, t.transaction_date " +
                "FROM transactions t " +
                "JOIN transaction_types tt ON t.type_id = tt.id " +
                "WHERE t.to_user_username = ? " +
                "ORDER BY t.transaction_date DESC";

        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, receiverUsername);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String fromUserUsername = rs.getString("from_user_username");
                    String toUserUsername = rs.getString("to_user_username");
                    BigDecimal amount = rs.getBigDecimal("amount");
                    String typeName = rs.getString("type_name");
                    LocalDateTime transactionDate = rs.getTimestamp("transaction_date").toLocalDateTime();

                    transactions.add(new Transaction(id, fromUserUsername, toUserUsername, amount, typeName, transactionDate));
                }
            }
        }
        return transactions;
    }
}