package org.example;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible for managing the cobot's connection and executing commands to draw letters based on input.
 *
 * @version 1.0
 * @author Luca Ornstil
 * @author Monish Suresh
 * @author Christine Widden
 */

public class Cobot implements PropertyChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(Cobot.class);
    private Socket socket;
    private PrintWriter out;

    // Start position (joint angles)
    private static final String START_POSITION = "movej([1.14, -2.16, -1.58, -1.57, 1.57, 0], a=1.0, v=0.5)";

    public Cobot() {
        Blackboard.getInstance().addPropertyChangeListener(this);
        logger.info("Cobot registered as a listener to Blackboard.");
    }

    public boolean connect() {
        try {
            socket = new Socket(ServerConfig.URSIM_SERVER.getHost(), ServerConfig.URSIM_SERVER.getPort());
            out = new PrintWriter(socket.getOutputStream(), true);
            logger.info("Connected to Cobot at {}:{}", ServerConfig.URSIM_SERVER.getHost(), ServerConfig.URSIM_SERVER.getPort());
            sendCommand(START_POSITION); // Move to the start position on connection
            logger.info("Moved to START_POSITION on connect.");
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

    public boolean isConnected() {
        return socket != null && socket.isConnected();
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
        if (letter == null || letter.trim().isEmpty()) {
            logger.warn("Attempted to draw a null or empty letter.");
            return;
        }

        try {
            // Move to start position
            logger.info("Moving to start position...");
            sendCommand(START_POSITION);
            Thread.sleep(2000); // Wait for the robot to reach the position

            // Perform the drawing movements
            switch (letter) {
                case "A":
                    drawA();
                    break;
                case "B":
                    drawB();
                    break;
                // Add more cases for other letters
                default:
                    logger.warn("Unhandled letter: {}", letter);
            }

            // Return to start position
            logger.info("Returning to start position...");
            sendCommand(START_POSITION);
            Thread.sleep(2000); // Wait for the robot to return
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting: {}", e.getMessage());
        }
    }

    private void drawA() {
        try {
            // Move downwards to start drawing
            sendCommand("movel(p[0.51845, -0.42828, -0.21507, 0.005, -3.134, -0.007], a=1.0, v=0.2)");
            Thread.sleep(1000);

            sendCommand("movel(p[0.51845, -0.42828, -0.31507, 0.005, -3.134, -0.007], a=1.0, v=0.2)");
            Thread.sleep(1000);

            sendCommand("movel(p[0.56845, -0.42828, -0.31507, 0.005, -3.134, -0.007], a=1.0, v=0.2)");
            Thread.sleep(1000);

            sendCommand("movel(p[0.54345, -0.42828, -0.26507, 0.005, -3.134, -0.007], a=1.0, v=0.2)");
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            logger.error("Error while drawing A: {}", e.getMessage());
        }
    }

    private void drawB() {
        try {
            // Move downwards to start drawing
            sendCommand("movel(p[0.51845, -0.42828, -0.21507, 0.005, -3.134, -0.007], a=1.0, v=0.2)");
            Thread.sleep(1000);

            sendCommand("movel(p[0.51845, -0.42828, -0.31507, 0.005, -3.134, -0.007], a=1.0, v=0.2)");
            Thread.sleep(1000);

            sendCommand("movel(p[0.56845, -0.42828, -0.26507, 0.005, -3.134, -0.007], a=1.0, v=0.2)");
            Thread.sleep(1000);

            sendCommand("movel(p[0.56845, -0.42828, -0.31507, 0.005, -3.134, -0.007], a=1.0, v=0.2)");
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            logger.error("Error while drawing B: {}", e.getMessage());
        }
    }
}
