package com.jcode.analyzer.context;

import java.util.Map;

public abstract class OperationContext {
    protected static final ThreadLocal context = new ThreadLocal();
    public static OperationContext getContext(){
        return (OperationContext)context.get();
    }
    public abstract void add2ApplicationContext(String name,Object value);
    public abstract void setApplicationContext(Map context);
    public abstract Map getApplicationContext();
    public abstract Object get(String name);


}
