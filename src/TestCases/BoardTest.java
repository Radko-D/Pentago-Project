package TestCases;

import GameLogic.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A JUNIT to test the most important game functions
 */
public class BoardTest {
    private Board board;

    /**
     * Creates a fresh board before every test
     */
    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    /**
     * Tests if the positions exists
     * The positions in the test are random preselected positions
     */
    @Test
    public void testPosition() {
        assertEquals(0, board.pos(0, 0));
        assertEquals(1, board.pos(1, 0));
        assertEquals(6, board.pos(0, 1));
        assertEquals(14, board.pos(2, 2));
        assertEquals(21, board.pos(3, 3));
        assertEquals(4, board.pos(4, 0));
        assertEquals(10, board.pos(4, 1));
    }

    /**
     * Tests if the isEmpty() method works properly
     * Test if the initial output of the method is True
     * Then markers are put on some empty spots and should return False
     */
    @Test
    public void testIsEmpty() {
        assertTrue(board.isEmpty(3));
        assertTrue(board.isEmpty(5));
        board.placeMark(3, 2, 1);
        assertFalse(board.isEmpty(15));
        assertTrue(board.isEmpty(3));
        board.placeMark(21, 7, 2);
        assertFalse(board.isEmpty(23));
        assertTrue(board.isEmpty(2));

    }

    /**
     * Tests if the rotation of the subboards works
     * First markers are placed on some of the spots and after the rotations are done
     * the new positions are checked if they are empty and should return False
     * The test is made for all possible rotations (clockwise and counter-clockwise)
     */
    @Test
    public void testRotation() {
        board.placeMark(0, 0, 1);
        board.placeMark(3, 2, 1);
        board.placeMark(18, 4, 1);
        board.placeMark(21, 6, 1);
        assertFalse(board.isEmpty(12));
        assertFalse(board.isEmpty(15));
        assertFalse(board.isEmpty(30));
        assertFalse(board.isEmpty(33));
        board.reset();
        board.placeMark(0, 1, 1);
        board.placeMark(3, 3, 1);
        board.placeMark(18, 5, 1);
        board.placeMark(21, 7, 1);
        assertFalse(board.isEmpty(2));
        assertFalse(board.isEmpty(5));
        assertFalse(board.isEmpty(20));
        assertFalse(board.isEmpty(23));
    }

    /**
     * Tests if the get() method works
     * Using the method we check if the spots exist
     * If it exists the command returns 0 and if doesn't the return is -1
     */
    @Test
    public void testGet() {
        assertEquals(-1, board.get(-1, 0));
        assertEquals(-1, board.get(0, -1));
        assertEquals(0, board.get(0, 0));
        assertEquals(0, board.get(2, 2));
        assertEquals(0, board.get(2, 3));
        assertEquals(0, board.get(3, 2));
    }

    /**
     * Test if the reset() method works
     * Markers are placed in certain spots
     * Afterwards the board is reset and using isEmpty the previously filled spots are checked if they are now empty
     */
    @Test
    public void testReset() {
        board.placeMark(0, 7, 1);
        board.placeMark(1, 7, 2);
        board.placeMark(2, 7, 1);
        board.placeMark(5, 7, 2);
        board.reset();
        assertTrue(board.isEmpty(0));
        assertTrue(board.isEmpty(1));
        assertTrue(board.isEmpty(1));
        assertTrue(board.isEmpty(5));
    }

    @Test
    public void testGameOverAndHasNoWinnerFullBoard() {
        /**
         * Tests if the gameover() and isFull() methods work for a draw when the board is full
         * Markers are placed in every single spot in a way that there is no winner
         */
        board.placeMark(22, 7, 1);
        board.placeMark(12, 2, 2);
        board.placeMark(22, 5, 1);
        board.placeMark(23, 1, 2);
        board.placeMark(26, 0, 1);
        board.placeMark(28, 4, 2);

        board.placeMark(34, 0, 1);
        board.placeMark(25, 5, 2);
        board.placeMark(19, 2, 1);
        board.placeMark(15, 0, 2);
        board.placeMark(10, 5, 1);
        board.placeMark(6, 4, 2);

        board.placeMark(32, 4, 1);
        board.placeMark(31, 7, 2);
        board.placeMark(30, 0, 1);
        board.placeMark(26, 7, 2);
        board.placeMark(29, 0, 1);
        board.placeMark(5, 4, 2);

        board.placeMark(9, 0, 1);
        board.placeMark(11, 4, 2);
        board.placeMark(16, 5, 1);
        board.placeMark(12, 6, 2);
        board.placeMark(7, 3, 1);
        board.placeMark(21, 6, 2);

        board.placeMark(15, 0, 1);
        board.placeMark(5, 0, 2);
        board.placeMark(35, 1, 2);
        board.placeMark(0, 0, 1);
        board.placeMark(21, 5, 2);
        board.placeMark(8, 6, 1);

        board.placeMark(1, 2, 2);
        board.placeMark(6, 6, 1);
        board.placeMark(4, 6, 2);
        board.placeMark(32, 2, 1);
        board.placeMark(18, 2, 2);

        assertFalse(board.gameOver());
        assertFalse(board.hasWinner());
        board.placeMark(14, 0, 2);
        assertTrue(board.gameOver());
        assertFalse(board.hasWinner());
        assertTrue(board.isFull());
        assertTrue(board.Draw());
    }

    /**
     * Tests if the game can be won with 5 marks in a column
     * Four marks are placed in a column and the hasWinner method checks if the game has a winner should be False at first
     * Afterwards a fifth marker is placed which decides a winner
     * and checks again if there is a winner using hasWinner()
     * The result should be True
     */
    @Test
    public void testHasWinnerColumn() {

        board.placeMark(0, 7, 1);
        board.placeMark(6, 7, 1);
        board.placeMark(12, 7, 1);
        board.placeMark(18, 7, 1);
        assertFalse(board.hasWinner());
        board.placeMark(24, 7, 1);
        assertTrue(board.hasWinner());
    }

    /**
     * Tests if the game can be won with 5 marks in a column
     * Four marks are placed in a diagonal and the hasWinner method checks if the game has a winner should be False at first
     * Afterwards a fifth marker is placed which decides a winner
     * and checks again if there is a winner using hasWinner()
     * The result should be True
     */

    @Test
    public void testHasWinnerDiagonal() {

        board.placeMark(0, 2, 1);
        board.placeMark(7, 2, 1);
        board.placeMark(14, 2, 1);
        board.placeMark(21, 2, 1);
        assertFalse(board.hasWinner());
        board.placeMark(28, 2, 1);
        assertTrue(board.hasWinner());

    }

    /**
     * Tests if the game can be won with 5 marks in a column
     * Four marks are placed in a line and the hasWinner method checks if the game has a winner should be False at first
     * Afterwards a fifth marker is placed which decides a winner
     * and checks again if there is a winner using hasWinner()
     * The result should be True
     */

    @Test
    public void testHasWinnerHorizontal() {
        board.placeMark(0, 7, 1);
        board.placeMark(1, 7, 1);
        board.placeMark(2, 7, 1);
        board.placeMark(3, 7, 1);
        assertFalse(board.hasWinner());
        board.placeMark(4, 7, 1);
        assertTrue(board.hasWinner());
    }
}