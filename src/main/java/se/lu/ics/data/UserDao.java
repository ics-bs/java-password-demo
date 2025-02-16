package se.lu.ics.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String sqlQuery = "SELECT UserPassword FROM AppUser WHERE Username = ?";

        try (Connection connection = connectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            // Check if user exists and compare passwords
            if (rs.next()) {
                String storedPassword = rs.getString("UserPassword");
                return storedPassword.equals(password);
            }

            return false;

        } catch (SQLException e) {
            if (e.getErrorCode() == 0) {
                throw new DaoException("Error: Could not reach the server", e);
            } else {
                throw new DaoException("Error: Unknown, contact the system administrator", e);
            }
        }
    }
}
