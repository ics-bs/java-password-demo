package se.lu.ics.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDao {

    private ConnectionHandler connectionHandler;

    public UserDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    /**
     * Authenticates a user by checking the provided password against the database.
     *
     * @param username The username to look up.
     * @param password The provided password (plaintext in this example).
     * @return true if the password matches, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        String sqlQuery = "SELECT PasswordHash FROM AppUser WHERE Username = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Check if user exists and compare hashed passwords
            if (resultSet.next()) {
                byte[] storedHash = resultSet.getBytes("PasswordHash");

                // Hash the input password using SHA-256
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] computedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

                // Compare computed hash with the stored hash
                return Arrays.equals(storedHash, computedHash);
            }

            return false;

        } catch (SQLException e) {
            if (e.getErrorCode() == 0) {
                throw new DaoException("Error: Could not reach the server", e);
            } else {
                throw new DaoException("Error: Unknown, contact the system administrator", e);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new DaoException("Internal error: contact the system administrator", e);
        }
    }
}
