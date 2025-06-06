package com.mybankingapp.dao;

import com.mybankingapp.model.User;
import com.mybankingapp.util.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserDao {


    public User create(User user) throws SQLException {
        String sql = "INSERT INTO users (first_name, last_name, username, password, iban, money) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getIban());
            stmt.setBigDecimal(6, user.getMoney());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
        }
        return user;
    }


    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ?, iban = ?, money = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getIban());
            stmt.setBigDecimal(6, user.getMoney());
            stmt.setLong(7, user.getId());
            stmt.executeUpdate();
        }
    }


    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, first_name, last_name, username, password, iban, money FROM users WHERE username = ?";
        User user = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String dbUsername = rs.getString("username");
                    String dbPassword = rs.getString("password");
                    String iban = rs.getString("iban");
                    BigDecimal money = rs.getBigDecimal("money");
                    user = new User(id, firstName, lastName, dbUsername, dbPassword, iban, money);
                }
            }
        }
        return user;
    }


    public User findByIBAN(String iban) throws SQLException {
        String sql = "SELECT id, first_name, last_name, username, password, iban, money FROM users WHERE iban = ?";
        User user = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String dbUsername = rs.getString("username");
                    String dbPassword = rs.getString("password");
                    String dbIban = rs.getString("iban");
                    BigDecimal money = rs.getBigDecimal("money");
                    user = new User(id, firstName, lastName, dbUsername, dbPassword, dbIban, money);
                }
            }
        }
        return user;
    }


    public User findByUsernameAndPassword(String username, String password) throws SQLException {

        String sql = "SELECT id, first_name, last_name, username, password, iban, money FROM users WHERE username = ? AND password = ?";
        User user = null;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String dbUsername = rs.getString("username");
                    String dbPassword = rs.getString("password");
                    String iban = rs.getString("iban");
                    BigDecimal money = rs.getBigDecimal("money");

                    user = new User(id, firstName, lastName, dbUsername, dbPassword, iban, money);
                }
            }
        }
        return user;
    }


    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, first_name, last_name, username, password, iban, money FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                String iban = rs.getString("iban");
                BigDecimal money = rs.getBigDecimal("money");
                users.add(new User(id, firstName, lastName, dbUsername, dbPassword, iban, money));
            }
        }
        return users;
    }
}