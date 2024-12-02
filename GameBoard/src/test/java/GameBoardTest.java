import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.progTech.GameBoard;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(6, 7); // Standard Connect 4 board
    }

    @Test
    void testInitializeBoard() {
        char[][] board = getBoard();
        for (char[] row : board) {
            for (char cell : row) {
                assertEquals('-', cell, "Board should be initialized with empty cells");
            }
        }
    }

    @Test
    void testPrintBoard() {
        // Not much to test here unless using mocks or capturing console output
        assertDoesNotThrow(() -> gameBoard.printBoard());
    }

    @Test
    void testDropPieceValid() {
        assertEquals("OK", gameBoard.dropPiece(0, 'X'), "Dropping a piece in a valid column should succeed");
        assertEquals('X', getBoard()[5][0], "Piece should be placed in the bottom-most row of the column");
    }

    @Test
    void testDropPieceFullColumn() {
        for (int i = 0; i < 6; i++) {
            assertEquals("OK", gameBoard.dropPiece(0, 'X'));
        }
        assertEquals("Ez az oszlop már tele van!", gameBoard.dropPiece(0, 'X'), "Should not allow adding to a full column");
    }

    @Test
    void testDropPieceInvalidColumn() {
        assertEquals("Érvénytelen oszlop! Válassz 0 és 6 között!", gameBoard.dropPiece(-1, 'X'));
        assertEquals("Érvénytelen oszlop! Válassz 0 és 6 között!", gameBoard.dropPiece(7, 'X'));
    }

    @Test
    void testCheckHorizontalWin() {
        for (int i = 0; i < 4; i++) {
            gameBoard.dropPiece(i, 'X');
        }
        assertTrue(gameBoard.checkWin('X'), "Player X should win with a horizontal line");
    }

    @Test
    void testCheckVerticalWin() {
        for (int i = 0; i < 4; i++) {
            gameBoard.dropPiece(0, 'X');
        }
        assertTrue(gameBoard.checkWin('X'), "Player X should win with a vertical line");
    }

    @Test
    void testCheckDiagonalWinLeftToRight() {
        gameBoard.dropPiece(0, 'X');
        gameBoard.dropPiece(1, 'O');
        gameBoard.dropPiece(1, 'X');
        gameBoard.dropPiece(2, 'O');
        gameBoard.dropPiece(2, 'O');
        gameBoard.dropPiece(2, 'X');
        gameBoard.dropPiece(3, 'O');
        gameBoard.dropPiece(3, 'O');
        gameBoard.dropPiece(3, 'O');
        gameBoard.dropPiece(3, 'X');

        assertTrue(gameBoard.checkWin('X'), "Player X should win with a diagonal line (left to right)");
    }

    @Test
    void testCheckDiagonalWinRightToLeft() {
        gameBoard.dropPiece(3, 'X');
        gameBoard.dropPiece(2, 'O');
        gameBoard.dropPiece(2, 'X');
        gameBoard.dropPiece(1, 'O');
        gameBoard.dropPiece(1, 'O');
        gameBoard.dropPiece(1, 'X');
        gameBoard.dropPiece(0, 'O');
        gameBoard.dropPiece(0, 'O');
        gameBoard.dropPiece(0, 'O');
        gameBoard.dropPiece(0, 'X');

        assertTrue(gameBoard.checkWin('X'), "Player X should win with a diagonal line (right to left)");
    }

    @Test
    void testIsBoardFull() {
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                assertEquals("OK", gameBoard.dropPiece(col, 'X'));
            }
        }
        assertTrue(gameBoard.isBoardFull(), "Board should be full after all cells are filled");
    }

    @Test
    void testIsBoardNotFull() {
        assertFalse(gameBoard.isBoardFull(), "Board should not be full at the start");
        gameBoard.dropPiece(0, 'X');
        assertFalse(gameBoard.isBoardFull(), "Board should not be full after one piece is dropped");
    }

    // Helper method to access private board field
    private char[][] getBoard() {
        try {
            var field = GameBoard.class.getDeclaredField("board");
            field.setAccessible(true);
            return (char[][]) field.get(gameBoard);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access board field", e);
        }
    }
}
