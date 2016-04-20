/*
 * TCSS 305, Fowler
 * Spring 2015
 * Assignment 6 : Tetris
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Board;

/**
 * The Game Board class is a panel that will be used to play the Tetris game. 
 * It does this by listening to the Board (as an observer) and drawing/displaying the game
 * as pieces are played. 
 * 
 * @author Antonio V. Alvillar
 * @version 04/June/2015
 */
public class GameBoard extends JPanel implements Observer {
    
    /** A Generated serial number. */
    private static final long serialVersionUID = 4390421953250290955L;

    /** Constant needed to adjust the board (SMALL), to account for the pieces' outline. */
    private static final int SMALL_WIDTH = -1;
    
    /** Constant needed to adjust the board (MEDIUM), to account for the pieces' outline. */
    private static final int MED_WIDTH = 9;
    
    /** Constant needed to adjust the board (MEDIUM), to account for the pieces' outline. */
    private static final int MED_HEIGHT = -13; 
    
    /** Constant needed to adjust the board (LARGE), to account for the pieces' outline. */
    private static final int LRG_WIDTH = -1;

    /** A constant to check the incoming board size (if > this, board is small). */
    private static final int SMALL_SIZE = 20;
    
    /** A constant to check the incoming board size (if > this, board is large). */
    private static final int LRG_SIZE = 30;
    
    /** Size of the Tetris pieces. */
    private int myPieceSize;

    /** A list of Tetris Pieces. */
    private final List<Piece> myBoardData;
    
    /** Creates the game panel. 
     * 
     * @param theBoard : The board for the game to play on.
     * @param theSize : The size for the pieces and board panel. 
     */
    public GameBoard(final Board theBoard, final int theSize) {
        super();
        setBoardSize(theBoard, theSize);
        System.out.println(this.getPreferredSize());
        setBackground(Color.BLACK);
        myBoardData = new ArrayList<>();
    }

    /**
     * Method to set the size of the Game Board panel depending on the size 
     * chosen by the client. Default size is medium. 
     * 
     * @param theBoard : The board to play the game on.
     * @param theSize : The size selected by the client (or the default).
     */
    private void setBoardSize(final Board theBoard, final int theSize) {
        final Board board = theBoard;
        myPieceSize = theSize;
        System.out.println(myPieceSize);
        if (myPieceSize < SMALL_SIZE) {
            setPreferredSize(new Dimension(myPieceSize * board.getWidth() - SMALL_WIDTH, 
                                           myPieceSize * board.getHeight()));
        } else if (myPieceSize > LRG_SIZE) {
            setPreferredSize(new Dimension(myPieceSize * board.getWidth() - LRG_WIDTH, 
                                           myPieceSize * board.getHeight()));
        } else {
            setPreferredSize(new Dimension(myPieceSize * board.getWidth() - MED_WIDTH, 
                                           myPieceSize * board.getHeight() - MED_HEIGHT));
            System.out.println(board.getHeight());
            System.out.println(board.getWidth());
            System.out.println(MED_HEIGHT);
            System.out.println(MED_WIDTH);
        }
    }

    /**
     * Repaints the Game board with Tetris pieces as the game plays. 
     * 
     * @param theGraphics : The graphics object. 
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        final AffineTransform transform = new AffineTransform();
        transform.translate(0, getHeight());
        transform.scale(1.0, -1.0);
        g2d.setTransform(transform);
        for (int i = myBoardData.size() - 1; i >= 0; i--) {
            g2d.setColor(myBoardData.get(i).getColor());
            g2d.fill(myBoardData.get(i).getPiece());
            g2d.setColor(Color.BLACK);
            g2d.draw(myBoardData.get(i).getPiece());   
        }  
    }
    
    /**
     * Updates the game board by keeping track of what should be painted (pieces).
     * 
     * @param theObservable : The Board that this is observing.
     * @param theData : The data passed in from the board (array list of colors).
     */
    @Override
    public void update(final Observable theObservable, final Object theData) {
        if (theData instanceof ArrayList) {
            myBoardData.clear();
            final List<Color[]> board = new ArrayList<Color[]>((List<Color[]>) theData);
            for (int i = board.size() - 1; i >= 0; i--) {
                final Color[] row = board.get(i);
                for (int j = 0; j < row.length; j++) {
                    if (row[j] != null) {
                        final Rectangle2D.Double rectangle = 
                                        new Rectangle2D.Double(j * myPieceSize, 
                                                               i * myPieceSize, myPieceSize,
                                                                                myPieceSize);
                        myBoardData.add(new Piece(rectangle, row[j]));
                    } 
                }
                repaint();
            }  
        } 
    }
    
//////// INNER CLASS ///////////////////////////////////////////////////////////////////    
    
    /***/
    private final class Piece {

        /** The Tetris piece created. */
        private final Shape myPiece;
        
        /** The color of the Tetris piece. */
        private final Color myColor;

        /**
         * The constructor of a Tetris piece. 
         * 
         * @param thePiece : The Tetris piece. 
         * @param theColor : The Tetris pieces color. 
         */
        public Piece(final Shape thePiece, final Color theColor) {
            myPiece = thePiece;
            myColor = theColor;
        }
        
        /**
         * Method to retrieve a Tetris piece. 
         * 
         * @return Shape : The Tetris piece as a shape. 
         */
        private Shape getPiece() {
            return myPiece;
        }
        
        /**
         * Method to get the color of the Tetris piece. 
         * 
         * @return Color : The color of the Tetris piece. 
         */
        private Color getColor() {
            return myColor;
        }
    }
}
