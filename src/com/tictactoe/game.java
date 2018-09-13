package com.tictactoe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Main game class, created as "com.tictactoe.game".
public class game {

    // Game Image Files
    // NOTE: Image Files are expected in the root folder (same as the "src" directory).
    private File xImageFile = new File("x.png");
    private File oImageFile = new File("o.png");
    private File blankImageFile = new File("blank.png");

    // Main UI Panel
    private JPanel panel = new JPanel();

    // Game State Elements
    private JLabel currentPlayer = new JLabel();
    private JLabel winStatus = new JLabel();
    private JLabel winScreen = new JLabel();
    private enum Player
    {
        X, O, None
    }
    private Player currentTurn;
    private boolean Ovalues[][] = new boolean[3][3]; // defaults to false
    private boolean Xvalues[][] = new boolean[3][3]; // defaults to false
    private ArrayList<JButton> listUsed = new ArrayList<JButton>();


    // Main Entry Point for Game
    public void run()
    {
        // Creating instance of JFrame
        JFrame frame = new JFrame("Tic Tac Toe");
        // Setting the width and height of frame
        frame.setSize(565, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating panel. This is same as a div tag in HTML
        // We can create several panels and add them to specific
        // positions in a JFrame. Inside panels we can add text
        // fields, buttons and other components.
        // adding panel to frame
        frame.add(panel);
        // Add all UI components to the panel (build the UI).
        buildGameUI(panel);
        // Setting the frame visibility to true
        frame.setVisible(true);
    }


    private void buildGameUI(JPanel panel)
    {
        panel.setLayout(null);
        // Make Win Screen Top ZOrder
        panel.add(winScreen);

        // Reset Button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                reset();
            }
        });
        resetButton.setBounds(10, 600, 80, 25);
        panel.add(resetButton);

        // Close Button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        closeButton.setBounds(100, 600, 80, 25);
        panel.add(closeButton);

        // Game Buttons
        for (int j = 0; j < 3; j++ )
        {
            for (int i = 0; i < 3; i++)
            {
                JButton jb = new JButton();
                setImageIcon(jb, blankImageFile);
                jb.setBounds(50 + 147 * j, 50 + 147 * i, 142, 142);
                jb.setName("button" + i + j);
                jb.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        onGameButtonClick((JButton) e.getSource());
                    }
                });
                panel.add(jb);
            }
        }

        //
        // Text Fields
        //
        // Game State Elements
        currentPlayer.setBounds(65,525,40,25);
        panel.add(currentPlayer);

        winStatus.setBounds(65,555,60,25);
        panel.add(winStatus);

        winScreen.setBounds(65,240,350,75);
        winScreen.setForeground(Color.GREEN);
        winScreen.setFont(winScreen.getFont().deriveFont(60.0f));
        winScreen.setHorizontalAlignment(JLabel.CENTER);
        // winscreen is already added to the panel (to make it top zorder).

        // Static Text
        JLabel helpText = new JLabel("Select any blank square to move.");
        helpText.setBounds(10,495,200,25);
        panel.add(helpText);

        JLabel turnLabel = new JLabel("Turn:");
        turnLabel.setBounds(10,525,40,25);
        panel.add(turnLabel);

        JLabel winnerLabel = new JLabel("Winner:");
        winnerLabel.setBounds(10,555,60,25);
        panel.add(winnerLabel);

        // Set Initial Game State
        reset();
    }

    private void reset ()
    {
        // Clear Board
        for (Object obj : listUsed) {
            setImageIcon((JButton) obj, blankImageFile);
        }
        listUsed.clear();

        // Clear Game State
        currentTurn = Player.X;
        currentPlayer.setText("X");
        winStatus.setText("< none >");
        winScreen.setText("No Winner");
        winScreen.setVisible(false);
        java.util.Arrays.fill(Ovalues[0], false);
        java.util.Arrays.fill(Ovalues[1], false);
        java.util.Arrays.fill(Ovalues[2], false);
        java.util.Arrays.fill(Xvalues[0], false);
        java.util.Arrays.fill(Xvalues[1], false);
        java.util.Arrays.fill(Xvalues[2], false);
        setGameBoardEnabled(true);
    }


    private void setImageIcon (JButton jb, File imageFile)
    {
        try
        {
            Image img = ImageIO.read(imageFile);
            jb.setIcon(new ImageIcon(img));
        } catch (IOException ex)
        {
            // TODO: What to do here?
        }
    }


    private void setGameBoardEnabled (boolean enable)
    {
        Component[] comp = panel.getComponents();
        for (int i = 0;i<comp.length;i++) {
            if (comp[i] instanceof JButton) {
                JButton jb = (JButton) comp[i];
                if (jb.getText() != "Reset" && jb.getText() != "Close") {
                    jb.setEnabled(enable);
                }
            }
        }
    }


    private void onGameButtonClick (JButton jb)
    {
        // Can't play if game over
        if (currentTurn == Player.None)
            return;
        // Make sure not already used
        for (Object obj : listUsed) {
            // NOTE: Use ".equals()" for string comparison, not "=="
            if (((JButton) obj).getName().equals(jb.getName()))
                return;
        }
        // Set the new image
        setImageIcon (jb, currentTurn == Player.X ? xImageFile : oImageFile);
        listUsed.add(jb);
        // Set the value
        Player winner = Player.None;
        String button = jb.getName();
        int x = button.charAt(6) - '0';
        int y = button.charAt(7) - '0';
        if (currentTurn == Player.X) {
            Xvalues[x][y] = true;
            if (isAWin(Xvalues))
                winner = Player.X;
        }
        else
        {
            Ovalues[x][y] = true;
            if (isAWin(Ovalues))
                winner = Player.O;
        }

        if (winner != Player.None) {
            // Game Over
            setGameCompleted(winner == Player.X ? "X Wins!!!" : "O Wins!!!");
        }
        else
        {
            if (listUsed.size() < 9) {
                currentTurn = (currentTurn == Player.X) ? Player.O : Player.X;
                currentPlayer.setText(currentTurn == Player.X ? "X" : "O");
            }
            else
            {
                // Draw
                setGameCompleted("Draw");
            }
        }
    }


    class DeferVisible implements Runnable
    {
        private JLabel showMe;
        public DeferVisible (JLabel jl) { showMe = jl; }
        public void run()
        {
            try
            {
                Thread.sleep(50);
            } catch (InterruptedException ex)
            {
                // TODO: What to do here?
            }
            showMe.setVisible(true);
        }
    }


    private void setGameCompleted (String message)
    {
        setGameBoardEnabled(false);
        currentTurn = Player.None;
        winStatus.setText(message);
        winScreen.setText(message);

        // Defered Visibility for winscreen as it needs to paint over the disabled board.
        Thread object = new Thread(new DeferVisible(winScreen));
        object.start();
    }


    private boolean isAWin (boolean[][] moves)
    {
        // Columns
        for (int iCol = 0; iCol < 3; iCol++) {
            if (moves[0][iCol] && moves[1][iCol] && moves[2][iCol])
                return true;
        }
        // Rows
        for (int iRow = 0; iRow < 3; iRow++) {
            if (moves[iRow][0] && moves[iRow][1] && moves[iRow][2])
                return true;
        }
        // Diagonals
        return (moves[1][1] && ((moves[0][0] && moves[2][2]) || (moves[2][0] && moves[0][2])));
    }
}
