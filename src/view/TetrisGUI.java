/*
 * TCSS 305, Fowler
 * Spring 2015
 * Assignment 6 : Tetris
 */

package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.Timer;

import model.Board;

/**
 * The Tetris GUI class will build the Tetris GUI and all components
 * as well as work with other classes to provide functionality per the 
 * assignment instructions. 
 * 
 * @author Antonio V. Alvillar
 * @version 04/June/2015
 */
public class TetrisGUI implements Observer {
    
    private int myLines;

    /** The timer delay that keeps track of the time between animations. */
    private static final int TIMER_DELAY = 1000;

    /** Timer to progress the game by periodically calling the step method on the board. */
    private final Timer myTimer;
    
    /** JFrame to hold the tetris game board and other components. */
    private final JFrame myFrame;
    
    /** JPanel the will be the right side of the GUI - to display next piece. */
    private final JPanel myEastPanel; 
    
    /** The panel and image to display the upcoming piece. */
    private final NextPiece myNextPiece;
    
    /** The panel and image to display the upcoming piece. */
    private final GameScore myGameScore;
    
    /** The panel and image to display the upcoming piece. */
    private final ControlInfoPanel myInfoPanel;
    
    /** The Tetris game board that will display the game in progress. */
    private final Board myBoard;
    
    /** The Menu bar that will be attached to the frame. */
    private final JMenuBar myMenuBar;

    /** Keep track of the game when/if paused (initial pause to prevent key listeners). */
    private boolean myGamePaused = true;
    
    /** JMenuItem for the New Game (enables and disables at certain times). */
    private JMenuItem myNewGame;
    
    /** JMenuItem for the End Game (enables and disables at certain times). */
    private JMenuItem myEndGame;
    
    /**
     * The public constructor that is called by the main class. 
     * Will initialize needed fields, components and call the Start
     * method that will do most of the creation for the assignment. 
     */
    public TetrisGUI() {
        myFrame = new JFrame("Tetris");
        myBoard = new Board();
        myTimer = new Timer(TIMER_DELAY, new TimerAction());
        myEastPanel = new JPanel();
        myNextPiece = new NextPiece();
        myGameScore = new GameScore(0, 0);
        myInfoPanel = new ControlInfoPanel();
        myMenuBar = new JMenuBar();
        create();
    }

    /**
     * Start method will handle a lot of the creation of and addition of components 
     * by using other methods. 
     */
    private void create() {
        createGameMenu();
        createSizeMenu();
        createControlsMenu();
        myFrame.setJMenuBar(myMenuBar);
        myEastPanel.setLayout(new BoxLayout(myEastPanel, BoxLayout.Y_AXIS));
        myEastPanel.add(myNextPiece);
        myEastPanel.add(myGameScore);
        myEastPanel.add(myInfoPanel);
        myBoard.addObserver(this);
        myBoard.addObserver(myNextPiece);
        setUpFrame();
    }

    /** Method to set up the frame: Location, default game size, pack, visible. */
    private void setUpFrame() {
        myFrame.add(myEastPanel, BorderLayout.EAST);
        final GameBoard newPanel = new GameBoard(myBoard, SizeAction.MEDIUM);
        myBoard.addObserver(newPanel);
        myFrame.getContentPane().add(newPanel, 0);
        myFrame.getContentPane().revalidate();
        myFrame.addKeyListener(new Controller());
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /** A ToolKit used to set frame location relative to the current screen (center). */
        final Toolkit kit = Toolkit.getDefaultToolkit();
        final int scaleW = 3;
        final int scaleH = 4;
        myFrame.setLocation(
            (int) (kit.getScreenSize().getWidth() / scaleW - myFrame.getWidth() / scaleW),
            (int) (kit.getScreenSize().getHeight() / scaleH - myFrame.getHeight() / scaleH));
        myFrame.pack();
        System.out.println(myFrame.getSize());
        myFrame.setResizable(false);
        myFrame.setVisible(true);
    }

