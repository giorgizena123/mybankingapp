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

    /**
     * Checks if a username is already taken in the database.
     * @param username The username to check.
     * @return true if the username is taken, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean isUsernameTaken(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?"; // Corrected SQL
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

    /**
     * Registers a new user in the database.
     * Generates a unique IBAN for the user.
     * @param user The User object containing registration details.
     * @return true if the user was successfully registered, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, first_name, last_name, iban, money) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             // IMPORTANT: Statement.RETURN_GENERATED_KEYS is crucial for getting auto-generated ID
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());

            // Generate a unique IBAN
            String iban = generateUniqueIBAN();
            // Ensure IBAN is unique by checking against DB (simple retry loop)
            while (findByIBAN(iban) != null) {
                iban = generateUniqueIBAN();
            }
            user.setIban(iban); // Set the generated IBAN to the user object

            stmt.setString(5, user.getIban()); // Set the generated IBAN
            stmt.setBigDecimal(6, BigDecimal.ZERO); // Initial money is BigDecimal.ZERO

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the auto-generated ID
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

    /**
     * Retrieves a user from the database by username.
     * @param username The username of the user to retrieve.
     * @return The User object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Retrieves a user from the database by IBAN.
     * @param iban The IBAN of the user to retrieve.
     * @return The User object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Retrieves all users from the database.
     * @return A list of all User objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<User> findAllUsers() throws SQLException { // Renamed from findAll to findAllUsers for clarity
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

    /**
     * Authenticates a user based on username and password.
     * @param username The username.
     * @param password The password.
     * @return The User object if authentication is successful, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Updates a user's details (username, password, first_name, last_name, iban, money) in the database.
     * @param user The User object with updated details.
     * @return true if the user was successfully updated, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Generates a unique IBAN (simplified for demonstration).
     * Format: GE + 2 digits + 16 alphanumeric characters.
     * @return A generated IBAN string.
     */
    private String generateUniqueIBAN() {
        SecureRandom random = new SecureRandom();
        String countryCode = "GE";
        String bankCode = String.format("%02d", random.nextInt(100)); // 2 random digits for bank code
        // 16 alphanumeric characters for account number
        String accountNumber = random.ints(48, 123) // ASCII range for alphanumeric
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)) // filter for 0-9, A-Z, a-z
                .limit(16)
                .mapToObj(i -> (char) i)
                .map(String::valueOf)
                .collect(Collectors.joining());

        return countryCode + bankCode + accountNumber.toUpperCase();
    }
}
