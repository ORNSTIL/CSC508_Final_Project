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
    private final CobotDrawLetterCommandGenerator letterCommandGenerator;
    private Blackboard blackboard;


    private final String baseMoveCommand = "movel(p[%5f, %5f, %5f, 0.005, -3.134, -0.007], a=1.0, v=0.2)";

    // Start position (joint angles)
    private static final String START_POSITION = "movej([1.14, -2.16, -1.58, -1.57, 1.57, 0], a=1.0, v=0.5)";

    public Cobot() {
        blackboard = Blackboard.getInstance();
        blackboard.addPropertyChangeListener(this);
        logger.info("Cobot registered as a listener to Blackboard.");
        letterCommandGenerator = new CobotDrawLetterCommandGenerator();
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
        logger.info("Cobot detected property change");
        if ("cobotCommandAdded".equals(evt.getPropertyName())) {
            String command = blackboard.removeFromCobotCommands();
            logger.info("Cobot received command from Blackboard: {}", command);
            sendCommand(command);
        }
    }
}