    /** This method will create the game menu and needed listeners for the menu items. */
    private void createGameMenu() {
        final JMenu game = new JMenu("Game");
        myNewGame = new JMenuItem("New Game");
        myEndGame = new JMenuItem("End Game");
        myNewGame.addActionListener(new GameAction());
        myEndGame.addActionListener(new GameAction());
        game.add(myNewGame);
        myEndGame.setEnabled(false);
        game.add(myEndGame);
        myMenuBar.add(game);
    }
    
    /** This method will create the size menu as well as add the needed listeners. */
    private void createSizeMenu() {
        final ButtonGroup buttonGroup = new ButtonGroup();
        final JMenu size = new JMenu("Size");
        final JRadioButtonMenuItem small = new JRadioButtonMenuItem("small");
        buttonGroup.add(small);
        small.addActionListener(new SizeAction());
        size.add(small);
        final JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Medium");
        buttonGroup.add(medium);
        medium.addActionListener(new SizeAction());
        medium.setSelected(true);
        size.add(medium);
        final JRadioButtonMenuItem large = new JRadioButtonMenuItem("LARGE");
        buttonGroup.add(large);
        large.addActionListener(new SizeAction());
        size.add(large);
        myMenuBar.add(size);
    }
    
    /** This method will create the controls menu that just list the game controls. */
    private void createControlsMenu() {
        final JMenu controls = new JMenu("Controls");
        final JMenuItem controlsInfo = new JMenuItem("Game Controls");
        controlsInfo.addActionListener(new GameAction());
        controls.add(controlsInfo);
        myMenuBar.add(controls);
    }

    /** Change the status if the game is paused. */
    private void setPaused() {
        myGamePaused ^= true;
    }

    /**
     * Updates the game board by keeping track of when the board is full and
     * the game is over. During this check it will also enable and disable the end game and
     * new game menu items as need. 
     */
    @Override
    public void update(final Observable theObservable, final Object theData) {
        if ("true".equals(theData.toString())) {
            myTimer.stop();
            JOptionPane.showMessageDialog(null, "\"That part will happen pretty definitely!\""
                                          + "\n                         -Wash", 
                                          "GAME OVER!!!!!",
                                          JOptionPane.WARNING_MESSAGE);
            myEndGame.setEnabled(false);
            myNewGame.setEnabled(true);
        }
        if (theData instanceof Integer[]) {
            myLines++;
            final JPanel newGameScore = new GameScore(myLines, 0);
            myEastPanel.remove(1);
            myEastPanel.invalidate();
            myEastPanel.add(newGameScore, 1);
            myEastPanel.revalidate();
        }
    }

/////  BELOW ARE ALL INNER CLASSES ///////////////////////////////////////////////////////////
    
