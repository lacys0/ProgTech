package org.progTech;

import java.util.Scanner;

public class Connect4Game {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char PLAYER_ONE_SYMBOL = 'X';
    private static final char PLAYER_TWO_SYMBOL = 'O';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DatabaseConnection dbConnection = new DatabaseConnection();

        System.out.println("Üdvözöllek a Connect 4 játékban!");
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

            int column = getPlayerMove(scanner, currentPlayerName, currentPlayerSymbol);
            String result = board.dropPiece(column, currentPlayerSymbol);

            if (!result.equals("OK")) {
                System.out.println(result);
                continue;
            }

            if (board.checkWin(currentPlayerSymbol)) {
                board.printBoard();
                System.out.println("Gratulálok! " + currentPlayerName + " nyert!");
                dbConnection.saveWinner(currentPlayerName);
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

    // Changed visibility to public to make it accessible from tests
    public static int getPlayerMove(Scanner scanner, String playerName, char playerSymbol) {
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
