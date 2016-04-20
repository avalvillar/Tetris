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
public class ControlInfoPanel extends JPanel {
    
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
     */
    public ControlInfoPanel() {
        super();
        setBackground(Color.BLACK);
        setPreferredSize(SIZE);
        setMinimumSize(getPreferredSize());
        final TitledBorder title = BorderFactory.createTitledBorder("Controls");
        title.setTitleColor(Color.WHITE);
        setBorder(title);
        final JLabel controls = new JLabel("Listed in the Menu Bar");
        controls.setForeground(Color.WHITE);
        add(controls);
    }
}