    /** 
     * The Game Action INNER CLASS used for some of the game settings. Such as when a new
     * game is selected or to end the current game. The option to change the key bindings
     * in the control menu will also apply here. 
     */
    private final class GameAction implements ActionListener {
        /**
         * Which option was chosen by the client will determine what happens in this method. 
         * Whether to start a new game or end the current one, as well as possibly changing
         * the controls for the game. 
         * 
         * @param theEvent : The selected menu item (new game, end game, change controls).
         */
        public void actionPerformed(final ActionEvent theEvent) {
            if ("new game".equalsIgnoreCase(theEvent.getActionCommand())) {
                myTimer.stop();
                myEndGame.setEnabled(true);
                myNewGame.setEnabled(false);
                myGamePaused = false;
                myBoard.newGame();
                myTimer.start();
            } 
            if ("end game".equalsIgnoreCase(theEvent.getActionCommand())) {
                myTimer.stop();
                myEndGame.setEnabled(false);
                myNewGame.setEnabled(true);
            }
            if ("game controls".equalsIgnoreCase(theEvent.getActionCommand())) {
                setPaused();
                JOptionPane.showMessageDialog(null, "Move LEFT:      \"Left Arrow\" \n"
                                                  + "Move RIGHT:    \"Right Arrow\" \n"
                                                  + "Move DOWN:   \"Down Arrow\" \n"
                                                  + "Rotate CW:       \"A\" \n"
                                                  + "Rotate CCW:    \"S\" \n"
                                                  + "Piece DROP:    \"Space Bar\"", 
                                                    "Tetris Controls", 
                                                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /** 
     * The Size Action INNER CLASS used to changed the displayed game boards size 
     * at the request of the client.  
     */
    private final class SizeAction implements ActionListener {
        
        /** Constants for the game size (small). */
        private static final int SMALL = 15;
        
        /** Constants for the game size (medium). */
        private static final int MEDIUM = 25;

        /** Constants for the game size (large). */
        private static final int LARGE = 35;
        
        /**
         * The performed method used when a client selects a different size for the game board
         * from the Size menu. 
         * 
         * @param theEvent : Used to check which size the client has chosen. 
         */
        public void actionPerformed(final ActionEvent theEvent) {
            if ("Small".equalsIgnoreCase(theEvent.getActionCommand())) {
                changeBoardSize(SMALL);
            } 
            if ("medium".equalsIgnoreCase(theEvent.getActionCommand())) {
                changeBoardSize(MEDIUM);
            }
            if ("Large".equalsIgnoreCase(theEvent.getActionCommand())) {
                changeBoardSize(LARGE);
            }
        }
        
        /**
         * Changes the display size of the game board. The current game stays active. 
         * 
         * @param theSize : The requested display size for the new board. 
         */
        private void changeBoardSize(final int theSize) {
            myFrame.getContentPane().remove(0);
            myFrame.getContentPane().invalidate();
            final GameBoard newPanel = new GameBoard(myBoard, theSize);
            myBoard.addObserver(newPanel);
            myFrame.getContentPane().add(newPanel, 0);
            myFrame.getContentPane().revalidate();
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myFrame.pack();
            System.out.println(myFrame.getSize());
            myFrame.setResizable(false);
        }
    }
    
    /** 
     * The Timer Action INNER CLASS to animate the game depending on the status as well as 
     * keep track for a pause in game play. 
     */
    private final class TimerAction implements ActionListener {
        
        /** 
         * The action performed method called by the timer.
         *  
         * @param theEvent : ignored. 
         */
        public void actionPerformed(final ActionEvent theEvent) {
            if (!myGamePaused) {
                myBoard.step();
            }
        }
    }
    
  /** 
   * The listener INNER CLASS for the keys that are pressed to move the game pieces or pause 
   * the game. 
   */
    private final class Controller extends KeyAdapter {
        
        /** 
         * The key pressed method that will call actions on the board.
         * 
         * @param theEvent : The key that was pressed. 
         */
        public void keyPressed(final KeyEvent theEvent) {
            if ("p".equalsIgnoreCase(KeyEvent.getKeyText(theEvent.getKeyCode()))) {
                setPaused();
            }
            if (!myGamePaused && (myEndGame.isEnabled())) {
                movePiece(theEvent);
            }
        }
        
        /**
         * Private method to move the pieces on the board only if the game is not paused. 
         * 
         * @param theEvent : The key that was pressed. 
         */
        private void movePiece(final KeyEvent theEvent) {
            if ("left".equalsIgnoreCase(KeyEvent.getKeyText(theEvent.getKeyCode()))) {
                myBoard.left();
            } 
            if ("right".equalsIgnoreCase(KeyEvent.getKeyText(theEvent.getKeyCode()))) {
                myBoard.right();
            } 
            if ("down".equalsIgnoreCase(KeyEvent.getKeyText(theEvent.getKeyCode()))) {
                myBoard.down();
            }
            if ("a".equalsIgnoreCase(KeyEvent.getKeyText(theEvent.getKeyCode()))) {
                myBoard.rotateCW();
            }
            if ("s".equalsIgnoreCase(KeyEvent.getKeyText(theEvent.getKeyCode()))) {
                myBoard.rotateCCW();
            }
            if ("space".equalsIgnoreCase(KeyEvent.getKeyText(theEvent.getKeyCode()))) {
                myBoard.drop();
            }
        }
    }
}
