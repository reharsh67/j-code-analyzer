package com.jcode.analyzer.context;

import java.util.HashMap;
import java.util.Map;

public class OperationContextImpl extends OperationContext {

    // Map to store application-level data
    private Map<String, Object> applicationData;

    // Method to add a key-value pair to the application context
    @Override
    public void add2ApplicationContext(String name, Object value) {
        if (applicationData == null) {
            applicationData = new HashMap<>();
        }
        applicationData.put(name, value);
        System.out.println("Added to application context: " + name + " = " + value);
    }

    // Method to set the entire application context from a Map
    @Override
    public void setApplicationContext(Map<String, Object> map) {
        applicationData = map;
        System.out.println("Application context set: " + applicationData);
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
            System.out.println("No application data found.");
            return null;
        }
        Object value = applicationData.get(name);
        System.out.println("Retrieved from application context: " + name + " = " + value);
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
        System.out.println("Context set for thread: " + txContext);
    }

    // Default constructor
    public OperationContextImpl() {
        applicationData = new HashMap<>();
    }

    // Copy constructor for deep cloning
    public OperationContextImpl(OperationContext other) {
        // Deep copy of operations
        this.applicationData = new HashMap<>(other.getApplicationContext());
        System.out.println("OperationContextImpl cloned: " + applicationData);
    }
}
