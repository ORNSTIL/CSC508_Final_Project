package org.example;

import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            logger.info("Starting the Cobot Control Application...");
            JFrame frame = new JFrame("Cobot Control");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.add(new CobotControlPanel());
            frame.setVisible(true);
        });
    }
}
