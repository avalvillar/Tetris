/*
 * TCSS 305, Fowler
 * Spring 2015
 * Assignment 6 : Tetris
 */

package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * This class creates a JPanel which draws the next piece.
 * 
 * @author Antonio V. Alvillar
 * @version 04/June/2015
 */
public class GameScore extends JPanel {
    
    /**
     * The size of the JPanel.
     */
    public static final Dimension SIZE = new Dimension(150, 50);
    
    /**
     * 
     */
    private static final long serialVersionUID = 6518536083905151938L;

    /**
     * This constructor initializes the fields as well as setting up the panels features.
     * 
     * @param theScore : The score to be displayed on the panel.
     * @param theLevel : The level to be displayed on the panel.
     */
    public GameScore(final int theScore, final int theLevel) {
        super();
        setBackground(Color.BLACK);
        setPreferredSize(SIZE);
        setMinimumSize(getPreferredSize());
        final TitledBorder title = BorderFactory.createTitledBorder("Current Progress");
        title.setTitleColor(Color.WHITE);
        setBorder(title);
        updateScore(theScore, theLevel);
    }
    
    /**
     * 
     * @param theNewScore 
     * @param theNewLevel 
     */
    private void updateScore(final int theNewScore, final int theNewLevel) {
        final JLabel score = new JLabel("Score: " + theNewScore + " ");
        final JLabel level = new JLabel("Level: " + theNewLevel);
        score.setForeground(Color.WHITE);
        level.setForeground(Color.WHITE);
        add(score);
        add(level);
    }
}