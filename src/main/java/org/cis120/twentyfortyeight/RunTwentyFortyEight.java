package org.cis120.twentyfortyeight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunTwentyFortyEight implements Runnable {
    final static JFrame FRAME = new JFrame("2048");

    public void run() {
        // Top-level frame in which game components live
        FRAME.setResizable(false);
        FRAME.setLocation(0, 0);
        FRAME.setSize(new Dimension(600, 800));

        // ========================== Main Screen ==========================
        final JPanel mainScreen = new MainScreen("./files/img/startScreen.png");
        mainScreen.setPreferredSize(new Dimension(600, 600));

        // ========================== Instruction Screen ==========================
        final JPanel instructionScreen = new InstructionScreen("./files/img/instructionScreen.png");
        instructionScreen.setPreferredSize(new Dimension(600, 700));

        // ========================== Buttons ==========================
        final JButton instruction = new JButton(new ImageIcon("./files/img/instruction.png"));
        instruction.setPreferredSize(new Dimension(600, 100));

        final JButton newGame = new JButton(new ImageIcon("./files/img/newGame.png"));
        newGame.setPreferredSize(new Dimension(300, 100));

        final JButton load = new JButton(new ImageIcon("./files/img/load.png"));
        load.setPreferredSize(new Dimension(300, 100));

        final JButton back = new JButton(new ImageIcon("./files/img/back.png"));
        load.setPreferredSize(new Dimension(300, 100));

        // ========================== Menu Bar ==========================
        final JPanel menuBar = new JPanel();
        menuBar.setPreferredSize(new Dimension(600, 200));
        menuBar.setLayout(new BorderLayout());
        menuBar.add(instruction, BorderLayout.CENTER);

        final JPanel subMenuBar = new JPanel();
        subMenuBar.setLayout(new BorderLayout());
        subMenuBar.add(newGame, BorderLayout.WEST);
        subMenuBar.add(load, BorderLayout.EAST);

        menuBar.add(subMenuBar, BorderLayout.SOUTH);

        FRAME.add(mainScreen, BorderLayout.CENTER);
        FRAME.add(menuBar, BorderLayout.SOUTH);

        // ========================== Game Menu panel ==========================
        final JPanel score_panel = new JPanel();
        // frame.add(score_panel, BorderLayout.SOUTH);
        final JLabel score = new JLabel("Score: 0");
        score_panel.add(score);

        final JButton undo = new JButton("undo");

        final JPanel gameMenuBar = new JPanel();
        gameMenuBar.setPreferredSize(new Dimension(600, 200));
        gameMenuBar.setLayout(new BorderLayout());
        gameMenuBar.add(score_panel, BorderLayout.WEST);
        gameMenuBar.add(undo, BorderLayout.EAST);

        // ========================== Game Board ==========================
        final GameStateManager gsm = new GameStateManager(score);

        // ========================== Initiating Buttons ==========================

        // Back
        instruction.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                FRAME.add(instructionScreen, BorderLayout.CENTER);
                FRAME.add(back, BorderLayout.SOUTH);

                mainScreen.setVisible(false);
                menuBar.setVisible(false);

                FRAME.requestFocusInWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                instruction.setIcon(new ImageIcon("./files/img/instructionClicked.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                instruction.setIcon(new ImageIcon("./files/img/instruction.png"));
            }
        });

        // Back
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                FRAME.remove(instructionScreen);
                FRAME.remove(back);

                mainScreen.setVisible(true);
                menuBar.setVisible(true);

                FRAME.requestFocusInWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                back.setIcon(new ImageIcon("./files/img/backClicked.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                back.setIcon(new ImageIcon("./files/img/back.png"));
            }
        });

        // New Game
        newGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mainScreen.setVisible(false);
                menuBar.setVisible(false);

                FRAME.add(gsm, BorderLayout.CENTER);
                FRAME.add(gameMenuBar, BorderLayout.SOUTH);

                gsm.requestFocusInWindow();

                gsm.setGameStarted(true);

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                newGame.setIcon(new ImageIcon("./files/img/newGameClicked.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                newGame.setIcon(new ImageIcon("./files/img/newGame.png"));
            }
        });

        // Load
        load.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (gsm.isLoadable()) {
                    gsm.load();

                    mainScreen.setVisible(false);
                    menuBar.setVisible(false);

                    FRAME.add(gsm, BorderLayout.CENTER);
                    FRAME.add(gameMenuBar, BorderLayout.SOUTH);

                    gsm.setGameStarted(true);
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "There does not exist a save file or the save file has been corrupted."
                                    +
                                    "\n Please start a new game."
                    );
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                load.setIcon(new ImageIcon("./files/img/loadClicked.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                load.setIcon(new ImageIcon("./files/img/load.png"));
            }
        });

        // Undo
        undo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                gsm.undo();
            }
        });

        // ========================== On Closing ==========================
        FRAME.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (gsm.isGameStarted()) {
                    if (!gsm.isGameWon()) {
                        gsm.save();
                    }

                }
            }
        });

        // Put the frame on the screen
        FRAME.pack();
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setVisible(true);

    }
}