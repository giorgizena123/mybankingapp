package com.mybankingapp.dao;

import com.mybankingapp.model.User;
import com.mybankingapp.util.DatabaseConnectionManager;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

    public boolean isUsernameTaken(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }


    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, first_name, last_name, iban, money) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionManager.getConnection();

             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());


            String iban = generateUniqueIBAN();

            while (findByIBAN(iban) != null) {
                iban = generateUniqueIBAN();
            }
            user.setIban(iban);

            stmt.setString(5, user.getIban());
            stmt.setBigDecimal(6, BigDecimal.ZERO);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }
        }
        return false;
    }


    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password, first_name, last_name, iban, money FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("iban"),
                        rs.getBigDecimal("money")
                );
            }
        }
        return null;
    }


    public User findByIBAN(String iban) throws SQLException {
        String sql = "SELECT id, username, password, first_name, last_name, iban, money FROM users WHERE iban = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iban);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("iban"),
                        rs.getBigDecimal("money")
                );
            }
        }
        return null;
    }


    public List<User> findAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, first_name, last_name, iban, money FROM users";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("iban"),
                        rs.getBigDecimal("money")
                ));
            }
        }
        return users;
    }


    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, first_name, last_name, iban, money FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("iban"),
                        rs.getBigDecimal("money")
                );
            }
        }
        return null;
    }


    public boolean update(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ?, iban = ?, money = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getIban());
            stmt.setBigDecimal(6, user.getMoney());
            stmt.setLong(7, user.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }


    private String generateUniqueIBAN() {
        SecureRandom random = new SecureRandom();
        String countryCode = "GE";
        String bankCode = String.format("%02d", random.nextInt(100));

        String accountNumber = random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(16)
                .mapToObj(i -> (char) i)
                .map(String::valueOf)
                .collect(Collectors.joining());

        return countryCode + bankCode + accountNumber.toUpperCase();
    }
}
