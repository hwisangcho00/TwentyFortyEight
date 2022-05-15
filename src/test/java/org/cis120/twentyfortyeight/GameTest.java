package org.cis120.twentyfortyeight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {
    private Matrix mat;
    private GameStateManager gsm;

    @BeforeEach
    public void setUp() {
        mat = new Matrix();
        gsm = new GameStateManager(new JLabel("Score: 0"));
    }

    // ========================== Move Matrix ==========================
    @Test
    public void testMoveUpFull() {
        int[][] fullBoard = {
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 }
        };

        mat.setMatrix(fullBoard);

        int[][] actual = mat.moveUp();
        int[][] expected = {
            { 32, 32, 32, 32 },
            { 32, 32, 32, 32 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }
        };

        assertTrue(Arrays.deepEquals(actual, expected));
    }

    @Test
    public void testMoveUpEdgeCases() {
        /*
         * 1st column: no merging
         * 2nd column: merging with 1 space between
         * 3rd column: merging with 2 spaces between
         * 4th column: 2 different merging
         */
        int[][] board = {
            { 16, 16, 32, 4 },
            { 8, 0, 0, 4 },
            { 4, 16, 0, 32 },
            { 2, 2, 32, 32 }
        };

        mat.setMatrix(board);

        int[][] actual = mat.moveUp();
        int[][] expected = {
            { 16, 32, 64, 8 },
            { 8, 2, 0, 64 },
            { 4, 0, 0, 0 },
            { 2, 0, 0, 0 }
        };

        assertTrue(Arrays.deepEquals(actual, expected));
    }

    @Test
    public void testMoveDownFull() {
        int[][] fullBoard = {
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 }
        };

        mat.setMatrix(fullBoard);

        int[][] actual = mat.moveDown();
        int[][] expected = {
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
            { 32, 32, 32, 32 },
            { 32, 32, 32, 32 }
        };

        assertTrue(Arrays.deepEquals(actual, expected));
    }

    @Test
    public void testMoveDownEdgeCases() {
        /*
         * 1st column: no merging
         * 2nd column: merging with 1 space between
         * 3rd column: merging with 2 spaces between
         * 4th column: 2 different merging
         */
        int[][] board = {
            { 16, 16, 32, 4 },
            { 8, 0, 0, 4 },
            { 4, 16, 0, 32 },
            { 2, 2, 32, 32 }
        };

        mat.setMatrix(board);

        int[][] actual = mat.moveDown();
        int[][] expected = {
            { 16, 0, 0, 0 },
            { 8, 0, 0, 0 },
            { 4, 32, 0, 8 },
            { 2, 2, 64, 64 }
        };

        assertTrue(Arrays.deepEquals(actual, expected));
    }

    @Test
    public void testMoveLeftFull() {
        int[][] fullBoard = {
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 }
        };

        mat.setMatrix(fullBoard);

        int[][] actual = mat.moveLeft();
        int[][] expected = {
            { 32, 32, 0, 0 },
            { 32, 32, 0, 0 },
            { 32, 32, 0, 0 },
            { 32, 32, 0, 0 }
        };

        assertTrue(Arrays.deepEquals(actual, expected));
    }

    @Test
    public void testMoveLeftEdgeCases() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 4, 16, 0, 32 },
            { 32, 0, 32, 4 },
            { 8, 0, 0, 8 },
            { 2, 2, 32, 32 }
        };

        mat.setMatrix(board);

        int[][] actual = mat.moveLeft();
        int[][] expected = {
            { 4, 16, 32, 0 },
            { 64, 4, 0, 0 },
            { 16, 0, 0, 0 },
            { 4, 64, 0, 0 }
        };

        assertTrue(Arrays.deepEquals(actual, expected));
    }

    @Test
    public void testMoveRightFull() {
        int[][] fullBoard = {
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 },
            { 16, 16, 16, 16 }
        };

        mat.setMatrix(fullBoard);

        int[][] actual = mat.moveRight();
        int[][] expected = {
            { 0, 0, 32, 32 },
            { 0, 0, 32, 32 },
            { 0, 0, 32, 32 },
            { 0, 0, 32, 32 },
        };

        assertTrue(Arrays.deepEquals(actual, expected));
    }

    @Test
    public void testMoveRightEdgeCases() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 4, 16, 0, 32 },
            { 32, 0, 32, 4 },
            { 8, 0, 0, 8 },
            { 2, 2, 32, 32 }
        };

        mat.setMatrix(board);

        int[][] actual = mat.moveRight();
        int[][] expected = {
            { 0, 4, 16, 32 },
            { 0, 0, 64, 4 },
            { 0, 0, 0, 16 },
            { 0, 0, 4, 64 }
        };

        assertTrue(Arrays.deepEquals(actual, expected));
    }

    // ========================== moveDIRECTIONValid() ==========================

    @Test
    public void testMoveRightValidFalse() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 0, 0, 0, 2 },
            { 0, 0, 0, 2 },
            { 0, 0, 0, 2 },
            { 0, 0, 0, 2 }
        };

        mat.setMatrix(board);

        assertFalse(mat.moveRightValid());
    }

    @Test
    public void testMoveRightValidTrue() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 16, 2, 2, 4 },
            { 8, 4, 512, 2 },
            { 32, 2, 32, 2 },
            { 16, 32, 4, 2 }
        };

        mat.setMatrix(board);

        assertTrue(mat.moveRightValid());
    }

    @Test
    public void testMoveLeftValidFalse() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 2, 4, 0, 0 },
            { 2, 4, 2, 0 },
            { 8, 4, 0, 0 },
            { 16, 4, 0, 0 }
        };

        mat.setMatrix(board);

        assertFalse(mat.moveLeftValid());
    }

    @Test
    public void testMoveLeftValidTrue() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 0, 0, 0, 2 },
            { 0, 0, 0, 2 },
            { 0, 0, 0, 2 },
            { 0, 0, 0, 2 }
        };

        mat.setMatrix(board);

        assertTrue(mat.moveLeftValid());
    }

    @Test
    public void testMoveUpValidFalse() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 2, 2, 2, 2 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }
        };

        mat.setMatrix(board);

        assertFalse(mat.moveUpValid());
    }

    @Test
    public void testMoveUpValidTrue() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 0, 0, 0, 2 },
            { 0, 0, 0, 2 },
            { 0, 0, 0, 2 },
            { 0, 0, 0, 2 }
        };

        mat.setMatrix(board);

        assertTrue(mat.moveUpValid());
    }

    @Test
    public void testMoveDownValidFalse() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 0, 4, 0, 0 },
            { 0, 8, 0, 0 },
            { 0, 4, 0, 16 },
            { 2, 2, 2, 2 }
        };

        mat.setMatrix(board);

        assertFalse(mat.moveDownValid());
    }

    @Test
    public void testMoveDownValidTrue() {
        /*
         * 1st row: no merging
         * 2nd row: merging with 1 space between
         * 3rd row: merging with 2 spaces between
         * 4th row: 2 different merging
         */
        int[][] board = {
            { 2, 0, 32, 2 },
            { 0, 0, 0, 2 },
            { 0, 16, 0, 4 },
            { 2, 0, 0, 2 }
        };

        mat.setMatrix(board);

        assertTrue(mat.moveDownValid());
    }

    // ========================== isGameOver() ==========================
    @Test
    public void testWinScenarioGameOver() {
        int[][] board = {
            { 4, 16, 0, 32 },
            { 1024, 1024, 32, 4 },
            { 8, 0, 0, 8 },
            { 2, 2, 32, 32 }
        };
        mat.setMatrix(board);
        mat.moveRight();

        assertTrue(mat.isGameOver());
        assertTrue(mat.isGameWon());
    }

    @Test
    public void testLoseScenarioGameOver() {
        int[][] board = {
            { 4, 16, 4, 16 },
            { 16, 4, 16, 4 },
            { 4, 16, 4, 16 },
            { 16, 4, 16, 4 }
        };
        mat.setMatrix(board);

        assertTrue(mat.isGameOver());
        assertFalse(mat.isGameWon());
    }

    @Test
    public void testWinScenarioGameOverEdgeCase() {
        int[][] board = {
            { 1024, 1024, 4, 16 },
            { 512, 4, 16, 4 },
            { 4, 16, 4, 16 },
            { 16, 4, 16, 4 }
        };
        mat.setMatrix(board);
        mat.moveRight();
        mat.addNewBlock();
        // Player has won & there is no moves left afterward
        // Still should count has a win
        assertTrue(mat.isGameOver());
        assertTrue(mat.isGameWon());
    }

    // ========================== addHistory() ==========================
    @Test
    public void testAddHistoryDefault() {
        List<Map.Entry<int[][], Integer>> history = mat.getHistory();

        assertFalse(history.isEmpty());
        assertEquals(1, history.size());
    }

    @Test
    public void testAddHistoryOneMove() {
        // Erase the default

        mat.getHistory().clear();

        int[][] board = {
            { 0, 0, 0, 0 },
            { 0, 4, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 2, 0 },
        };

        // newly set the default
        mat.setMatrix(board);
        mat.addHistory();

        mat.moveRight();
        mat.addHistory();

        List<Map.Entry<int[][], Integer>> history = mat.getHistory();

        int[][] expected = {
            { 0, 0, 0, 0 },
            { 0, 0, 0, 4 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 2 },
        };

        assertTrue(Arrays.deepEquals(expected, history.get(history.size() - 1).getKey()));
    }

    @Test
    public void testAddHistoryTwoMoves() {
        // Erase the default

        mat.getHistory().clear();

        int[][] board = {
            { 0, 0, 0, 0 },
            { 0, 4, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 2, 0 },
        };

        // newly set the default
        mat.setMatrix(board);
        mat.addHistory();

        mat.moveRight();
        mat.addHistory();

        mat.moveUp();
        mat.addHistory();

        List<Map.Entry<int[][], Integer>> history = mat.getHistory();

        int[][] expected = {
            { 0, 0, 0, 4 },
            { 0, 0, 0, 2 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
        };

        assertTrue(Arrays.deepEquals(expected, history.get(history.size() - 1).getKey()));
    }

    // ========================== undo() ==========================
    @Test
    public void testUndoDefault() {
        // Erase the default

        mat.getHistory().clear();

        int[][] board = {
            { 0, 0, 0, 0 },
            { 0, 4, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 2, 0 },
        };

        // newly set the default
        mat.setMatrix(board);
        mat.addHistory();

        mat.undo();

        List<Map.Entry<int[][], Integer>> history = mat.getHistory();

        int[][] expected = {
            { 0, 0, 0, 0 },
            { 0, 4, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 2, 0 },
        };

        assertEquals(1, history.size());
        assertTrue(Arrays.deepEquals(expected, history.get(history.size() - 1).getKey()));
    }

    @Test
    public void testUndoOneMove() {
        // Erase the default

        mat.getHistory().clear();

        int[][] board = {
            { 0, 0, 0, 0 },
            { 0, 4, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 2, 0 },
        };

        // newly set the default
        mat.setMatrix(board);
        mat.addHistory();

        mat.moveRight();
        mat.addHistory();

        mat.undo();

        List<Map.Entry<int[][], Integer>> history = mat.getHistory();

        int[][] expected = {
            { 0, 0, 0, 0 },
            { 0, 4, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 2, 0 },
        };

        // mat.matrix back to normal
        assertTrue(Arrays.deepEquals(expected, mat.getMatrix()));

        assertEquals(1, history.size());

        // History removed
        assertTrue(Arrays.deepEquals(expected, history.get(history.size() - 1).getKey()));
    }

    @Test
    public void testUndoTwoMoves() {
        // Erase the default

        mat.getHistory().clear();

        int[][] board = {
            { 0, 0, 0, 0 },
            { 0, 4, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 4, 0 },
        };

        // newly set the default
        mat.setMatrix(board);
        mat.addHistory();

        mat.moveRight();
        mat.addHistory();

        mat.moveUp();
        mat.addHistory();

        mat.undo();

        List<Map.Entry<int[][], Integer>> history = mat.getHistory();

        int[][] expected = {
            { 0, 0, 0, 0 },
            { 0, 0, 0, 4 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 4 },
        };

        assertTrue(Arrays.deepEquals(expected, mat.getMatrix()));

        assertEquals(2, history.size());

        assertTrue(Arrays.deepEquals(expected, history.get(history.size() - 1).getKey()));
        // Score is not 4
        assertEquals(mat.getScore(), history.get(history.size() - 1).getValue());
    }
}
