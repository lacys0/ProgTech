package org.progTech;

public class GameBoard {
    private final int rows;
    private final int cols;
    private final char[][] board;
    private static final char EMPTY_CELL = '-';

    public GameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new char[rows][cols];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
    }

    public void printBoard() {
        // Oszlopszámok kiírása
        for (int j = 0; j < cols; j++) {
            System.out.print(j + " ");
        }
        System.out.println();

        // Tábla kiírása
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public String dropPiece(int col, char symbol) {
        if (col < 0 || col >= cols) {
            return "Érvénytelen oszlop! Válassz 0 és " + (cols-1) + " között!";
        }

        // Ellenőrizzük, hogy az oszlop tele van-e
        if (board[0][col] != EMPTY_CELL) {
            return "Ez az oszlop már tele van!";
        }

        // Megkeressük az első üres helyet alulról
        for (int row = rows - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY_CELL) {
                board[row][col] = symbol;
                return "OK";
            }
        }

        return "Ismeretlen hiba történt!";
    }

    public boolean checkWin(char symbol) {
        return checkHorizontalWin(symbol) ||
                checkVerticalWin(symbol) ||
                checkDiagonalWin(symbol);
    }

    private boolean checkHorizontalWin(char symbol) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                if (checkLine(row, col, 0, 1, symbol, 4)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkVerticalWin(char symbol) {
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row <= rows - 4; row++) {
                if (checkLine(row, col, 1, 0, symbol, 4)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonalWin(char symbol) {
        // Balról jobbra átló
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                if (checkLine(row, col, 1, 1, symbol, 4)) {
                    return true;
                }
            }
        }

        // Jobbról balra átló
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = cols - 1; col >= 3; col--) {
                if (checkLine(row, col, 1, -1, symbol, 4)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkLine(int startRow, int startCol, int deltaRow, int deltaCol, char symbol, int count) {
        for (int i = 0; i < count; i++) {
            if (board[startRow + i * deltaRow][startCol + i * deltaCol] != symbol) {
                return false;
            }
        }
        return true;
    }

    public boolean isBoardFull() {
        for (int col = 0; col < cols; col++) {
            if (board[0][col] == EMPTY_CELL) {
                return false;
            }
        }
        return true;
    }
}

