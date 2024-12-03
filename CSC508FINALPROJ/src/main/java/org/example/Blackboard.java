package org.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Blackboard {
    private static final Logger logger = LoggerFactory.getLogger(Blackboard.class);
    private static Blackboard instance;
    private String data;
    private final PropertyChangeSupport support;

    // Private constructor for Singleton
    private Blackboard() {
        support = new PropertyChangeSupport(this);
    }

    // Get the singleton instance
    public static synchronized Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
            logger.info("Blackboard instance created.");
        }
        return instance;
    }

    // Add a listener
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
        logger.debug("Listener added: {}", listener.getClass().getName());
    }

    // Remove a listener
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
        logger.debug("Listener removed: {}", listener.getClass().getName());
    }

    // Set data and notify listeners
    public void setData(String data) {
        String oldData = this.data;
        this.data = data;
        support.firePropertyChange("data", oldData, data);
        logger.info("Data updated in Blackboard: {}", data);
    }

    // Get the current data
    public String getData() {
        return data;
    }
}
