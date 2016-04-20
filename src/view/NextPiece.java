/*
 * TCSS 305, Fowler
 * Spring 2015
 * Assignment 6 : Tetris
 */

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import model.Point;
import model.TetrisPiece;

/**
 * This class creates a JPanel which draws the next piece.
 * 
 * @author Antonio V. Alvillar
 * @version 04/June/2015
 */
public class NextPiece extends JPanel implements Observer {
    
    /**
     * The size of the JPanel.
     */
    public static final Dimension SIZE = new Dimension(150, 50);

    /** The size needed to multiply when creating each square of the pieces. */
    private static final int PIECE_SIZE = 25;
    
    /** A constant used to position the piece on the JPanel (inside the border). */
    private static final int SCALE_WIDTH = 6;
    
    /** A constant used to position the piece on the JPanel (inside the border). */
    private static final int SCALE_HEIGHT = 9;
    
    /***/
    private static final long serialVersionUID = 1L;

    /** The Tetris piece to be displayed as the upcoming piece. */
    private TetrisPiece myNextPiece;

    /**
     * This constructor initializes the fields as well as setting up the panels features.
     */
    public NextPiece() {
        super();
        setBackground(Color.BLACK);
        setPreferredSize(SIZE);
        setMinimumSize(getPreferredSize());
        final TitledBorder title = BorderFactory.createTitledBorder("Next Piece");
        title.setTitleColor(Color.WHITE);
        setBorder(title);
        setLayout(new BorderLayout());
    }
    
    /**
     * This method copies the Tetris piece passed in. 
     * 
     * @param theObservable : The object that is sending information (board).
     * @param theData : The data that may be passed in (from the board).
     */
    @Override
    public void update(final Observable theObservable, final Object theData) {
        if ("TetrisPiece".equals(theData.getClass().getSimpleName())) {
            myNextPiece = (TetrisPiece) theData;
        }
        repaint();
    }
    
    /**
     * This method draws the pieces onto the JPanel.
     * 
     * @param theGraphics The graphics object that will draw the shape objects.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        final AffineTransform trans = g2d.getTransform();
        final int m = getHeight() / 2;
        g2d.translate(0, m);
        g2d.scale(1, -1);
        g2d.translate(0, -m);
        if (myNextPiece != null) {
            for (final Point points : myNextPiece.getPoints()) {
                g2d.setColor(myNextPiece.getColor());
                g2d.fillRect((points.x() * PIECE_SIZE) + (getWidth() / SCALE_WIDTH), 
                             (points.y() * PIECE_SIZE) + (getHeight() / SCALE_HEIGHT), 
                             PIECE_SIZE, PIECE_SIZE);
                g2d.setColor(Color.BLACK);
                g2d.drawRect((points.x() * PIECE_SIZE) + (getWidth() / SCALE_WIDTH),
                             (points.y() * PIECE_SIZE) + (getHeight() / SCALE_HEIGHT), 
                             PIECE_SIZE, PIECE_SIZE);
            }
        }
        g2d.setTransform(trans);
    }
    
    
}
