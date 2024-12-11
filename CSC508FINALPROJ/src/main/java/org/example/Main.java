package org.example;

import javax.swing.*;

/**
 * Entry point of the application, responsible for initializing the GUI and launching the Cobot control system.
 *
 * @version 1.0
 * @author Luca Ornstil
 * @author Monish Suresh
 * @author Christine Widden
 */

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cobot Control");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.add(new CobotControlPanel());
            frame.setVisible(true);
        });
    }
}