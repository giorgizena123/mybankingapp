package com.mybankingapp.dao;

import com.mybankingapp.model.TransactionType;
import com.mybankingapp.util.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionTypeDao {

    /**
     * Retrieves a transaction type from the database by its name.
     * @param name The name of the transaction type (e.g., "deposit", "withdraw", "transfer").
     * @return The TransactionType object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public TransactionType getTypeByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM types WHERE name = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new TransactionType(rs.getLong("id"), rs.getString("name"));
            }
        }
        return null;
    }

    /**
     * Retrieves a transaction type from the database by its ID.
     * @param id The ID of the transaction type.
     * @return The TransactionType object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public TransactionType getTypeById(Long id) throws SQLException {
        String sql = "SELECT id, name FROM types WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new TransactionType(rs.getLong("id"), rs.getString("name"));
            }
        }
        return null;
    }
}