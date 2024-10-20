package com.jcode.analyzer.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OperationContextImpl extends OperationContext {

    // SLF4J Logger
    private static final Logger logger = LoggerFactory.getLogger(OperationContextImpl.class);

    // Concurrent Map to store application-level data
    private Map<String, Object> applicationData;

    /**
     * Default constructor initializes the application data map.
     */
    public OperationContextImpl() {
        applicationData = new ConcurrentHashMap<>();
    }

    /**
     * Copy constructor for deep cloning.
     *
     * @param other The other OperationContext to clone.
     */
    public OperationContextImpl(OperationContext other) {
        this.applicationData = new ConcurrentHashMap<>(other.getApplicationContext());
        logger.info("OperationContextImpl cloned: {}", applicationData);
    }

    /**
     * Adds a key-value pair to the application context.
     *
     * @param name The key to add.
     * @param value The value associated with the key.
     */
    @Override
    public void add2ApplicationContext(String name, Object value) {
        if (applicationData == null) {
            applicationData = new ConcurrentHashMap<>();
        }
        applicationData.put(name, value);
        logger.info("Added to application context: {} = {}", name, value);
    }

    /**
     * Sets the entire application context from a Map.
     *
     * @param map The map containing key-value pairs to set in the application context.
     */
    @Override
    public void setApplicationContext(Map<String, Object> map) {
        applicationData = new ConcurrentHashMap<>(map);
        logger.info("Application context set: {}", applicationData);
    }

    /**
     * Retrieves the entire application context as a Map.
     *
     * @return The current application context as a Map.
     */
    @Override
    public Map<String, Object> getApplicationContext() {
        return applicationData;
    }

    /**
     * Retrieves a specific value from the application context by key.
     *
     * @param name The key to look up.
     * @return The value associated with the key, or null if not found.
     */
    @Override
    public Object get(String name) {
        if (applicationData == null) {
            logger.warn("No application data found.");
            return null;
        }
        Object value = applicationData.get(name);
        logger.info("Retrieved from application context: {} = {}", name, value);
        return value;
    }

    /**
     * Clones the current OperationContextImpl.
     *
     * @return A new instance of OperationContextImpl that is a clone of the current instance.
     */
    @Override
    public OperationContextImpl clone() {
        return new OperationContextImpl(this);
    }

    /**
     * Sets the context for the current thread.
     *
     * @param txContext The OperationContext to set.
     */
    public static void setContext(OperationContext txContext) {
        context.set(txContext);
        logger.info("Context set for thread: {}", txContext);
    }
}
