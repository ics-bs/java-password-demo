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
     * Authenticates a user by verifying the provided password against the database.
     *
     * The password is hashed using SHA-256 with a unique, per-user salt
     * retrieved from the database. The computed hash is then compared
     * to the stored hash for authentication.
     *
     * @param username The username to look up.
     * @param password The provided password (plaintext input from the user).
     * @return true if the salted and hashed password matches the stored hash, false
     *         otherwise.
     */
    public boolean authenticate(String username, String password) {
        String sqlQuery = "SELECT PasswordHash, Salt FROM AppUser WHERE Username = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Check if user exists and compare hashed passwords
            if (resultSet.next()) {
                byte[] storedHash = resultSet.getBytes("PasswordHash");
                String salt = resultSet.getString("Salt");

                // Hash the input password with the retrieved salt
                String saltedPassword = password + salt;
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] computedHash = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

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
