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
        String databasePassword = "";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            System.out.println("Az adatbázishoz való csatlakozás sikertelen: " + e.getMessage());
            e.printStackTrace();
        }

        return databaseLink;
    }

    public void saveWinner(String winnerName) {
        try (Connection connection = getConnection()) {
            if (connection == null) {
                System.out.println("Nincs kapcsolat az adatbázis között.");
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

            System.out.println("A játék nyertese sikeresen elmentve!");
        } catch (Exception e) {
            System.out.println("Sikertelen mentés a játék nyerteséről: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getTopPlayers(int topN) {
        try (Connection connection = getConnection()) {
            if (connection == null) {
                System.out.println("Nincs kapcsolat az adatbázis között");
                return;
            }

            // Query to get top players sorted by wins in descending order
            String query = "SELECT nev, nyertjatek FROM jatekosok ORDER BY nyertjatek DESC LIMIT ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, topN);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Top " + topN + " játékosok:");
            System.out.println("----------------------");
            while (resultSet.next()) {
                String playerName = resultSet.getString("nev");
                int wins = resultSet.getInt("nyertjatek");
                System.out.println(playerName + " - " + wins + " nyert játékok");
            }
            System.out.println("----------------------");

        } catch (Exception e) {
            System.out.println("Hiba a top játékosok listájának betöltése közben: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
