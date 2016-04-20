/*
 * TCSS 305, Fowler
 * Spring 2015
 * Assignment 6 : Tetris
 */

package view;

import java.awt.EventQueue;

/**
 * The main class to start the Tetris program by instantiation of the Tetris GUI.
 * 
 * @author Antonio V. Alvillar
 * @version 04/June/2015
 */
public final class TetrisMain {
        
    /**
     * Private constructor to prevent instantiation of this class.
     */
    private TetrisMain() {
        throw new IllegalStateException();
    }

    /**
     * Main method that invokes the Tetris GUI. Command line arguments are ignored. 
     * 
     * @param theArgs : The command line arguments.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGUI();
            }
        });

    }
}
