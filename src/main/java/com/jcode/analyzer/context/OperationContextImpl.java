package com.jcode.analyzer.context;

import java.util.HashMap;
import java.util.Map;

public class OperationContextImpl extends OperationContext{

    private Map applicationData;
    @Override
    public void add2ApplicationContext(String name, Object value) {
        if (applicationData == null) {
            applicationData = new HashMap();
        }
        applicationData.put(name, value);
    }

    @Override
    public void setApplicationContext(Map map) {
        applicationData = map;
    }

    @Override
    public Map getApplicationContext() {
        return applicationData;
    }

    public Object get(String name) {
        if (applicationData == null)
            return null;
        return applicationData.get(name);
    }
    public static void setContext(OperationContext txContext) {
        context.set(txContext);
    }
}
