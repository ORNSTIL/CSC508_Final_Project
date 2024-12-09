package org.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class representing a central storage for letter input, managing observers and notifying them of property changes.
 *
 * @version 1.0
 * @author Luca Ornstil
 * @author Monish Suresh
 * @author Christine Widden
 */

public class Blackboard {
    private static final Logger logger = LoggerFactory.getLogger(Blackboard.class);
    private static Blackboard instance;
    private String data;
    private final PropertyChangeSupport support;

    private Blackboard() {
        support = new PropertyChangeSupport(this);
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

    public void setData(String data) {
        if (data == null || data.trim().isEmpty()) {
            logger.warn("Attempted to set null or empty data in Blackboard.");
            return;
        }

        String oldData = this.data;
        this.data = null; // Set to null temporarily
        support.firePropertyChange("data", oldData, null);

        this.data = data; // Now set the actual data
        support.firePropertyChange("data", null, data);

        logger.info("Data updated in Blackboard: {}", data);
    }

    public String getData() {
        return data;
    }
}
