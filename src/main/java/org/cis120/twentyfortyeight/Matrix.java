package org.cis120.twentyfortyeight;

import java.util.*;

public class Matrix {

    private int[][] matrix;
    private Random r = new Random();

    private int score;
    private List<Map.Entry<int[][], Integer>> history;
    private final int targetScore = 2048;
    private final int row = 4;
    private final int col = 4;
    private boolean isGameWon;

    public Matrix() {
        // initiate @matrix
        matrix = new int[row][col];

        // default score 0
        score = 0;

        isGameWon = false;

        history = new LinkedList<>();

        // add two random blocks to @matrix
        addNewBlock();
        addNewBlock();

        // add the first default @matrix into @history
        addHistory();
    }

    // ========================== Getters and Setters ==========================

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = copy(matrix);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Map.Entry<int[][], Integer>> getHistory() {
        return history;
    }

    public void setHistory(List<Map.Entry<int[][], Integer>> history) {
        this.history = history;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    // ========================== Public Methods ==========================

    /**
     * Check if the game is over or not. Game is over when the player either wins or
     * loses.
     *
     * @return Whether the game is over or not.
     */
    public boolean isGameOver() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                // if any of the tile has value bigger than the TARGET_SCORE
                if (matrix[i][j] >= targetScore) {
                    // player won
                    return isGameWon = true;
                }
            }
        }
        /*
         * Since the player has not won the game yet, the player either lost or the game
         * is still not over.
         * True (game is over) if player lost, or false (game is not over) if player has
         * not yet lost.
         */
        return isGameLost();
    }

    /**
     * Add a new block to the @matrix
     */
    public void addNewBlock() {

        // Ensure that the board is not full.
        if (isBoardFull()) {
            return;
        }

        int row = r.nextInt(4);
        int col = r.nextInt(4);

        // while loop until matrix[row][col] is empty
        while (matrix[row][col] != 0) {
            row = r.nextInt(4);
            col = r.nextInt(4);
        }

        matrix[row][col] = getTwoOrFour();
    }

    /**
     * Add a new Entry to the history.
     */
    public void addHistory() {
        // Use copy() to ensure encapsulation
        Map.Entry m = new AbstractMap.SimpleEntry(copy(matrix), score);
        history.add(m);
    }

    public boolean moveRightValid() {
        return !matrixEquals(moveRight(copy(matrix)), matrix);
    }

    public boolean moveLeftValid() {
        return !matrixEquals(moveLeft(copy(matrix)), matrix);
    }

    public boolean moveUpValid() {
        return !matrixEquals(moveUp(copy(matrix)), matrix);
    }

    public boolean moveDownValid() {
        return !matrixEquals(moveDown(copy(matrix)), matrix);
    }

    /**
     * Update the matrix, score, and history back to the most recent game state
     * before the user input.
     */
    public void undo() {

        // History size is 1 as default (through constructor). This indicates no move
        // has been made yet.
        if (history.size() < 2) {
            return;
        }

        /*
         * We get the second most recent history since the most recent Entry in @history
         * is the current state
         * saved by the update() method.
         */
        Map.Entry<int[][], Integer> past = history.get(history.size() - 2);

        // Directly update the @matrix
        for (int i = 0; i < row; i++) {
            System.arraycopy(past.getKey()[i], 0, matrix[i], 0, col);
        }

        // Set the @score
        setScore(past.getValue());

        // remove the most recent (current) game state from the history that has been
        // rolled back.
        history.remove(history.get(history.size() - 1));

    }

    /**
     * I have overloaded moveDIRECTION() methods to be used in different scenarios.
     * moveDIRECTION() : directly makes change to @matrix and return @matrix
     * moveDIRECTION(int[][] matrix) : make change to @param matrix and return the
     * altered @param matrix
     *
     * This is to ensure encapsulation of @matrix when we are using methods such as
     * isGameLost()
     *
     * All moveDIRECTION() have identical structure of 1) aligning the tiles to get
     * rid of in between empty tiles
     * 2) merging 3) again aligning the tiles to get rid of in between empty tiles.
     *
     */

    public int[][] moveRight() {
        return moveRight(matrix);
    }

    private int[][] moveRight(int[][] matrix) {
        for (int row = 0; row < 4; row++) {

            for (int col = 2; col >= 0; col--) {
                int cur = col;
                if (matrix[row][col] != 0) {
                    while (cur < 3 && matrix[row][cur + 1] == 0) {
                        int temp = matrix[row][cur];
                        matrix[row][cur] = 0;
                        matrix[row][cur + 1] = temp;
                        cur++;
                    }
                }
            }

            for (int col = 3; col > 0; col--) {
                if (matrix[row][col] == matrix[row][col - 1]) {
                    if (this.matrix == matrix) {
                        score += matrix[row][col];
                    }
                    matrix[row][col] *= 2;
                    matrix[row][col - 1] = 0;
                    col--;
                }
            }

            for (int col = 2; col >= 0; col--) {
                int cur = col;
                if (matrix[row][col] != 0) {
                    while (cur < 3 && matrix[row][cur + 1] == 0) {
                        int temp = matrix[row][cur];
                        matrix[row][cur] = 0;
                        matrix[row][cur + 1] = temp;
                        cur++;
                    }
                }
            }
        }

        return matrix;
    }

    public int[][] moveLeft() {
        return moveLeft(matrix);
    }

    private int[][] moveLeft(int[][] matrix) {
        for (int row = 0; row < 4; row++) {

            for (int col = 1; col < 4; col++) {
                int cur = col;
                if (matrix[row][col] != 0) {
                    while (cur > 0 && matrix[row][cur - 1] == 0) {
                        int temp = matrix[row][cur];
                        matrix[row][cur] = 0;
                        matrix[row][cur - 1] = temp;
                        cur--;
                    }
                }
            }

            for (int col = 0; col < 3; col++) {
                if (matrix[row][col] == matrix[row][col + 1]) {
                    if (this.matrix == matrix) {
                        score += matrix[row][col];
                    }
                    matrix[row][col] *= 2;
                    matrix[row][col + 1] = 0;
                    col++;
                }
            }

            for (int col = 1; col < 4; col++) {
                int cur = col;
                if (matrix[row][col] != 0) {
                    while (cur > 0 && matrix[row][cur - 1] == 0) {
                        int temp = matrix[row][cur];
                        matrix[row][cur] = 0;
                        matrix[row][cur - 1] = temp;
                        cur--;
                    }
                }
            }
        }
        return matrix;
    }

    public int[][] moveUp() {
        return moveUp(matrix);

    }

    private int[][] moveUp(int[][] matrix) {
        for (int col = 0; col < 4; col++) {
            for (int row = 1; row < 4; row++) {
                int cur = row;
                if (matrix[row][col] != 0) {
                    while (cur > 0 && matrix[cur - 1][col] == 0) {
                        int temp = matrix[cur][col];
                        matrix[cur][col] = 0;
                        matrix[cur - 1][col] = temp;
                        cur--;
                    }
                }
            }

            for (int row = 0; row < 3; row++) {
                if (matrix[row][col] == matrix[row + 1][col]) {
                    if (this.matrix == matrix) {
                        score += matrix[row][col];
                    }
                    matrix[row][col] *= 2;
                    matrix[row + 1][col] = 0;
                    row++;
                }
            }

            for (int row = 1; row < 4; row++) {
                int cur = row;
                if (matrix[row][col] != 0) {
                    while (cur > 0 && matrix[cur - 1][col] == 0) {
                        int temp = matrix[cur][col];
                        matrix[cur][col] = 0;
                        matrix[cur - 1][col] = temp;
                        cur--;
                    }
                }
            }
        }
        return matrix;
    }

    public int[][] moveDown() {
        return moveDown(matrix);

    }

    private int[][] moveDown(int[][] matrix) {
        for (int col = 0; col < 4; col++) {
            for (int row = 2; row >= 0; row--) {
                int cur = row;
                if (matrix[row][col] != 0) {
                    while (cur < 3 && matrix[cur + 1][col] == 0) {
                        int temp = matrix[cur][col];
                        matrix[cur][col] = 0;
                        matrix[cur + 1][col] = temp;
                        cur++;
                    }
                }
            }

            for (int row = 3; row > 0; row--) {
                if (matrix[row][col] == matrix[row - 1][col]) {
                    if (this.matrix == matrix) {
                        score += matrix[row][col];
                    }
                    matrix[row][col] *= 2;
                    matrix[row - 1][col] = 0;
                    row--;
                }
            }

            for (int row = 2; row >= 0; row--) {
                int cur = row;
                if (matrix[row][col] != 0) {
                    while (cur < 3 && matrix[cur + 1][col] == 0) {
                        int temp = matrix[cur][col];
                        matrix[cur][col] = 0;
                        matrix[cur + 1][col] = temp;
                        cur++;
                    }
                }
            }
        }
        return matrix;
    }

    // ========================== Helper Methods ==========================

    /**
     * Returns an int value 2 with 90% probability and 4 with 10% probability
     *
     * @return int value of either 2 or 4
     */
    private int getTwoOrFour() {
        return r.nextDouble() > 0.1 ? 2 : 4;
    }

    /**
     * Observe if @matrix is full of tiles
     *
     * @return Whether @matrix is full or not.
     */
    private boolean isBoardFull() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                // If there exists an empty tile, return false
                if (matrix[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates and returns a shallow copy of the input parameter.
     *
     * @param original int 2d array that we want to create a copy of
     * @return Shallow copied int 2d array
     */
    private int[][] copy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    /**
     * Structurally compare the two int 2d arrays by seeing if each element is the
     * same
     *
     * @param m1 first int 2d array
     * @param m2 second int 2d array
     * @return Whether the two parameters are structurally equal
     */

    private boolean matrixEquals(int[][] m1, int[][] m2) {

        if (m1.length != m2.length) {
            return false;
        }

        boolean result = true;

        for (int i = 0; i < m1.length; i++) {
            result = result && Arrays.equals(m1[i], m2[i]);
        }

        return result;
    }

    /**
     * Observe whether @matrix has any more possible moves.
     * If none of up, down, left, right key input makes a change in @matrix, we know
     * that there are no moves left.
     *
     * @return Whether there are more moves left for @matrix
     */
    private boolean isGameLost() {
        // if all result indicates that no change has occurred, it means that the player
        // has lost.
        return !(moveRightValid() || moveLeftValid() || moveUpValid() || moveDownValid());
    }

    // ========================== Testing Methods ==========================

    /**
     * Prints the int 2d array into form like a matrix.
     * Used for testing and debugging purposes.
     *
     * @param matrix int 2d array we want to print in console
     */
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("======================");
    }

}
