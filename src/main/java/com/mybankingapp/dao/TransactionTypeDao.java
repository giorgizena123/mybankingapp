package com.mybankingapp.dao;

import com.mybankingapp.model.TransactionType;
import com.mybankingapp.util.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransactionTypeDao {


    public TransactionType create(String typeName) throws SQLException {
        String sql = "INSERT INTO transaction_types (type_name) VALUES (?)";
        TransactionType transactionType = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, typeName);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    transactionType = new TransactionType(id, typeName);
                }
            }
        }
        return transactionType;
    }


    public TransactionType findByTransactionTypeName(String typeName) throws SQLException {
        String sql = "SELECT id, type_name FROM transaction_types WHERE type_name = ?";
        TransactionType transactionType = null;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, typeName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String dbTypeName = rs.getString("type_name");
                    transactionType = new TransactionType(id, dbTypeName);
                }
            }
        }
        return transactionType;
    }


    public List<TransactionType> findAll() throws SQLException {
        String sql = "SELECT id, type_name FROM transaction_types ORDER BY type_name";
        List<TransactionType> transactionTypes = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String dbTypeName = rs.getString("type_name");
                transactionTypes.add(new TransactionType(id, dbTypeName));
            }
        }
        return transactionTypes;
    }


}