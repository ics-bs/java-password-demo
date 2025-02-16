package se.lu.ics.data;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserDao {

    private ConnectionHandler connectionHandler;

    public UserDao() throws IOException {
        this.connectionHandler = new ConnectionHandler();
    }

    /**
     * Authenticates a user by verifying the provided password against the database.
     *
     * The password is hashed using PBKDF2WithHmacSHA256 with a unique, per-user salt
     * and iteration count retrieved from the database. The computed hash is then
     * compared to the stored hash for authentication.
     *
     * @param username The username to look up.
     * @param password The provided password (plaintext input from the user).
     * @return true if the computed hash matches the stored hash, false otherwise.
     */
    public boolean authenticate(String username, String password) {
        String sqlQuery = "SELECT PasswordHash, Salt, Iterations FROM AppUser WHERE Username = ?";

        try (Connection connection = connectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Check if user exists
            if (resultSet.next()) {
                byte[] storedHash = resultSet.getBytes("PasswordHash");
                String salt = resultSet.getString("Salt");
                int iterations = resultSet.getInt("Iterations");

                // Hash the password using PBKDF2 with the retrieved salt and iterations
                byte[] computedHash = hashPassword(password, salt, iterations);

                // Compare the computed hash with the stored hash
                return Arrays.equals(storedHash, computedHash);
            }

            return false;

        } catch (SQLException e) {
            if (e.getErrorCode() == 0) {
                throw new DaoException("Error: Could not reach the server", e);
            } else {
                throw new DaoException("Error: Unknown, contact the system administrator", e);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new DaoException("Internal error: contact the system administrator", e);
        }
    }

    /**
     * Hashes a password using PBKDF2 with HMAC SHA-256.
     *
     * @param password The plaintext password.
     * @param salt The salt value.
     * @param iterations The number of iterations.
     * @return byte array containing the hashed password.
     */
    private byte[] hashPassword(String password, String salt, int iterations) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return factory.generateSecret(spec).getEncoded();
    }
}