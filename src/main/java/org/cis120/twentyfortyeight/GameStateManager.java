package org.cis120.twentyfortyeight;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class GameStateManager extends JPanel {

    private Matrix mat; // model for the game
    private int[][] matrix;
    private List<Map.Entry<int[][], Integer>> load;

    private JLabel scoreLabel; // current score text

    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 600;

    // path and name for the save file
    private final String savePath = "./files/save/saveFile.txt";

    private boolean isGameStarted = false;
    private boolean isGameWon = false;

    private Set<Integer> allowedKeys;
    private Set<Integer> allowedNumbers;

    public GameStateManager(JLabel scoreInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setFocusable(true);

        mat = new Matrix(); // initializes model for the game

        matrix = mat.getMatrix();

        load = null;

        scoreLabel = scoreInit; // initializes the status JLabel

        // we want only arrow key inputs
        allowedKeys = new TreeSet<>();
        allowedKeys.add(KeyEvent.VK_RIGHT);
        allowedKeys.add(KeyEvent.VK_LEFT);
        allowedKeys.add(KeyEvent.VK_UP);
        allowedKeys.add(KeyEvent.VK_DOWN);

        // the valid numbers
        allowedNumbers = new TreeSet<>();
        allowedNumbers.add(0);
        allowedNumbers.add(2);
        allowedNumbers.add(4);
        allowedNumbers.add(8);
        allowedNumbers.add(16);
        allowedNumbers.add(32);
        allowedNumbers.add(64);
        allowedNumbers.add(128);
        allowedNumbers.add(256);
        allowedNumbers.add(512);
        allowedNumbers.add(1024);
        allowedNumbers.add(2048);

        addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                boolean flag = false; // flag to see if the KeyInput is valid

                int code = e.getKeyCode();
                if (allowedKeys.contains(code)) {
                    if (code == KeyEvent.VK_LEFT && mat.moveLeftValid()) {
                        mat.moveLeft();
                        flag = true;

                    } else if (code == KeyEvent.VK_RIGHT && mat.moveRightValid()) {
                        mat.moveRight();
                        flag = true;

                    } else if (code == KeyEvent.VK_DOWN && mat.moveDownValid()) {
                        mat.moveDown();
                        flag = true;
                    } else if (code == KeyEvent.VK_UP && mat.moveUpValid()) {
                        mat.moveUp();
                        flag = true;
                    }
                }

                // if the input was valid, add a new block
                if (flag) {
                    mat.addNewBlock();
                    mat.addHistory();
                }

                updateScore();
                repaint();

                // game end scenario
                if (mat.isGameOver()) {
                    // game win scenario
                    if (mat.isGameWon()) {
                        JOptionPane.showMessageDialog(null, "You Won!");
                        isGameWon = true;

                        /*
                         * Delete any save file from this game.
                         * Ensure that the player cannot load game for the next game
                         */
                        File saveFile = new File(savePath);
                        if (saveFile != null) {
                            saveFile.delete();
                        }

                        // close game
                        RunTwentyFortyEight.FRAME.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "You Lost!");
                        /*
                         * Delete any save file from this game.
                         * Ensure that the player cannot load game for the next game
                         */
                        File saveFile = new File(savePath);
                        if (saveFile != null) {
                            saveFile.delete();
                        }
                        RunTwentyFortyEight.FRAME.dispose();
                    }
                }
            }
        });
    }

    // ========================== Setters and Getters ==========================

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    // ========================== Functionality ==========================

    /**
     * Updates the scoreLabel to reflect the current score of the game.
     */
    private void updateScore() {
        scoreLabel.setText("Score: " + mat.getScore());
    }

    /**
     * Undo the move done by the player
     */
    public void undo() {
        mat.undo();
        updateScore();
        repaint();
        requestFocusInWindow();
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    // ========================== Save and Load ==========================

    /**
     * Write into the file the entire @mat.history.
     * Each Entry will take up 5 lines: 4 for the matrix and 1 for the score.
     * Sample txt file will look like this:
     * 0 0 0 2
     * 2 0 0 4
     * 0 0 0 0
     * 0 0 0 0
     * 2
     */
    public void save() {
        BufferedWriter bw = getBufferedWriter();
        List<Map.Entry<int[][], Integer>> history = mat.getHistory();
        try {
            for (Map.Entry<int[][], Integer> state : history) {
                int[][] matrix = state.getKey();
                int score = state.getValue();

                for (int[] ints : matrix) {
                    StringBuilder line = new StringBuilder();
                    for (int anInt : ints) {
                        line.append(anInt).append(" ");
                    }

                    bw.write(line.toString());
                    bw.newLine();
                }
                bw.write("" + score);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if 1) there exists a save file 2) it is valid and not corrupt.
     *
     * @return whether there exists a valid loadable save file.
     */
    public boolean isLoadable() {
        BufferedReader br = getBufferedReader();

        if (br == null) {
            return false;
        }

        int cnt = 0;
        StringBuilder rawData = new StringBuilder();
        String readLine;
        try {
            while ((readLine = br.readLine()) != null) {
                rawData.append(readLine).append("\n");
                cnt++;
            }

            // The save file cannot be empty since history is never empty
            if (cnt == 0) {
                System.out.println("empty file");
                return false;
            }

            // By the way @save() works, the length of the save file must be divisible by 5
            if (cnt % 5 != 0) {
                System.out.println("corrupt file");
                return false;
            }

            String[] lines = rawData.toString().split("\n");

            String[] sets = new String[lines.length / 5];
            StringBuilder set = new StringBuilder();
            cnt = 0;

            for (String line : lines) {
                set.append(line);
                cnt++;

                if (cnt % 5 == 0) {
                    sets[cnt / 5 - 1] = set.toString();
                    set = new StringBuilder();
                }

            }

            List<Map.Entry<int[][], Integer>> history = new LinkedList<>();

            for (String s : sets) {
                String[] singles = s.split(" ");
                // each Entry data must have 17 numbers: 16 for the matrix and 1 for the score.
                if (singles.length != 17) {
                    System.out.println("corrupt files / bad singles length");
                    return false;
                }

                int[][] tempMatrix = new int[4][4];
                int score = 0;

                for (int i = 0; i < singles.length; i++) {
                    try {
                        if (i == singles.length - 1) {
                            score = Integer.parseInt(singles[i]);
                            break;
                        }
                        int single = Integer.parseInt(singles[i]);
                        if (!allowedNumbers.contains(single)) {
                            System.out.println("invalid value (not 0, 2, 4 ... 2048)");
                            return false;
                        }
                        tempMatrix[i / 4][i % 4] = single;
                    } catch (NumberFormatException e) {
                        // Every data in the save file must be parsable to an int value
                        System.out.println("corrupt files / not number");
                        return false;
                    }
                }

                Map.Entry<int[][], Integer> entry = new AbstractMap.SimpleEntry<>(
                        tempMatrix,
                        score
                );
                history.add(entry);
            }

            load = history;

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * load the save file and alter the @mat accordingly.
     * Should be used after @isLoadable().
     *
     */
    public void load() {

        // ensure we have a valid data to load
        if (load == null) {
            System.out.println(
                    "There does not exist a save file or the save file has been corrupted."
            );
            return;
        }

        mat.setHistory(load);
        mat.setMatrix(load.get(load.size() - 1).getKey());
        matrix = mat.getMatrix();
        mat.setScore(load.get(load.size() - 1).getValue());

        updateScore();
        repaint();
        requestFocusInWindow();

    }

    /**
     * Creates and returns a BufferedWriter with the savePath
     *
     * @return BufferedWriter BufferedWriter with the location of the save file
     */
    private BufferedWriter getBufferedWriter() {
        FileWriter fw = null;

        try {
            File saveFile = new File(savePath);
            fw = new FileWriter(saveFile, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter bw = new BufferedWriter(fw);

        return bw;
    }

    /**
     * Creates and returns a BufferedReader with the savePath
     *
     * @return BufferedReader BufferedReader with the location of the save file
     */
    private BufferedReader getBufferedReader() {
        FileReader fr;

        try {
            File saveFile = new File(savePath);
            fr = new FileReader(saveFile);
        } catch (IOException e) {
            return null;
        }

        return new BufferedReader(fr);
    }

    // ========================== Draw Component ==========================

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        paintBoardGrid(g);

        //
        paintTiles(g);
    }

    private void paintBoardGrid(Graphics g) {
        g.drawLine(150, 0, 150, 600);
        g.drawLine(300, 0, 300, 600);
        g.drawLine(450, 0, 450, 600);

        g.drawLine(0, 150, 600, 150);
        g.drawLine(0, 300, 600, 300);
        g.drawLine(0, 450, 600, 450);
        g.drawLine(0, 600, 600, 600);
    }

    private void paintTiles(Graphics g) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int value = matrix[row][col];
                if (value != 0) {

                    BufferedImage bi = null;

                    try {
                        bi = ImageIO.read(new File("./files/tiles/tile" + value + ".png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Image i = bi.getScaledInstance(130, 130, Image.SCALE_FAST);

                    g.drawImage(i, 10 + 150 * col, 10 + 150 * (row), null);

                }
            }
        }
    }
}
