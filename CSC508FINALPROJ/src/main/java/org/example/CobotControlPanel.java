package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main panel class for managing user interactions, connecting to the cobot, and handling letter input through the GUI.
 *
 * @version 1.0
 * @author Luca Ornstil
 * @author Monish Suresh
 * @author Christine Widden
 */

public class CobotControlPanel extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(CobotControlPanel.class);
    private final Cobot cobot;
    private final CobotGUI gui;

    public CobotControlPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        cobot = new Cobot();
        gui = new CobotGUI();

        // Add header and keyboard panels
        add(gui.createHeaderPanel(this::connectCobot, this::disconnectCobot), BorderLayout.NORTH);
        add(gui.createKeyboardPanel(this::handleKeyPress), BorderLayout.CENTER);
    }

    private void connectCobot(ActionEvent e) {
        if (cobot.connect()) {
            gui.statusLabel.setText("Status: Connected");
            gui.statusLabel.setForeground(Color.GREEN);
            gui.connectButton.setEnabled(false);
            gui.disconnectButton.setEnabled(true);
            logger.info("Cobot connected successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to connect to the Cobot. Please try again.");
            logger.error("Failed to connect to Cobot.");
        }
    }

    private void disconnectCobot(ActionEvent e) {
        cobot.disconnect();
        gui.statusLabel.setText("Status: Disconnected");
        gui.statusLabel.setForeground(Color.RED);
        gui.connectButton.setEnabled(true);
        gui.disconnectButton.setEnabled(false);
        logger.info("Cobot disconnected.");
    }

    private void handleKeyPress(ActionEvent e) {
        String key = e.getActionCommand().toUpperCase();

        if (key == null || key.trim().isEmpty() || !key.matches("[A-Z]")) {
            JOptionPane.showMessageDialog(this, "Invalid key. Please press a valid letter (A-Z).");
            logger.warn("Invalid key '{}' attempted.", key);
            return;
        }

        if (!cobot.isConnected()) {
            JOptionPane.showMessageDialog(this, "Please connect to the Cobot first.");
            logger.warn("Attempted to send key '{}' without connection.", key);
            return;
        }

        Blackboard.getInstance().setKeyboardLetter(key);
        gui.commandIndicator.setText("Command: " + key);
        logger.info("Key '{}' clicked and sent to Blackboard.", key);
    }
}