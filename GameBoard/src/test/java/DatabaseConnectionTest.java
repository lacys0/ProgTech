import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.progTech.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    private DatabaseConnection dbConnection;
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/gameboard";
    private final String user = "root";
    private final String password = "";


    @BeforeEach
    void setUp() throws SQLException {
        dbConnection = new DatabaseConnection();
        connection = DriverManager.getConnection(url, user, password);
        //Clean up before each test.  Important to avoid test interference
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM jatekosok");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void testGetConnection() {
        Connection conn = dbConnection.getConnection();
        assertNotNull(conn, "Connection should not be null");
        // Add more robust checks if needed, e.g., checking connection validity
        try {
            conn.close();
        } catch (SQLException e) {
            fail("Failed to close connection: " + e.getMessage());
        }

    }

    @Test
    void testSaveWinner_NewPlayer() {
        dbConnection.saveWinner("Alice");
        try (Statement statement = connection.createStatement();
             java.sql.ResultSet resultSet = statement.executeQuery("SELECT nyertjatek FROM jatekosok WHERE nev = 'Alice'")) {
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt("nyertjatek"));
        } catch (SQLException e) {
            fail("Failed to check database: " + e.getMessage());
        }
    }

    @Test
    void testSaveWinner_ExistingPlayer() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO jatekosok (nev, nyertjatek) VALUES ('Bob', 2)");
        } catch (SQLException e) {
            fail("Failed to insert test data: "+ e.getMessage());
        }
        dbConnection.saveWinner("Bob");
        try (Statement statement = connection.createStatement();
             java.sql.ResultSet resultSet = statement.executeQuery("SELECT nyertjatek FROM jatekosok WHERE nev = 'Bob'")) {
            assertTrue(resultSet.next());
            assertEquals(3, resultSet.getInt("nyertjatek"));
        } catch (SQLException e) {
            fail("Failed to check database: " + e.getMessage());
        }
    }


    @Test
    void testSaveWinner_NoConnection() {
        DatabaseConnection dbc = new DatabaseConnection();
        dbc.saveWinner("Charlie"); //This may print a failure message to console but may not directly throw an exception to test

    }

}
