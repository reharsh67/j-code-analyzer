package com.jcode.analyzer.helper;

import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.context.OperationContext;
import com.jcode.analyzer.context.OperationContextImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ApplicationHelper {
    public static Map getArgMap() {
        Map map = new HashMap();
        map.put(JConstants.PATH,null);
        map.put(JConstants.VERBOSE,false);
        map.put(JConstants.ALL,false);
        map.put(JConstants.CONDITIONAL,false);
        map.put(JConstants.VAR,false);
        map.put(JConstants.EMPTYSTMT,false);
        map.put(JConstants.INDENTANDWHITE,false);

        return map;
    }

    public static void populateArgMap(String[] args, Map argMap) {
        List argList = Arrays.asList(args);
        if(argList.size() > 0){
           argMap.put(JConstants.PATH,argList.get(0));
            OperationContext.getContext().add2ApplicationContext(JConstants.PATH,argList.get(0));
            if(argList.contains(JConstants.VERBOSE)){
                argMap.put(JConstants.VERBOSE,true);
                OperationContext.getContext().add2ApplicationContext(JConstants.VERBOSE,true);
            }
           if(argList.contains(JConstants.ALL)){
               argMap.put(JConstants.ALL,true);
               OperationContext.getContext().add2ApplicationContext(JConstants.ALL,true);
           }
           else{
               if(argList.contains(JConstants.CONDITIONAL)){
                   argMap.put(JConstants.CONDITIONAL,true);
                   OperationContext.getContext().add2ApplicationContext(JConstants.CONDITIONAL,true);
               }
               if(argList.contains(JConstants.VAR)){
                   argMap.put(JConstants.EMPTYSTMT,true);
                   argMap.put(JConstants.VAR,true);
                   OperationContext.getContext().add2ApplicationContext(JConstants.EMPTYSTMT,true);
                   OperationContext.getContext().add2ApplicationContext(JConstants.VAR,true);
               }
               if(argList.contains(JConstants.INDENTANDWHITE)){
                   argMap.put(JConstants.INDENTANDWHITE,true);
                   OperationContext.getContext().add2ApplicationContext(JConstants.INDENTANDWHITE,true);
               }
               if(argList.contains(JConstants.IMPORTS)){
                   argMap.put(JConstants.IMPORTS,true);
                   OperationContext.getContext().add2ApplicationContext(JConstants.IMPORTS,true);
               }
           }
        }
        OperationContext.getContext().add2ApplicationContext("JARGS",argMap);
    }

    public static void getManualInput(Map argMap) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please provide path/to/your/java/file.java");
        String path = sc.nextLine();
        OperationContext.getContext().add2ApplicationContext(JConstants.PATH,path);
        argMap.put(JConstants.PATH,path);
        System.out.println("Would you like verbose to be enabled Y/N ?");
        String verVal = sc.nextLine();
        if (verVal.equals("y")) {
            argMap.put(JConstants.VERBOSE,true);
            OperationContext.getContext().add2ApplicationContext(JConstants.VERBOSE,true);
        }
        else{
            argMap.put(JConstants.VERBOSE,false);
            OperationContext.getContext().add2ApplicationContext(JConstants.VERBOSE,false);
        }
        System.out.println("Since this is manual input we will consider all the option may be will add support for options later.");
        argMap.put(JConstants.ALL,true);
        OperationContext.getContext().add2ApplicationContext(JConstants.ALL,true);
    }

    public static void init() {
        if(isAlreadySet()){
            return;
        }
        OperationContextImpl.setContext(new OperationContextImpl());

    }

    private static boolean isAlreadySet(){
        OperationContextImpl context = (OperationContextImpl)
                OperationContextImpl.getContext();
        return (context != null);
    }
}
