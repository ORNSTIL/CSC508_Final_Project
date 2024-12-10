package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Utility class for constructing the graphical user interface (GUI) components, including the keyboard and status indicators.
 *
 * @version 1.0
 * @author Luca Ornstil
 * @author Monish Suresh
 * @author Christine Widden
 */

public class CobotGUI {

    public JLabel statusLabel;
    public JLabel commandIndicator;
    public JButton connectButton;
    public JButton disconnectButton;
    public JPanel keyboardPanel;

    public JPanel createHeaderPanel(ActionListener connectAction, ActionListener disconnectAction) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.BLACK);

        // Add title
        JLabel titleLabel = new JLabel("Callie");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Status and command indicators
        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        statusPanel.setBackground(Color.BLACK);

        statusLabel = new JLabel("Status: Disconnected");
        statusLabel.setForeground(Color.RED);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusPanel.add(statusLabel);

        commandIndicator = new JLabel("Command: None");
        commandIndicator.setForeground(Color.WHITE);
        commandIndicator.setFont(new Font("Arial", Font.BOLD, 16));
        statusPanel.add(commandIndicator);

        headerPanel.add(statusPanel, BorderLayout.CENTER);

        // Add connect and disconnect buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBackground(Color.BLACK);

        connectButton = createStyledButton("Connect", connectAction);
        disconnectButton = createStyledButton("Disconnect", disconnectAction);
        disconnectButton.setEnabled(false);

        buttonPanel.add(connectButton);
        buttonPanel.add(disconnectButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }

    public JPanel createKeyboardPanel(ActionListener keyPressAction) {
        keyboardPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        keyboardPanel.setBackground(Color.BLACK);

        addKeyboardRow(keyboardPanel, "QWERTYUIOP".split(""), keyPressAction);
        addKeyboardRow(keyboardPanel, "ASDFGHJKL".split(""), keyPressAction);
        addKeyboardRow(keyboardPanel, "ZXCVBNM".split(""), keyPressAction);

        return keyboardPanel;
    }

    private void addKeyboardRow(JPanel parentPanel, String[] keys, ActionListener keyPressAction) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        rowPanel.setBackground(Color.BLACK);

        for (String key : keys) {
            JButton keyButton = createKeyButton(key, keyPressAction);
            rowPanel.add(keyButton);
        }

        parentPanel.add(rowPanel);
    }

    private JButton createKeyButton(String key, ActionListener keyPressAction) {
        JButton button = new JButton(key);
        button.setPreferredSize(new Dimension(50, 50));
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createBevelBorder(1));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.LIGHT_GRAY);
            }
        });

        button.addActionListener(e -> keyPressAction.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, key)));
        return button;
    }

    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(action);
        return button;
    }
}
