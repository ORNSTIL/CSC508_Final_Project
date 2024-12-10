package org.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Blackboard {
    private static final Logger logger = LoggerFactory.getLogger(Blackboard.class);
    private static Blackboard instance;
    private String keyboardLetter;
    private ArrayList<String> cobotCommands;
    private final PropertyChangeSupport support;

    private Blackboard() {
        support = new PropertyChangeSupport(this);
        cobotCommands = new ArrayList<>();
    }

    public static synchronized Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
            logger.info("Blackboard instance created.");
        }
        return instance;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
        logger.debug("Listener added: {}", listener.getClass().getName());
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
        logger.debug("Listener removed: {}", listener.getClass().getName());
    }

    public void setKeyboardLetter(String keyboardLetter) {
        String oldData = this.keyboardLetter;
        this.keyboardLetter = keyboardLetter;
        support.firePropertyChange("keyboardLetter", oldData, keyboardLetter);
        logger.info("Keyboard letter updated in Blackboard: {}", keyboardLetter);
    }

    public String getKeyboardLetter() {
        return keyboardLetter;
    }

    public void clearCobotCommands() {
        ArrayList<String> oldData = (ArrayList<String>) this.cobotCommands.clone();
        cobotCommands = new ArrayList<>();
        support.firePropertyChange("cobotCommandAdded", oldData, cobotCommands);
        logger.info("Cobot Commands updated in Blackboard: {}", cobotCommands);
    }

    public void addToCobotCommands(String newCommand) {
        ArrayList<String> oldData = (ArrayList<String>) this.cobotCommands.clone();
        cobotCommands.add(newCommand);
        support.firePropertyChange("cobotCommandAdded", oldData, cobotCommands);
        logger.info("Cobot Commands updated in Blackboard: {}", cobotCommands);
    }

    public String removeFromCobotCommands() {
        ArrayList<String> oldData = (ArrayList<String>) this.cobotCommands.clone();
        String removedCommand = cobotCommands.remove(0);
        support.firePropertyChange("cobotCommandRemoved", oldData, cobotCommands);
        logger.info("Data updated in Blackboard: {}", cobotCommands);
        return removedCommand;
    }
}
