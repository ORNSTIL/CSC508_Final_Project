package org.example;

import javax.swing.*;
import java.awt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobotControlPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(CobotControlPanel.class);
    private JButton connectButton;
    private JButton transmitButton;
    private JButton stopButton;
    private JTextField letterInput;

    public CobotControlPanel() {
        setLayout(new GridLayout(4, 1));

        Blackboard blackboard = Blackboard.getInstance();
        Cobot cobot = new Cobot();

        // Connect Button
        connectButton = new JButton("Connect to Cobot");
        connectButton.addActionListener(e -> {
            if (cobot.connect()) {
                connectButton.setEnabled(false);
                stopButton.setEnabled(true);
                logger.info("Cobot successfully connected.");
            } else {
                logger.error("Failed to connect to Cobot.");
            }
        });

        // Letter Input
        letterInput = new JTextField();
        letterInput.setToolTipText("Enter a single letter (A-Z)");

        // Transmit Button
        transmitButton = new JButton("Transmit Letter");
        transmitButton.setEnabled(true);
        transmitButton.addActionListener(e -> {
            String input = letterInput.getText().trim().toUpperCase();
            if (input.matches("[A-Z]") && input.length() == 1) {
                blackboard.setData(input);
                letterInput.setText(""); // Clear the field
                logger.info("Letter '{}' transmitted to the Cobot.", input);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a single letter (A-Z).");
                logger.warn("Invalid input: '{}'. Please enter a single letter (A-Z).", input);
            }
        });

        // Stop Button
        stopButton = new JButton("Stop Connection");
        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> {
            cobot.disconnect();
            connectButton.setEnabled(true);
            stopButton.setEnabled(false);
            logger.info("Cobot connection stopped.");
        });

        // Add components
        add(connectButton);
        add(letterInput);
        add(transmitButton);
        add(stopButton);
    }
}
