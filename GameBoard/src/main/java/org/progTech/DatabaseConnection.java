package org.progTech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection {

    private Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "gameboard";
        String databaseUser = "root";
        String databasePassword = ""; // Replace with your MySQL password
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }

        return databaseLink;
    }

    public void saveWinner(String winnerName) {
        try (Connection connection = getConnection()) {
            if (connection == null) {
                System.out.println("No connection to the database.");
                return;
            }

            // Check if the player already exists in the database
            String query = "SELECT nyertjatek FROM jatekosok WHERE nev = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, winnerName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Player exists, update the win count
                int currentWins = resultSet.getInt("nyertjatek");
                query = "UPDATE jatekosok SET nyertjatek = ? WHERE nev = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, currentWins + 1);
                preparedStatement.setString(2, winnerName);
                preparedStatement.executeUpdate();
            } else {
                // Player does not exist, insert a new record
                query = "INSERT INTO jatekosok (nev, nyertjatek) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, winnerName);
                preparedStatement.setInt(2, 1);
                preparedStatement.executeUpdate();
            }

            System.out.println("Winner saved successfully!");
        } catch (Exception e) {
            System.out.println("Failed to save winner: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
