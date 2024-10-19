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

    public static Map<String, Object> getArgMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(JConstants.PATH, null);
        map.put(JConstants.VERBOSE, false);
        map.put(JConstants.ALL, false);
        map.put(JConstants.CONDITIONAL, false);
        map.put(JConstants.VAR, false);
        map.put(JConstants.EMPTYSTMT, false);
        map.put(JConstants.INDENTANDWHITE, false);
        return map;
    }

    public static void populateArgMap(String[] args, Map<String, Object> argMap) {
        List<File> files = new ArrayList<>();
        OperationContext ctx = OperationContext.getContext();
        for (String arg : args) {
            if (arg.startsWith("--")) {
                switch(arg) {
                    case JConstants.PATH:
                        argMap.put(JConstants.PATH, true);
                        ctx.add2ApplicationContext(JConstants.PATH, true);
                        break;
                    case JConstants.ALL:
                        argMap.put(JConstants.ALL, true);
                        ctx.add2ApplicationContext(JConstants.ALL, true);
                        break;
                    case JConstants.CONDITIONAL:
                        argMap.put(JConstants.CONDITIONAL, true);
                        ctx.add2ApplicationContext(JConstants.CONDITIONAL, true);
                        break;
                    case JConstants.VAR:
                        argMap.put(JConstants.VAR, true);
                        ctx.add2ApplicationContext(JConstants.VAR, true);
                        break;
                    case JConstants.INDENTANDWHITE:
                        argMap.put(JConstants.INDENTANDWHITE, true);
                        ctx.add2ApplicationContext(JConstants.INDENTANDWHITE, true);
                        break;
                    case JConstants.IMPORTS:
                        argMap.put(JConstants.IMPORTS, true);
                        ctx.add2ApplicationContext(JConstants.IMPORTS, true);
                        break;
                    case JConstants.EMPTYSTMT:
                        argMap.put(JConstants.EMPTYSTMT, true);
                        ctx.add2ApplicationContext(JConstants.EMPTYSTMT, true);
                        break;
                    default:
                        argMap.put(JConstants.ALL, true);
                        ctx.add2ApplicationContext(JConstants.ALL, true);
                        break;
                }
            } else {
                processFilePath(arg, argMap, files, ctx);
            }
        }
        ctx.add2ApplicationContext("FileList", files);
        ctx.add2ApplicationContext("JARGS", argMap);
    }

    public static void getManualInput(Map<String, Object> argMap) {
        List<File> files = new ArrayList<>();
        OperationContext ctx = OperationContext.getContext();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please provide path/to/your/java/file.java");
        String path = sc.nextLine();
        processFilePath(path, argMap, files, ctx);
        System.out.println("Would you like verbose to be enabled Y/N ?");
        String verVal = sc.nextLine();
        boolean verbose = verVal.equalsIgnoreCase("y");
        argMap.put(JConstants.VERBOSE, verbose);
        ctx.add2ApplicationContext(JConstants.VERBOSE, verbose);
        System.out.println("Since this is manual input we will consider all the option may be will add support for options later.");
        argMap.put(JConstants.ALL, true);
        ctx.add2ApplicationContext(JConstants.ALL, true);
        ctx.add2ApplicationContext("FileList", files);
    }

    private static void processFilePath(String path, Map<String, Object> argMap, List<File> files, OperationContext ctx) {
        File file = new File(path);
        if (file.exists()) {
            argMap.put(JConstants.PATH, path);
            ctx.add2ApplicationContext(JConstants.PATH, file.getAbsolutePath());
            files.add(file);
        } else {
            System.out.println("File or directory not found: " + path);
        }
    }

    public static void init() {
        if (!isAlreadySet()) {
            OperationContextImpl.setContext(new OperationContextImpl());
        }
    }

    private static boolean isAlreadySet() {
        return OperationContextImpl.getContext() != null;
    }
}
