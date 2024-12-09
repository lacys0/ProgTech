package org.progTech;

import java.util.Scanner;

public class Connect4Manager {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char PLAYER_ONE_SYMBOL = 'X';
    private static final char PLAYER_TWO_SYMBOL = 'O';

    private Scanner scanner;
    private DatabaseConnection dbConnection;

    // Constructor with Dependency Injection
    public Connect4Manager(DatabaseConnection dbConnection) {
        this.scanner = new Scanner(System.in);
        this.dbConnection = dbConnection;
    }

    public void fomenu() {
        System.out.println("Entering fomenu ...");

        while (true) {
            System.out.println("=====Connect 4=====");
            System.out.println("1. Top játékosok");
            System.out.println("2. Játék kezdése");
            System.out.println("Kérem válasszon az alábbi menüpontok közül");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println("Top Játékosok ...");
                dbConnection.getTopPlayers(4);
            } else if (choice.equals("2")) {
                System.out.println("Játék kezdése ...");
                startGame();
                break;
            } else {
                System.out.println("");
            }
        }
    }

    public void startGame() {
        System.out.print("Játékos 1 neve: ");
        String playerOneName = scanner.nextLine();
        System.out.print("Játékos 2 neve: ");
        String playerTwoName = scanner.nextLine();

        GameBoard board = new GameBoard(ROWS, COLS);
        char currentPlayerSymbol = PLAYER_ONE_SYMBOL;
        String currentPlayerName = playerOneName;

        boolean gameRunning = true;
        System.out.println("Válassz oszlopot 0-" + (COLS - 1) + " között!");

        while (gameRunning) {
            board.printBoard();

            if (board.isBoardFull()) {
                System.out.println("A játék döntetlen!");
                break;
            }

            int column = getPlayerMove(currentPlayerName, currentPlayerSymbol);
            String result = board.dropPiece(column, currentPlayerSymbol);

            if (!result.equals("OK")) {
                System.out.println(result);
                continue;
            }

            if (board.checkWin(currentPlayerSymbol)) {
                board.printBoard();
                System.out.println("Gratulálok! " + currentPlayerName + " nyert!");
                dbConnection.saveWinner(currentPlayerName); // Uses the injected instance
                gameRunning = false;
            } else {
                // Switch to the other player
                if (currentPlayerSymbol == PLAYER_ONE_SYMBOL) {
                    currentPlayerSymbol = PLAYER_TWO_SYMBOL;
                    currentPlayerName = playerTwoName;
                } else {
                    currentPlayerSymbol = PLAYER_ONE_SYMBOL;
                    currentPlayerName = playerOneName;
                }
            }
        }

        System.out.println("Köszönjük a játékot!");
        scanner.close();
    }

    private int getPlayerMove(String playerName, char playerSymbol) {
        while (true) {
            System.out.println(playerName + " (" + playerSymbol + "), válassz oszlopot (0-" + (COLS - 1) + "): ");
            try {
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    System.out.println("Üres bemenet! Kérlek adj meg egy számot!");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Érvénytelen bemenet! Kérlek adj meg egy számot!");
            }
        }
    }
}