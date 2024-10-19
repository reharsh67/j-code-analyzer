package com.jcode.analyzer.helper;

import com.jcode.analyzer.constants.JConstants;
import com.jcode.analyzer.context.OperationContext;
import com.jcode.analyzer.context.OperationContextImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ApplicationHelper {

    public static Map getArgMap() {
        Map map = new HashMap();
        map.put(JConstants.PATH, null);
        map.put(JConstants.VERBOSE, false);
        map.put(JConstants.ALL, false);
        map.put(JConstants.CONDITIONAL, false);
        map.put(JConstants.VAR, false);
        map.put(JConstants.EMPTYSTMT, false);
        map.put(JConstants.INDENTANDWHITE, false);
        return map;
    }

    public static void populateArgMap(String[] args, Map argMap) {
        List<File> files = new ArrayList<>();
        OperationContext ctx = OperationContext.getContext();
        for (String arg : args) {
            if (arg.startsWith("--")) {
                switch(arg) {
                    case JConstants.PATH:
                        argMap.put(JConstants.VERBOSE, true);
                        ctx.add2ApplicationContext(JConstants.VERBOSE, true);
                    case JConstants.ALL:
                        argMap.put(JConstants.ALL, true);
                        ctx.add2ApplicationContext(JConstants.ALL, true);
                    case JConstants.CONDITIONAL:
                        argMap.put(JConstants.CONDITIONAL, true);
                        ctx.add2ApplicationContext(JConstants.CONDITIONAL, true);
                    case JConstants.VAR:
                        argMap.put(JConstants.VAR, true);
                        ctx.add2ApplicationContext(JConstants.VAR, true);
                    case JConstants.INDENTANDWHITE:
                        argMap.put(JConstants.INDENTANDWHITE, true);
                        ctx.add2ApplicationContext(JConstants.INDENTANDWHITE, true);
                    case JConstants.IMPORTS:
                        argMap.put(JConstants.IMPORTS, true);
                        ctx.add2ApplicationContext(JConstants.IMPORTS, true);
                    case JConstants.EMPTYSTMT:
                        argMap.put(JConstants.EMPTYSTMT, true);
                        ctx.add2ApplicationContext(JConstants.EMPTYSTMT, true);
                    default:
                        argMap.put(JConstants.ALL, true);
                        ctx.add2ApplicationContext(JConstants.ALL, true);
                }
            } else {
                File file = new File(arg);
                if (file.exists()) {
                    argMap.put(JConstants.PATH, true);
                    ctx.add2ApplicationContext(JConstants.PATH, file.getAbsolutePath());
                    files.add(file);
                } else {
                    System.out.println("File or directory not found: " + arg);
                }
            }
        }
        ctx.add2ApplicationContext("FileList", files);
        ctx.add2ApplicationContext("JARGS", argMap);
    }

    public static void getManualInput(Map argMap) {
        List<File> files = new ArrayList<>();
        OperationContext ctx = OperationContext.getContext();
        String path = null;
        Scanner sc = new Scanner(System.in);
        {
            System.out.println("Please provide path/to/your/java/file.java");
            path = sc.nextLine();
            File file = new File(path);
            if (file.exists()) {
                argMap.put(JConstants.PATH, true);
                ctx.add2ApplicationContext(JConstants.PATH, file.getAbsolutePath());
                files.add(file);
            } else {
                System.out.println("File or directory not found: " + path);
            }
        }
        {
            System.out.println("Would you like verbose to be enabled Y/N ?");
            String verVal = sc.nextLine();
            if (verVal.equals("y")) {
                argMap.put(JConstants.VERBOSE, true);
                ctx.add2ApplicationContext(JConstants.VERBOSE, true);
            } else {
                argMap.put(JConstants.VERBOSE, false);
                ctx.add2ApplicationContext(JConstants.VERBOSE, false);
            }
        }
        System.out.println("Since this is manual input we will consider all the option may be will add support for options later.");
        argMap.put(JConstants.ALL, true);
        argMap.put(JConstants.PATH, path);
        ctx.add2ApplicationContext(JConstants.PATH, path);
        ctx.add2ApplicationContext("FileList", files);
        ctx.add2ApplicationContext(JConstants.ALL, true);
    }

    public static void init() {
        if (isAlreadySet()) {
            return;
        }
        OperationContextImpl.setContext(new OperationContextImpl());
    }

    private static boolean isAlreadySet() {
        OperationContextImpl context = (OperationContextImpl) OperationContextImpl.getContext();
        return (context != null);
    }
}
