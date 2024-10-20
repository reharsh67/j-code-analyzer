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

    // Method to add a key-value pair to the application context
    @Override
    public void add2ApplicationContext(String name, Object value) {
        if (applicationData == null) {
            applicationData = new ConcurrentHashMap<>();
        }
        applicationData.put(name, value);
        logger.info("Added to application context: {} = {}", name, value);
    }

    // Method to set the entire application context from a Map
    @Override
    public void setApplicationContext(Map<String, Object> map) {
        applicationData = new ConcurrentHashMap<>(map);
        logger.info("Application context set: {}", applicationData);
    }

    // Method to retrieve the entire application context as a Map
    @Override
    public Map<String, Object> getApplicationContext() {
        return applicationData;
    }

    // Method to get a specific value from the application context by key
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

    // Method to clone the current OperationContextImpl
    @Override
    public OperationContextImpl clone() {
        return new OperationContextImpl(this);
    }

    // Method to set the context for the current thread
    public static void setContext(OperationContext txContext) {
        context.set(txContext);
        logger.info("Context set for thread: {}", txContext);
    }

    // Default constructor
    public OperationContextImpl() {
        applicationData = new ConcurrentHashMap<>();
    }

    // Copy constructor for deep cloning
    public OperationContextImpl(OperationContext other) {
        // Deep copy of the application context
        this.applicationData = new ConcurrentHashMap<>(other.getApplicationContext());
        logger.info("OperationContextImpl cloned: {}", applicationData);
    }
}
