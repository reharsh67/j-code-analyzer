package com.jcode.analyzer.context;

import java.util.Map;

public abstract class OperationContext {

    // ThreadLocal variable to hold the context for each thread
    protected static final ThreadLocal<OperationContext> context = new ThreadLocal<>();

    // Method to get the current OperationContext for the thread
    public static OperationContext getContext() {
        return context.get();
    }

    // Method to add a key-value pair to the application context
    public abstract void add2ApplicationContext(String name, Object value);

    // Method to set the entire application context from a Map
    public abstract void setApplicationContext(Map<String, Object> context);

    // Method to retrieve the entire application context as a Map
    public abstract Map<String, Object> getApplicationContext();

    // Method to get a specific value from the application context by key
    public abstract Object get(String name);

    // Method to clone the current OperationContext
    public abstract OperationContext clone();
}
