package org.example;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cobot implements PropertyChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(Cobot.class);
    private Socket socket;
    private PrintWriter out;

    public Cobot() {
        // Register this class as a listener to the Blackboard
        Blackboard.getInstance().addPropertyChangeListener(this);
        logger.info("Cobot registered as a listener to Blackboard.");
    }

    public boolean connect() {
        try {
            socket = new Socket("localhost", 30002);
            out = new PrintWriter(socket.getOutputStream(), true);
            logger.info("Connected to Cobot.");
            return true;
        } catch (IOException e) {
            logger.error("Failed to connect to Cobot: {}", e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            if (out != null) out.close();
            if (socket != null) socket.close();
            logger.info("Disconnected from Cobot.");
        } catch (IOException e) {
            logger.error("Failed to disconnect from Cobot: {}", e.getMessage());
        }
    }

    private void sendCommand(String command) {
        if (out != null) {
            out.println(command);
            logger.info("Sent command to Cobot: {}", command);
        } else {
            logger.warn("Attempted to send a command while not connected.");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("data".equals(evt.getPropertyName())) {
            String letter = (String) evt.getNewValue();
            logger.info("Received data from Blackboard: {}", letter);
            drawLetter(letter);
        }
    }

    private void drawLetter(String letter) {
        switch (letter) {
            case "A":
                sendCommand("movel(p[0.0, 0.0, 0.1, 0, 0, 0], a=1.0, v=0.5)"); // Example
                break;
            case "B":
                sendCommand("movel(p[0.0, 0.1, 0.1, 0, 0, 0], a=1.0, v=0.5)"); // Example
                break;
            // Add cases for other letters
            default:
                logger.warn("Unhandled letter: {}", letter);
        }
    }
}
