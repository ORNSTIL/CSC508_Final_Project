package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobotControlPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(CobotControlPanel.class);

    private JButton connectButton;
    private JButton disconnectButton;
    private JButton resetButton;
    private JLabel statusLabel;
    private JLabel commandLabel;
    private JPanel keyboardRowPanel1;
    private JPanel keyboardRowPanel2;
    private JPanel keyboardRowPanel3;

    public CobotControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setBackground(Color.BLACK);

        // Title Panel
        JLabel titleLabel = new JLabel("Callie");
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Billabong.otf")).deriveFont(70f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            titleLabel.setFont(font);
        } catch (Exception e) {
            logger.error("Error loading custom font: {}", e.getMessage());
            titleLabel.setFont(new Font("Serif", Font.BOLD, 50)); // Fallback font
        }
        titleLabel.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        titlePanel.add(titleLabel);

        // Status Panel
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(2, 1));
        statusPanel.setBackground(Color.BLACK);

        statusLabel = new JLabel("Disconnected from URSim");
        statusLabel.setForeground(Color.RED);
        commandLabel = new JLabel("Command: ");
        commandLabel.setForeground(Color.WHITE);

        statusPanel.add(statusLabel);
        statusPanel.add(commandLabel);

        // Control Buttons
        connectButton = createButton("Connect");
        disconnectButton = createButton("Disconnect");
        resetButton = createButton("Reset");

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 1, 5, 5));
        controlPanel.setBackground(Color.BLACK);
        controlPanel.add(connectButton);
        controlPanel.add(disconnectButton);
        controlPanel.add(resetButton);

        // Keyboard Panels
        keyboardRowPanel1 = createKeyboardRow("QWERTYUIOP");
        keyboardRowPanel2 = createKeyboardRow("ASDFGHJKL");
        keyboardRowPanel3 = createKeyboardRow("ZXCVBNM");

        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.Y_AXIS));
        keyboardPanel.setBackground(Color.BLACK);
        keyboardPanel.add(keyboardRowPanel1);
        keyboardPanel.add(keyboardRowPanel2);
        keyboardPanel.add(keyboardRowPanel3);

        // Add components to the main panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(titlePanel);
        add(statusPanel);
        add(controlPanel);
        add(keyboardPanel);

        // Action Listeners
        addActionListeners();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createKeyboardRow(String letters) {
        JPanel rowPanel = new JPanel();
        rowPanel.setBackground(Color.BLACK);
        rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        for (char c : letters.toCharArray()) {
            JButton keyButton = new JButton(String.valueOf(c));
            keyButton.setPreferredSize(new Dimension(50, 50));
            keyButton.setBackground(Color.WHITE);
            keyButton.setForeground(Color.BLACK);
            keyButton.setFocusPainted(false);
            keyButton.addActionListener(e -> handleKeyPress(String.valueOf(c)));
            rowPanel.add(keyButton);
        }

        return rowPanel;
    }

    private void handleKeyPress(String letter) {
        Blackboard blackboard = Blackboard.getInstance();
        blackboard.setData(letter);
        commandLabel.setText("Command: " + letter);
        logger.info("Letter '{}' sent to Blackboard.", letter);
    }

    private void addActionListeners() {
        Cobot cobot = new Cobot();

        connectButton.addActionListener(e -> {
            if (cobot.connect()) {
                statusLabel.setText("Connected to URSim");
                statusLabel.setForeground(Color.GREEN);
                connectButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                logger.info("Connected to Cobot.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to connect to Cobot.");
                logger.error("Failed to connect to Cobot.");
            }
        });

        disconnectButton.addActionListener(e -> {
            cobot.disconnect();
            statusLabel.setText("Disconnected from URSim");
            statusLabel.setForeground(Color.RED);
            connectButton.setEnabled(true);
            disconnectButton.setEnabled(false);
            logger.info("Disconnected from Cobot.");
        });

        resetButton.addActionListener(e -> {
            Blackboard.getInstance().setData("RESET");
            commandLabel.setText("Command: RESET");
            logger.info("Reset command sent.");
        });
    }
}
