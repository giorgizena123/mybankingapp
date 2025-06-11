package com.mybankingapp.dao;

import com.mybankingapp.model.Transaction;
import com.mybankingapp.model.TransactionType;
import com.mybankingapp.model.User;
import com.mybankingapp.util.DatabaseConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    private UserDao userDao;
    private TransactionTypeDao transactionTypeDao;

    public TransactionDao() {
        this.userDao = new UserDao();
        this.transactionTypeDao = new TransactionTypeDao();
    }


    public boolean recordTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (from_user_username, to_user_username, amount, type_id, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            if (transaction.getFromUser() != null) {
                stmt.setString(1, transaction.getFromUser().getUsername());
            } else {
                stmt.setNull(1, java.sql.Types.VARCHAR);
            }


            if (transaction.getToUser() != null) {
                stmt.setString(2, transaction.getToUser().getUsername());
            } else {
                stmt.setNull(2, java.sql.Types.VARCHAR);
            }


            stmt.setBigDecimal(3, transaction.getAmount());
            stmt.setLong(4, transaction.getType().getId());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        }
        return false;
    }


    public List<Transaction> getTransactionsForUser(String username) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, from_user_username, to_user_username, amount, date, type_id FROM transactions " +
                "WHERE from_user_username = ? OR to_user_username = ? ORDER BY date DESC";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String fromUsername = rs.getString("from_user_username");
                String toUsername = rs.getString("to_user_username");
                BigDecimal amount = rs.getBigDecimal("amount");
                LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
                Long typeId = rs.getLong("type_id");


                User fromUser = (fromUsername != null) ? userDao.findByUsername(fromUsername) : null;
                User toUser = (toUsername != null) ? userDao.findByUsername(toUsername) : null;
                TransactionType type = transactionTypeDao.getTypeById(typeId);

                transactions.add(new Transaction(id, fromUser, toUser, amount, date, type));
            }
        }
        return transactions;
    }


    public List<Transaction> getLatestTransactionsForUser(String username, int limit) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, from_user_username, to_user_username, amount, date, type_id FROM transactions " +
                "WHERE from_user_username = ? OR to_user_username = ? ORDER BY date DESC LIMIT ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setInt(3, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String fromUsername = rs.getString("from_user_username");
                String toUsername = rs.getString("to_user_username");
                BigDecimal amount = rs.getBigDecimal("amount");
                LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
                Long typeId = rs.getLong("type_id");


                User fromUser = (fromUsername != null) ? userDao.findByUsername(fromUsername) : null;
                User toUser = (toUsername != null) ? userDao.findByUsername(toUsername) : null;
                TransactionType type = transactionTypeDao.getTypeById(typeId);

                transactions.add(new Transaction(id, fromUser, toUser, amount, date, type));
            }
        }
        return transactions;
    }
}